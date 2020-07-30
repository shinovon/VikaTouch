package vikaTouch.canvas.menu;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.canvas.MainCanvas;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.items.DocItem;
import vikaTouch.newbase.items.GroupItem;


public class GroupsCanvas extends MainCanvas {

	public GroupsCanvas()
	{
		super();
		VikaTouch.loading = true;
		if(VikaTouch.menuCanv == null)
			VikaTouch.menuCanv = new MenuCanvas();

		try
		{
			switch(DisplayUtils.idispi)
			{
				case DisplayUtils.DISPLAY_PORTRAIT:
				case DisplayUtils.DISPLAY_ALBUM:
				case DisplayUtils.DISPLAY_E6:
				{
					if(menuImg == null)
						menuImg = Image.createImage("/menu.png");
					if(newsImg == null)
						newsImg = Image.createImage("/lenta.png");
					break;
				}
				case DisplayUtils.DISPLAY_S40:
				case DisplayUtils.DISPLAY_ASHA311:
				case DisplayUtils.DISPLAY_EQWERTY:
				{
					if(menuImg == null)
						menuImg = VikaUtils.resize(Image.createImage("/menu.png"), 10, 9);
					if(newsImg == null)
						newsImg = VikaUtils.resize(Image.createImage("/lenta.png"), 11, 12);
					break;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isReady() {
		return gi!=null;
	}
	
	public final static int loadGroupsCount = 100;
	private static GroupItem[] gi = null;
	public static Thread downloaderThread;

	public void LoadGroups()
	{
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();

		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("groups.get").addField("extended", "1").addField("count", loadGroupsCount).addField("fields", "members_count,counters"));
					try
					{
						VikaTouch.loading = true;
						JSONObject response = new JSONObject(x).getJSONObject("response");
						JSONArray items = response.getJSONArray("items");
						System.out.println(items.toString());
						itemsCount = items.length();
						gi = new GroupItem[items.length()];
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							gi[i] = new GroupItem(item);
							gi[i].parseJSON();
						}

					}
					catch (JSONException e)
					{
						e.printStackTrace();
						VikaTouch.error(e, "Парс списка групп");
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
					VikaTouch.error(e, "Загрузка списка групп");
				}
				VikaTouch.loading = false;
			}
		};

		downloaderThread.start();
	}
	public void paint(Graphics g)
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
				if(gi!=null)
				{
					for(int i = 0; i < itemsCount; i++)
					{
						if(gi[i] != null)
						{
							gi[i].paint(g, y, scrolled);
							y += gi[i].itemDrawHeight;
						}
	
					}
				}
			}
			catch (Exception e)
			{
				VikaTouch.error(e, "Прорисовка объектов: группы");
			}
			g.translate(0, -g.getTranslateY());

			drawHeaders(g, "Группы");

		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: группы");
			e.printStackTrace();
		}

	}
	public final void pointerReleased(int x, int y)
	{
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_ALBUM:
			case DisplayUtils.DISPLAY_PORTRAIT:
			{
				if(y > 58 && y < DisplayUtils.height - oneitemheight)
				{
					int h = 48 + (DocItem.BORDER * 2);
					int yy1 = y - (scrolled + 58);
					int i = yy1 / h;
					if(i < 0)
						i = 0;
					if(!dragging)
					{
						gi[i].tap(x, yy1 - (h * i));
					}
					break;
				}
				break;
			}

		}

		super.pointerReleased(x, y);
	}
}
