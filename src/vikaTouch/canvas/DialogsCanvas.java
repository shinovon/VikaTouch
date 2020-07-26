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
				case DisplayUtils.DISPLAY_PORTRAIT:
				case DisplayUtils.DISPLAY_ALBUM:
				case DisplayUtils.DISPLAY_E6:
				{
					if(menuImg == null)
					{
						menuImg = Image.createImage("/menu.png");
					}
					if(newsImg == null)
					{
						newsImg = Image.createImage("/lenta.png");
					}
					break;
				}
				case DisplayUtils.DISPLAY_S40:
				case DisplayUtils.DISPLAY_ASHA311:
				case DisplayUtils.DISPLAY_EQWERTY:
				{
					if(menuImg == null)
					{
						menuImg = VikaUtils.resize(Image.createImage("/menu.png"), 10, 9);
					}
					if(newsImg == null)
					{
						newsImg = VikaUtils.resize(Image.createImage("/lenta.png"), 11, 12);
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
		
		ColorUtils.setcolor(g, -1);
		g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
		ColorUtils.setcolor(g, 0);
		g.setFont(Font.getFont(0, 0, 8));
		itemsh = Dialogs.itemsCount * 63;
		DisplayUtils.checkdisplay(this);
		double multiplier = (double)DisplayUtils.height / 640.0;
		double ww = 10.0 * multiplier;
		int w = (int)ww;
		try
		{
			//g.drawString(string, 8, 8, 0);
			
			update(g);
			int y = oneitemheight + w;
			try
			{
				for(int i = 0; i < Dialogs.itemsCount; i++)
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
			g.translate(0, -g.getTranslateY());
			
			drawHeaders(g, "Сообщения");
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: Диалоги");
			e.printStackTrace();
		}
	}
	
	public void unselectAll()
	{
		try
		{
			VikaTouch.mainThread.join();
		}
		catch (InterruptedException e)
		{
			
		}
		for(int i = 0; i < Dialogs.itemsCount; i++)
		{
			if(Dialogs.dialogs[i] != null)
			{
				Dialogs.dialogs[i].selected = false;
			}
		}
	}
	
	protected final void pointerReleased(int x, int y)
	{
		try
		{
			VikaTouch.mainThread.join();
		}
		catch (InterruptedException e)
		{
			
		}
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_ALBUM:
			case DisplayUtils.DISPLAY_PORTRAIT:
			{
				if(y > 58 && y < DisplayUtils.height - oneitemheight)
				{
					int yy = 0;
					for(int i = 0; i < Dialogs.itemsCount; i++)
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
		try
		{
			VikaTouch.mainThread.join();
		}
		catch (InterruptedException e)
		{
			
		}
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_ALBUM:
			case DisplayUtils.DISPLAY_PORTRAIT:
			{
				if(y > 58 && y < DisplayUtils.height - oneitemheight)
				{
					int yy = 0;
					for(int i = 0; i < Dialogs.itemsCount; i++)
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
