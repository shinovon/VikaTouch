package vikatouch.screens.menu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import vikamobilebase.ErrorCodes;
import vikamobilebase.VikaUtils;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.FriendItem;
import vikatouch.screens.MainScreen;

public class FriendsScreen 
	extends MainScreen
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
	
	public final static int loadFriendsCount = 100;
	public static Thread downloaderThread;

	public void LoadFriends()
	{
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();

		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					System.out.println("Friends list");
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("friends.get").addField("count", loadFriendsCount).addField("fields", "domain,last_seen,photo_50"));
					try
					{
						VikaTouch.loading = true;
						JSONObject response = new JSONObject(x).getJSONObject("response");
						JSONArray items = response.getJSONArray("items");
						System.out.println(items.toString());
						itemsCount = (short) items.length();
						uiItems = new FriendItem[items.length()];
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new FriendItem(item);
							((FriendItem) uiItems[i]).parseJSON();
						}

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

			drawHeaders(g, "Друзья");

		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.FRIENDSDRAW);
			e.printStackTrace();
		}

	}
	public final void release(int x, int y)
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

		super.release(x, y);
	}

}
