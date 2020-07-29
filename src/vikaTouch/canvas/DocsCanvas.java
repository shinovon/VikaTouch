package vikaTouch.canvas;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.items.DocItem;

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

	
	public final static int loadDocsCount = 10;
	public int fromDoc = 0;
	public int docsCount = 0;
	public int totalDocs = 0;
	private static DocItem[] docs;
	public static Thread downloaderThread;
	
	public boolean isPreviewShown = false;
	public Image previewImage = null;
	public int previewY = 0;
	
	public void LoadDocs(final int from, final int count)
	{
		docs = new DocItem[count];
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();
		
		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("docs.get").addField("count", count).addField("offset", from));
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
							docs[i] = new DocItem(item);
							docs[i].parseJSON();
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
		itemsh = itemsCount * 63;
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
					if(docs[i] != null)
					{
						docs[i].paint(g, y, scrolled);
						y += docs[i].itemDrawHeight;
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
					int yy1 = y - (scrolled + 58);
					int yy2 = yy1 / (48 + (DocItem.BORDER * 2));
					if(yy2 < 0)
						yy2 = 0;
					int yy = 0;
					if(yy2 > 0)
						yy = yy1;
					for(int i = yy2; i < itemsCount; i++)
					{  
						int y1 = scrolled + 58 + yy;
						int y2 = y1 + docs[i].itemDrawHeight;
						yy += docs[i].itemDrawHeight;
						if(y > y1 && y < y2)
						{
							if(!dragging)
							{
								docs[i].tap(x, y1 - y);
							}
							break;
						}
						Thread.yield();
					}
				}
				break;
			}
				
		}
		
		super.pointerReleased(x, y);
	}

}
