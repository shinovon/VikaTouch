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
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.items.DocItem;
import vikaTouch.newbase.items.GroupItem;
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;


public class GroupsCanvas
	extends MainCanvas
{

	public GroupsCanvas()
	{
		super();
		VikaTouch.loading = true;
		if(VikaTouch.menuCanv == null)
			VikaTouch.menuCanv = new MenuCanvas();

		this.menuImg = MenuCanvas.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}

	public boolean isReady()
	{
		return uiItems != null;
	}
	
	public final static int loadGroupsCount = 100;
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
						uiItems = new GroupItem[items.length()];
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new GroupItem(item);
							((GroupItem) uiItems[i]).parseJSON();
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
	public final void release(int x, int y)
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
