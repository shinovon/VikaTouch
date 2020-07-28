package vikaTouch.canvas;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.Dialogs;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.JSONBase;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.items.DialogItem;

public class DocsCanvas extends MainCanvas {
	
	public DocsCanvas () {
		super();
		VikaTouch.loading = true;
		if(VikaTouch.menu == null)
			VikaTouch.menu = new MenuCanvas();
		
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
		try {
			LoadDocs(0,20);
		} catch (Exception e) {
			e.printStackTrace();
			VikaTouch.error(e, "Загрузка списка документов");
		}
	}

	
	public final static int loadDocsCount = 10;
	public int fromDoc = 0;
	public int docsCount = 0;
	public int totalDocs = 0;
	public Image[] docsIcons = null;
	public String[] docsNames = null;
	public String[] docsUrls = null;
	public String[] docsPreview = null;
	public int[] docsSizes = null;
	public static Thread downloaderThread;
	
	public void LoadDocs(final int from, final int count) {
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
						totalDocs = response.getInt("count");
						JSONArray items = response.getJSONArray("items");
						itemsCount = items.length();
						System.out.println("Получено "+itemsCount+" документов");
						docsNames = new String[itemsCount];
						docsPreview = new String[itemsCount];
						docsUrls = new String[itemsCount];
						docsIcons = new Image[itemsCount];
						docsSizes = new int[itemsCount];
						System.gc(); // форс удаления старых массивов
						for(int i = 0; i < itemsCount; i++)
						{
							JSONObject item = items.getJSONObject(i);
							docsNames[i] = item.getString("title");
							docsUrls[i] = item.getString("url");
							docsSizes[i] = item.getInt("size");
							try {
								JSONArray previews = item.getJSONObject("preview").getJSONObject("photo").getJSONArray("sizes");
								boolean hasXPrew = false;
								for(int j = 0; j < previews.length(); j++) {
									String pt = previews.getJSONObject(j).getString("type"); // свитч не дал, типа строки не умеет
									System.out.println(i+" "+j+" "+pt);
									if(pt.equals("s")) {
										//System.out.println("Иконка есть! "+i+" "+j);
										docsIcons[i] = DisplayUtils.resizeava(VikaUtils.downloadImage(JSONBase.fixJSONString(previews.getJSONObject(j).getString("src"))));
									} else if(pt.equals("x")) {
										hasXPrew = true;
										docsPreview[i] = JSONBase.fixJSONString(previews.getJSONObject(j).getString("src"));
									} else if(pt.equals("o")) {
										if(!hasXPrew) {
											docsPreview[i] = JSONBase.fixJSONString(previews.getJSONObject(j).getString("src"));
										}
									}
								}
							}
							catch (JSONException e)
							{
								//System.out.println("Предпросмотр сожрали неко. "+i);
								//Предпросмотр не завезли - видимо не картинка. Ну и ладно.
							}
						}
						
					}
					catch (JSONException e)
					{
						e.printStackTrace(); VikaTouch.error(e, "Парс списка документов");
					}

					VikaTouch.loading = false;
				}
				catch (NullPointerException e)
				{
					VikaTouch.warn("Переход в оффлайн режим!");
					VikaTouch.offlineMode = true;
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace(); VikaTouch.error(e, "Загрузка списка документов");
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
			if(docsNames!=null&&docsSizes!=null&&docsIcons!=null) {
				try
				{
					for(int i = 0; i < itemsCount; i++)
					{
						ColorUtils.setcolor(g, 0);
						if(docsNames[i]!=null)
							g.drawString(docsNames[i], 73, y + 16, 0);
						ColorUtils.setcolor(g, 6);
						g.drawString(docsSizes[i]/1000 + "кб", 73, y + 40, 0);
						if(docsIcons[i] != null)
						{
							g.drawImage(docsIcons[i], 14, y + 8, 0);
						}
						 
						y += 50;
						
					}
				}
				catch (Exception e)
				{
					VikaTouch.error(e, "Прорисовка объектов: Доки");
				}
			
			}
			g.translate(0, -g.getTranslateY());
			
			drawHeaders(g, "Документы");
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: Доки");
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
					int yy1 = y - scrolled - 50;
					int yy2 = yy1 / 63;
					if(yy2 < 0)
						yy2 = 0;
					int yy = 0;
					for(int i = yy2; i < Dialogs.itemsCount; i++)
					{
						int y1 = scrolled + 50 + yy;
						int y2 = y1 + Dialogs.dialogs[i].itemDrawHeight;
						yy += Dialogs.dialogs[i].itemDrawHeight;
						if(y > y1 && y < y2)
						{
							//DocsCanvas.class.unselectAll();
							if(!dragging)
							{
								Dialogs.dialogs[i].tap(x, y1 - y);
							}
							Dialogs.dialogs[i].released(dragging);
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
	
	public final void pointerPressed(int x, int y)
	{
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_ALBUM:
			case DisplayUtils.DISPLAY_PORTRAIT:
			{
				if(y > 58 && y < DisplayUtils.height - oneitemheight)
				{
					int yy1 = y - scrolled - 50;
					int yy2 = yy1 / 63; 
					if(yy2 < 0)
						yy2 = 0;
					int yy = 0;
					for(int i = yy2; i < Dialogs.itemsCount; i++)
					{
						int y1 = scrolled + 50 + yy;
						int y2 = y1 + Dialogs.dialogs[i].itemDrawHeight;
						yy += Dialogs.dialogs[i].itemDrawHeight;
						if(y > y1 && y < y2)
						{
							//unselectAll();
							Dialogs.dialogs[i].pressed();
							break;
						}
						Thread.yield();
					}
				}
				break;
			}
				
		}
		repaint();
		super.pointerPressed(x, y);
	}

}
