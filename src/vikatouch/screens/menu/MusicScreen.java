package vikatouch.screens.menu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.menu.*;
import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.popup.ContextMenu;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.IconsManager;
import vikatouch.VikaTouch;
import vikatouch.items.menu.AudioTrackItem;
import vikatouch.items.menu.FriendItem;
import vikatouch.items.menu.OptionItem;
import vikatouch.items.menu.PlaylistItem;
import vikatouch.screens.*;
import vikatouch.utils.ErrorCodes;
import vikatouch.utils.url.URLBuilder;

public class MusicScreen
	extends MainScreen
{
	
	public int ownerId;
	public int albumId;
	
	public String title;
	
	public static Thread downloaderThread;

	public void load(final int oid, final int albumId, String title)
	{
		scrolled = 0;
		uiItems = null;
		final MusicScreen thisC = this;
		this.albumId = albumId;
		ownerId = oid;
		this.title = title;
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();
		
		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("audio.get").addField("owner_id", oid).addField("album_id", albumId).addField("count", 100).addField("offset", 0));
					try
					{
						System.out.println(x);
						VikaTouch.loading = true;
						JSONObject response = new JSONObject(x).getJSONObject("response");
						JSONArray items = response.getJSONArray("items");
						itemsCount = (short) items.length();
						uiItems = new PressableUIItem[itemsCount];
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new AudioTrackItem(item, thisC, i);
							((AudioTrackItem) uiItems[i]).parseJSON();
							//Thread.yield();
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						//VikaTouch.error(e, ErrorCodes.DOCUMENTSPARSE);
					}
					VikaTouch.loading = false;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					//VikaTouch.error(e, ErrorCodes.DOCUMENTSLOAD);
				}
				VikaTouch.loading = false;
				System.gc();
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
				VikaTouch.error(e, ErrorCodes.MUSICITEMDRAW);
				e.printStackTrace();
			}
			g.translate(0, -g.getTranslateY());
		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.MUSICDRAW);
			e.printStackTrace();
		}
	}
	
	public final void release(int x, int y)
	{
		try 
		{
			if(y > 58 && y < DisplayUtils.height - oneitemheight)
			{
				int h = 50;
				int yy1 = y - (scrolled + 58);
				int i = yy1 / h;
				if(i < 0)
					i = 0;
				if(!dragging)
				{
					uiItems[i].tap(x, yy1 - (h * i));
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

	public static void open(final int id, final String name)
	{
		IMenu m = new EmptyMenu()
		{
			public void onMenuItemPress(int i) 
			{ 
				if(i==0)
				{
					MusicScreen pls = new MusicScreen();
					pls.load(id,0,name);
					VikaTouch.setDisplay(pls, 1);
				}
				else if(i==1)
				{
					PlaylistsScreen pls = new PlaylistsScreen();
					pls.load(id,name);
					VikaTouch.setDisplay(pls, 1);
				}
			}
		};
		OptionItem[] oi = new OptionItem[]
		{ 
			new OptionItem(m, "Вся музыка", IconsManager.MUSIC, 0, 50),
			new OptionItem(m, "Плейлисты", IconsManager.MENU, 1, 50)
		};
		VikaTouch.popup(new ContextMenu(oi));
	}
}
