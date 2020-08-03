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
import vikaTouch.newbase.items.LoadMoreButtonItem;
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;
import vikaUI.PressableUIItem;

public class DocsCanvas
	extends MainCanvas implements INextLoadable
{

	public DocsCanvas()
	{
		super();
		isPreviewShown = false;
		VikaTouch.loading = true;
		if(VikaTouch.menuCanv == null)
			VikaTouch.menuCanv = new MenuCanvas();

		this.menuImg = MenuCanvas.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}

	public static DocsCanvas current;

	public final static int loadDocsCount = 30;
	public int fromDoc = 0;
	public int totalDocs = 0;
	public int currId;
	public static Thread downloaderThread;

	public boolean isPreviewShown = false;
	public Image previewImage = null;
	public int previewX = 0;
	public int previewY = 0;
	
	public String whose = null;
	public String range = null;
	
	public boolean canLoadMore = true;

	public void LoadDocs(final int from, final int id, String name)
	{
		scrolled = 0;
		uiItems = null;
		final DocsCanvas thisC = current = this;
		final int count = loadDocsCount;
		fromDoc = from;
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
					String x = VikaUtils.download(new URLBuilder("docs.get").addField("owner_id", id).addField("count", count).addField("offset", from));
					try
					{
						VikaTouch.loading = true;
						JSONObject response = new JSONObject(x).getJSONObject("response");
						JSONArray items = response.getJSONArray("items");
						totalDocs = response.getInt("count");
						itemsCount = items.length();
						canLoadMore = !(itemsCount<count);
						if(totalDocs == from+count) { canLoadMore = false; }
						uiItems = new PressableUIItem[itemsCount+(canLoadMore?1:0)];
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new DocItem(item);
							((DocItem) uiItems[i]).parseJSON();
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
						VikaTouch.error(e, "Парс списка документов");
					}

					VikaTouch.loading = false;
				}
				catch (Exception e)
				{
					e.printStackTrace();
					VikaTouch.error(e, "Загрузка списка документов");
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
				VikaTouch.error(e, "Прорисовка объектов: Доки");
			}
			g.translate(0, -g.getTranslateY());

			drawHeaders(g, uiItems==null?"Документы (загрузка...)":"Документы"+(range==null?"":range)+" "+(whose==null?"":whose));

		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: Доки");
			e.printStackTrace();
		}
		try {
			if(isPreviewShown) {
				if(previewImage == null) {
					VikaTouch.loading = true;
				} else {
					VikaTouch.loading = false;
					g.setGrayScale(200);
					g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
					g.drawImage(previewImage, previewX, previewY, 0);
				}
			}
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: превью картинки");
			e.printStackTrace();
		}

	}
	public final void release(int x, int y)
	{
		try
		{
			if(isPreviewShown)
			{
				isPreviewShown = false;
				previewImage = null;
				return;
			}
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
		LoadDocs(fromDoc+loadDocsCount, currId, whose);
		
	}

}
