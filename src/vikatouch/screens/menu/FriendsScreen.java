package vikatouch.screens.menu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.PressableUIItem;
import vikamobilebase.ErrorCodes;
import vikamobilebase.VikaUtils;
import vikatouch.base.INextLoadable;
import vikatouch.base.Settings;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.FriendItem;
import vikatouch.base.items.GroupItem;
import vikatouch.base.items.LoadMoreButtonItem;
import vikatouch.screens.MainScreen;

public class FriendsScreen 
	extends MainScreen implements INextLoadable
{
	
	public FriendsScreen()
	{
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}
	
	public boolean isReady()
	{
		return uiItems != null;
	}
	public static void AbortLoading() {
		try {
			if(downloaderThread != null && downloaderThread.isAlive())
				downloaderThread.interrupt();
		} catch (Exception e) { }
	}
	
	public static Thread downloaderThread;
	
	public static FriendsScreen current;
	
	public int currId;
	public int fromF;
	public String whose = null;
	public String range = null;
	public int totalItems;
	public boolean canLoadMore = true;

	public void LoadFriends(final int from, final int id, final String name)
	{
		scrolled = 0;
		uiItems = null;
		final FriendsScreen thisC = current = this;
		fromF = from;
		currId = id;
		whose = name;
		
		AbortLoading();

		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					System.out.println("Friends list");
					VikaTouch.loading = true;
					repaint();
					String x = VikaUtils.download(new URLBuilder("friends.get").addField("count", Settings.simpleListsLength).addField("fields", "domain,last_seen,photo_50").addField("offset", from).addField("user_id", id));
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
						repaint();
						Thread.sleep(1000);
						VikaTouch.loading = true;
						for(int i = 0; i < itemsCount - (canLoadMore?1:0); i++)
						{
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
				catch (Exception e)
				{
					e.printStackTrace();
					VikaTouch.error(e, ErrorCodes.FRIENDSLOAD);
				}
				VikaTouch.loading = false;
			}
		};

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

			drawHeaders(g, uiItems==null?"Люди (загрузка...)":(currId<0?"Участники":"Друзья")+(range==null?"":range)+" "+(whose==null?"":whose));

		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.FRIENDSDRAW);
			e.printStackTrace();
		}

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
		LoadFriends(fromF+Settings.simpleListsLength, currId, whose);
	}
}
