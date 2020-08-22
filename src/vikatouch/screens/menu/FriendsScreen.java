package vikatouch.screens.menu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.VikaTouch;
import vikatouch.items.LoadMoreButtonItem;
import vikatouch.items.menu.FriendItem;
import vikatouch.items.menu.GroupItem;
import vikatouch.json.INextLoadable;
import vikatouch.local.TextLocal;
import vikatouch.screens.MainScreen;
import vikatouch.settings.Settings;
import vikatouch.utils.ErrorCodes;
import vikatouch.utils.url.URLBuilder;

public class FriendsScreen 
	extends MainScreen implements INextLoadable
{
	
	private static String loadingStr;

	private static String membersStr;

	private static String friendsStr;

	private static String peopleStr;

	public FriendsScreen()
	{
		if(loadingStr == null)
		{
			loadingStr = TextLocal.inst.get("title.loading");
			peopleStr = TextLocal.inst.get("title.people");
			friendsStr = TextLocal.inst.get("title.friends");
			membersStr = TextLocal.inst.get("title.members");
		}
	}
	
	public boolean isReady()
	{
		return uiItems != null;
	}
	public static void abortLoading() {
		try {
			if(downloaderThread != null && downloaderThread.isAlive())
				downloaderThread.interrupt();
		} catch (Exception e) { }
	}
	
	public static Thread downloaderThread;
	
	
	public int currId;
	public int fromF;
	public String whose = null;
	public String range = null;
	public int totalItems;
	public boolean canLoadMore = true;

	private String name2;

	private String formattedTitle;

	public void loadFriends(final int from, final int id, final String name, final String name2)
	{
		formattedTitle = peopleStr;
		scrolled = 0;
		uiItems = null;
		final FriendsScreen thisC = this;
		fromF = from;
		currId = id;
		whose = name;
		this.name2 = name2;
		
		abortLoading();

		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					//System.out.println("Friends list");
					VikaTouch.loading = true;
					repaint();
					String x;
					if(id<0) {
						// как участники
						//!!! это дает ошибку! и я не знаю почему!
						// И какую же? У меня ни разу не падало.
						x = VikaUtils.download(new URLBuilder("groups.getMembers").addField("count", Settings.simpleListsLength).addField("fields", "domain,last_seen,photo_50").addField("offset", from).addField("group_id", -id));
					} else {
						// как друзья
						x = VikaUtils.download(new URLBuilder("friends.get").addField("count", Settings.simpleListsLength).addField("fields", "domain,last_seen,photo_50").addField("offset", from).addField("user_id", id));
					}
					try
					{
						VikaTouch.loading = true;
						JSONObject response = new JSONObject(x).getJSONObject("response");
						JSONArray items = response.getJSONArray("items");
						totalItems = response.getInt("count");
						itemsCount = (short) items.length();
						canLoadMore = totalItems > from+Settings.simpleListsLength;
						uiItems = new PressableUIItem[itemsCount+(canLoadMore?1:0)];
						for(int i = 0; i < itemsCount; i++)
						{
							VikaTouch.loading = true;
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new FriendItem(item);
							((FriendItem) uiItems[i]).parseJSON();
						}
						range = " ("+(from+1)+"-"+(itemsCount+from)+")";
						if(canLoadMore) {
							uiItems[itemsCount] = new LoadMoreButtonItem(thisC);
							itemsCount++;
						}
						if(keysMode) {
							currentItem = 0;
							uiItems[0].setSelected(true);
						}
						VikaTouch.loading = true;
						if(whose == null && name2 != null)
							whose = name2;
						
						if(whose == null || name2 == null)
							formattedTitle = TextLocal.inst.get("title.friends");
						else
							formattedTitle = TextLocal.inst.getFormatted("title.friendsw", new String[] { whose, name2 });
						
						repaint();
						Thread.sleep(1000); // ну вдруг юзер уже нажмёт? Зачем зря грузить
						VikaTouch.loading = true;
						for(int i = 0; i < itemsCount - (canLoadMore?1:0); i++)
						{
							/*
							if(!this.isAlive())
							{
								return;
							}
							*/
							if(!(VikaTouch.canvas.currentScreen instanceof FriendsScreen))
							{
								VikaTouch.loading = false; return; // Костыль деревянный, 1 штука, 78 lvl, 6 ранг
								//не одобряю. для чего создали thread.isAlive()?
							}
							VikaTouch.loading = true;
							((FriendItem) uiItems[i]).GetAva();
						}
						VikaTouch.loading = false;
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						VikaTouch.error(e, ErrorCodes.FRIENDSPARSE);
					}

					VikaTouch.loading = false;
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}
				catch (InterruptedException e)
				{ }
				catch (Exception e)
				{
					e.printStackTrace();
					VikaTouch.error(e, ErrorCodes.FRIENDSLOAD);
				}
				VikaTouch.loading = false;
			}
		};
		hasBackButton = true;

		downloaderThread.start();
	}
	public void draw(Graphics g)
	{
		ColorUtils.setcolor(g, 0);
		g.setFont(Font.getFont(0, 0, 8));
		itemsh = itemsCount * 52;
		double multiplier = (double)DisplayUtils.height / 640.0;
		double ww = 10.0 * multiplier;
		int w = (int)ww;
		try
		{
			update(g);
			int y = oneitemheight + w;
			try
			{
				if(uiItems != null)
				{
					for(int i = 0; i < itemsCount; i++)
					{
						if(uiItems[i] != null)
						{
							uiItems[i].paint(g, y, scrolled);
							y += uiItems[i].getDrawHeight();
						}
	
					}
				}
			}
			catch (Exception e)
			{
				VikaTouch.error(e, ErrorCodes.FRIENDSITEMDRAW);
			}
			g.translate(0, -g.getTranslateY());
		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.FRIENDSDRAW);
			e.printStackTrace();
		}
	}
	
	public final void drawHUD(Graphics g)
	{
		//super.drawHUD(g, uiItems==null?peopleStr+" ("+loadingStr+"...)":(currId<0?membersStr:friendsStr)/*+(range==null?"":range)*/+" "+(whose==null?"":whose));
		super.drawHUD(g, formattedTitle);
	}
	
	public final void release(int x, int y)
	{
		try 
		{
		switch(DisplayUtils.idispi)
			{
				case DisplayUtils.DISPLAY_ALBUM:
				case DisplayUtils.DISPLAY_PORTRAIT:
				{
					if(y > 58 && y < DisplayUtils.height - oneitemheight)
					{
						int h = 48 + (FriendItem.BORDER * 2);
						int yy1 = y - (scrolled + 58);
						int i = yy1 / h;
						if(i < 0)
							i = 0;
						if(!dragging)
						{
							uiItems[i].tap(x, yy1 - (h * i));
						}
						break;
					}
					break;
				}
	
			}
		}
		catch (ArrayIndexOutOfBoundsException e) 
		{ }
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		super.release(x, y);
	}
	public void loadNext() {
		loadFriends(fromF+Settings.simpleListsLength, currId, whose, name2);
	}
}
