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
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;

public class DocsCanvas
	extends MainCanvas
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


	public final static int loadDocsCount = 15;
	public int fromDoc = 0;
	public int docsCount = 0;
	public int totalDocs = 0;
	public static Thread downloaderThread;

	public boolean isPreviewShown = false;
	public Image previewImage = null;
	public int previewY = 0;
	
	public String whose = null;

	public void LoadDocs(final int from, final int count, final int id)
	{
		uiItems = new DocItem[count];
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
						itemsCount = response.getInt("count");
						if(itemsCount > count)
						{
							itemsCount = count;
						}
						System.out.println("Получено "+itemsCount+" документов");
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							uiItems[i] = new DocItem(item);
							((DocItem) uiItems[i]).parseJSON();
						}

					}
					catch (JSONException e)
					{
						e.printStackTrace();
						VikaTouch.error(e, "Парс списка документов");
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
					VikaTouch.error(e, "Загрузка списка документов");
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
				for(int i = 0; i < itemsCount; i++)
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

			drawHeaders(g, "Документы");

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
					g.drawImage(previewImage, 0, previewY, 0);
				}
			}
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: превью картинки");
			e.printStackTrace();
		}

	}
	public final void pointerReleased(int x, int y)
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

		super.pointerReleased(x, y);
	}

}
