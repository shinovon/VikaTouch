package vikatouch.screens.menu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.PressableUIItem;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.base.INextLoadable;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.GroupItem;
import vikatouch.base.items.LoadMoreButtonItem;
import vikatouch.base.items.VideoItem;
import vikatouch.base.local.TextLocal;
import vikatouch.base.settings.Settings;
import vikatouch.base.utils.ErrorCodes;
import vikatouch.base.utils.url.URLBuilder;
import vikatouch.screens.MainScreen;

public class VideosScreen
	extends MainScreen implements INextLoadable
{

	private String videosStr;

	public VideosScreen()
	{
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuScr.newsImg;
		videosStr = TextLocal.inst.get("title.videos");
	}

	public static VideosScreen current;

	public int fromVid;
	public int currId;
	public String whose;
	public static Thread downloaderThread;
	public String range = null;
	public boolean canLoadMore = true;

	public void load(final int from, final int id, final String name)
	{
		scrolled = 0;
		uiItems = null;
		final VideosScreen thisC = current = this;
		final int count = Settings.simpleListsLength;
		fromVid = from;
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
					repaint();
					String x = VikaUtils.download(new URLBuilder("video.get").addField("owner_id", id).addField("count", count).addField("offset", from));
					try
					{
						VikaTouch.loading = true;
						System.out.println(x);
						JSONObject response = new JSONObject(x).getJSONObject("response");
						JSONArray items = response.getJSONArray("items");
						int totalVids = response.getInt("count");
						itemsCount = (short) items.length();
						canLoadMore = !(itemsCount<count);
						if(totalVids <= from+count) { canLoadMore = false; }
						uiItems = new PressableUIItem[itemsCount+(canLoadMore?1:0)];
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new VideoItem(item);
							((VideoItem) uiItems[i]).parseJSON();
							//Thread.yield();
						}
						range = " ("+(from+1)+"-"+(itemsCount+from)+")";
						if(canLoadMore) {
							uiItems[itemsCount] = new LoadMoreButtonItem(thisC);
							itemsCount++;
						}
						VikaTouch.loading = true;
						Thread.sleep(500);
						VikaTouch.loading = true;
						for(int i = 0; i < itemsCount - (canLoadMore?1:0); i++)
						{
							((VideoItem) uiItems[i]).loadIcon();
						}
						VikaTouch.loading = false;
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						VikaTouch.error(e, ErrorCodes.VIDEOSPARSE);
					}

					VikaTouch.loading = false;
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					VikaTouch.error(e, ErrorCodes.VIDEOSLOAD);
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
				VikaTouch.error(e, ErrorCodes.VIDEOSDRAW);
			}
			g.translate(0, -g.getTranslateY());

			drawHUD(g, uiItems==null?(videosStr+" (загрузка...)"):(videosStr+(range==null?"":range)+" "+(whose==null?"":whose)));

		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.VIDEOSDRAW);
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
						int h = 50;
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
		load(fromVid+Settings.simpleListsLength, currId, whose);
	}

}
