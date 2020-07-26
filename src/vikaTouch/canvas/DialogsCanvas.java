package vikaTouch.canvas;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.Dialogs;
import vikaTouch.newbase.DisplayUtils;

public class DialogsCanvas
	extends MainCanvas
	{
	
	private Image menuImg;

	public DialogsCanvas()
	{
		super();
		DisplayUtils.checkdisplay(this);
		
		if(VikaTouch.menu == null)
		{
			VikaTouch.menu = new MenuCanvas();
		}
		
		try
		{
			switch(DisplayUtils.idispi)
			{
				case 1:
				case 5:
				case 6:
				{
					if(menuImg == null)
					{
						menuImg = Image.createImage("/menu.png");
					}
					break;
				}
				case 2:
				case 3:
				case 4:
				{
					if(menuImg == null)
					{
						menuImg = VikaUtils.resize(Image.createImage("/menu.png"), 10, 9);
					}
					break;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected final void callRefresh()
	{
		Dialogs.refreshDialogsList();
	}

	public void paint(Graphics g)
	{
		DisplayUtils.checkdisplay(this);
		double multiplier = (double)DisplayUtils.height / 640.0;
		double ww = 10.0 * multiplier;
		int w = (int)ww;
		try
		{
			DisplayUtils.checkdisplay(this);
			
			ColorUtils.setcolor(g, -1);
			g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
			ColorUtils.setcolor(g, 0);
			g.setFont(Font.getFont(0, 0, 8));
			//g.drawString(string, 8, 8, 0);
			
			update(g);
			int y = oneitemheight + w;
			try
			{
				for(int i = 0; i < itemsCount; i++)
				{
					if(Dialogs.dialogs[i] != null)
					{
						Dialogs.dialogs[i].paint(g, y, scrolled);
						y += Dialogs.dialogs[i].itemDrawHeight;
					}
				}
			}
			catch (Exception e)
			{
				VikaTouch.error(e, "Прорисовка объектов: Диалоги");
			}
			g.translate(0,-g.getTranslateY());
			
			switch(DisplayUtils.idispi)
			{
				case 1:
				{

					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, 360, 58);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, 590, 360, 50);

					if(menuImg != null)
					{
						g.drawImage(menuImg, 304, 606, 0);
					}
					if(MenuCanvas.logoImg != null)
					{
						g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
					}
					if(MenuCanvas.newsImg != null)
					{
						g.drawImage(MenuCanvas.newsImg, 37, 604, 0);
					}
					if(VikaTouch.has > 0)
					{

						if(MenuCanvas.dialImg2 != null)
						{
							g.drawImage(MenuCanvas.dialImg2, 168, 599, 0);
							g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
							g.drawString(""+VikaTouch.has, 191, 598, 0);
						}
						else if(MenuCanvas.dialImg != null)
						{
							g.drawImage(MenuCanvas.dialImg, 168, 604, 0);
						}

					}
					else
					{
						if(MenuCanvas.dialImg != null)
						{
							g.drawImage(MenuCanvas.dialImg, 168, 604, 0);
						}
					}
					break;
				}
				case 2:
				{
					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, 240, 30);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, 295, 240, 25);
					
					if(MenuCanvas.logoImg != null)
					{
						g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
					}
					
					if(menuImg != null)
					{
						g.drawImage(menuImg, 212, 303, 0);
					}
					
					if(MenuCanvas.newsImg != null)
					{
						g.drawImage(MenuCanvas.newsImg, 18, 301, 0);
					}
					
					if(VikaTouch.has > 0)
					{
						if(MenuCanvas.dialImg2 != null)
						{
							g.drawImage(MenuCanvas.dialImg2, 114, 299, 0);
							g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
							g.drawString(""+VikaTouch.has, 126, 300, 0);
						}
						else if(MenuCanvas.dialImg != null)
						{
							g.drawImage(MenuCanvas.dialImg, 114, 302, 0);
						}

					}
					else
					{
						if(MenuCanvas.dialImg != null)
						{
							g.drawImage(MenuCanvas.dialImg, 114, 302, 0);
						}
					}
					break;
				}

				case 5:
				{
					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, 640, 58);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, 310, 640, 50);

					if(MenuCanvas.logoImg != null)
					{
						g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
					}
					if(MenuCanvas.newsImg != null)
					{
						g.drawImage(MenuCanvas.newsImg, 36, 324, 0);
					}
					if(VikaTouch.has > 0)
					{

						if(MenuCanvas.dialImg2 != null)
						{
							g.drawImage(MenuCanvas.dialImg2, 308, 319, 0);
							g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
							g.drawString(""+VikaTouch.has, 330, 318, 0);
						}
						else if(MenuCanvas.dialImg != null)
						{
							g.drawImage(MenuCanvas.dialImg, 308, 324, 0);
						}

					}
					else
					{
						if(MenuCanvas.dialImg != null)
						{
							g.drawImage(MenuCanvas.dialImg, 308, 324, 0);
						}
					}
					if(menuImg != null)
					{
						g.drawImage(menuImg, 584, 326, 0);
					}
					break;
				}
				default:
				{
					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, DisplayUtils.width, oneitemheight+w);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, DisplayUtils.height - oneitemheight, DisplayUtils.width, oneitemheight);
					
					if(MenuCanvas.logoImg != null)
					{
						g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
					}
				}
			}
		} catch (Exception e) {
			VikaTouch.error(e, "Прорисовка: Диалоги");
			e.printStackTrace();
		}
	}
	
	public void unselectAll()
	{
		for(int i = 0; i < itemsCount; i++)
		{
			if(Dialogs.dialogs[i] != null)
			{
				Dialogs.dialogs[i].selected = false;
			}
		}
	}
	
	protected final void pointerReleased(int x, int y)
	{
		switch(DisplayUtils.idispi)
		{
			case 5:
			case 1:
			{
				if(y > 58 && y < DisplayUtils.height - oneitemheight)
				{
					int yy = 0;
					for(int i = 0; i < itemsCount; i++)
					{
						int y1 = scrolled + 50 + yy;
						int y2 = y1 + Dialogs.dialogs[i].itemDrawHeight;
						yy += Dialogs.dialogs[i].itemDrawHeight;
						if(y > y1 && y < y2)
						{
							unselectAll();
							if(!dragging)
							{
								Dialogs.dialogs[i].tap(x, y1 - y);
							}
							Dialogs.dialogs[i].released(dragging);
							break;
						}
							
					}
				}
				break;
			}
				
		}
		
		super.pointerReleased(x, y);
	}
	
	protected final void pointerPressed(int x, int y)
	{
		switch(DisplayUtils.idispi)
		{
			case 5:
			case 1:
			{
				if(y > 58 && y < DisplayUtils.height - oneitemheight)
				{
					int yy = 0;
					for(int i = 0; i < itemsCount; i++)
					{
						int y1 = scrolled + 50 + yy;
						int y2 = y1 + Dialogs.dialogs[i].itemDrawHeight;
						yy += Dialogs.dialogs[i].itemDrawHeight;
						unselectAll();
						if(y > y1 && y < y2)
						{
							Dialogs.dialogs[i].pressed();
							break;
						}
							
					}
				}
				break;
			}
				
		}
		
		super.pointerPressed(x, y);
	}

	protected void scrollHorizontally(int deltaX)
	{
		
	}

}
