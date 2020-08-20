package vikatouch.screens.menu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.VikaTouch;
import vikatouch.items.LoadMoreButtonItem;
import vikatouch.items.menu.DocItem;
import vikatouch.items.menu.FriendItem;
import vikatouch.items.menu.PlaylistItem;
import vikatouch.local.TextLocal;
import vikatouch.screens.MainScreen;
import vikatouch.utils.ErrorCodes;
import vikatouch.utils.url.URLBuilder;

public class PlaylistsScreen extends MainScreen {
	
	private static String plStr;

	private String loadingStr;

	public PlaylistsScreen()
	{
		super();
		loadingStr = TextLocal.inst.get("title.loading");
		plStr = TextLocal.inst.get("title.playlists");
	}

	public static PlaylistsScreen current;

	public int currId;
	public static Thread downloaderThread;
	
	public String whose = null;

	public void load(final int id, String name)
	{
		scrolled = 0;
		uiItems = null;
		final PlaylistsScreen thisC = current = this;
		currId = id;
		whose = name;
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();
		
		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("audio.getPlaylists").addField("owner_id", id).addField("count", 100).addField("offset", 0));
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
							uiItems[i] = new PlaylistItem(item);
							((PlaylistItem) uiItems[i]).parseJSON();
							//Thread.yield();
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						//VikaTouch.error(e, ErrorCodes.DOCUMENTSPARSE);
					}
					VikaTouch.loading = true;
					repaint();
					Thread.sleep(1000); // ну вдруг юзер уже нажмёт? Зачем зря грузить
					VikaTouch.loading = true;
					for(int i = 0; i < itemsCount; i++)
					{
						if(!(VikaTouch.canvas.currentScreen instanceof PlaylistsScreen))
						{
							VikaTouch.loading = false; return;
						}
						VikaTouch.loading = true;
						((PlaylistItem) uiItems[i]).loadIcon();
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
				if(uiItems!=null) for(int i = 0; i < itemsCount; i++)
				{
					if(uiItems[i] != null)
					{
						uiItems[i].paint(g, y, scrolled);
						y += uiItems[i].getDrawHeight();
					}

				}
			}
			catch (Exception e)
			{
				VikaTouch.error(e, ErrorCodes.DOCUMENTSITEMDRAW);
			}
			g.translate(0, -g.getTranslateY());


		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.DOCUMENTSDRAW);
			e.printStackTrace();
		}
	}
	
	public final void drawHUD(Graphics g)
	{
		drawHUD(g, uiItems==null?plStr+" ("+loadingStr+"...)":plStr+" "+(whose==null?"":whose));
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
						int h = 102;
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

}