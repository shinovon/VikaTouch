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
import vikaTouch.canvas.INextLoadable;
import vikaTouch.canvas.MainCanvas;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.items.DocItem;
import vikaTouch.newbase.items.GroupItem;
import vikaTouch.newbase.items.LoadMoreButtonItem;
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;
import vikaUI.PressableUIItem;


public class GroupsCanvas
	extends MainCanvas implements INextLoadable
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
	public static void AbortLoading() {
		try {
			if(downloaderThread != null && downloaderThread.isAlive())
				downloaderThread.interrupt();
		} catch (Exception e) { }
	}
	public static GroupsCanvas current;
	
	public final static int loadGroupsCount = 50;
	public static Thread downloaderThread;
	
	public int currId;
	public int fromG;
	public String whose = null;
	public String range = null;
	public int totalItems;
	public boolean canLoadMore = true;

	public void LoadGroups(final int from, final int id, final String name)
	{
		scrolled = 0;
		uiItems = null;
		final GroupsCanvas thisC = current = this;
		fromG = from;
		currId = id;
		whose = name;
		
		AbortLoading();

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
						totalItems = response.getInt("count");
						itemsCount = items.length();
						canLoadMore = !(itemsCount<loadGroupsCount);
						if(totalItems == from+loadGroupsCount) { canLoadMore = false; }
						uiItems = new PressableUIItem[itemsCount+(canLoadMore?1:0)];
						for(int i = 0; i < itemsCount; i++)
						{
							VikaTouch.loading = true;
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new GroupItem(item);
							((GroupItem) uiItems[i]).parseJSON();
							//Thread.yield();
						}
						range = " ("+(from+1)+"-"+(itemsCount+from)+")";
						if(canLoadMore) {
							uiItems[itemsCount] = new LoadMoreButtonItem(thisC);
							itemsCount++;
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						VikaTouch.error(e, "Парс списка групп");
					}
					System.out.println("Groups list OK");
					VikaTouch.loading = true;
					repaint();
					Thread.sleep(1500);
					VikaTouch.loading = true;
					for(int i = 0; i < itemsCount - (canLoadMore?1:0); i++)
					{
						VikaTouch.loading = true;
						((GroupItem) uiItems[i]).GetAva();
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

			drawHeaders(g, uiItems==null?"Группы (загрузка...)":"Группы"+(range==null?"":range)+" "+(whose==null?"":whose));

		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: группы");
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
		}
		catch (ArrayIndexOutOfBoundsException e) 
		{ 
			// Всё нормально, просто тапнули ПОД последним элементом.
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		super.release(x, y);
	}

	public void LoadNext() {
		down();
		LoadGroups(fromG+loadGroupsCount, currId, whose);
	}
}
