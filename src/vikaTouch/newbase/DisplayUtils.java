package vikaTouch.newbase;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.canvas.MenuCanvas;
import vikaTouch.canvas.ScrollableCanvas;

public class DisplayUtils
{
	public static int idispi;
	public static int width;
	public static int height;
	public static int lwidth;
	public static int lheight;
	public static int current;
	
	//Дисплеи
	public static final int LOGIN = 1;
	public static final int MENU = 2;
	public static final int NEWS = 3;
	public static final int CHATSLIST = 4;
	public static final int CHAT = 5;
	public static final int ABOUT = 6;
	public static final int SETTINGS = 7;
	public static final int TEMPLIST = 8;

	//Экраны
	public static final int PORTRAIT = 1;
	public static final int S40 = 2;
	public static final int ASHA311 = 3;
	public static final int EQWERTY = 4;
	public static final int ALBUM = 5;
	public static final int E6 = 6;
	public static final int UNDEFINED = -6;

	public static Image resizeava(Image img)
	{
		int h = img.getHeight();
		int need = h;
		switch(idispi)
		{
			case E6:
			case PORTRAIT:
			{
				need = 50;
				break;
			}
			
			case S40:
			case ASHA311:
			case EQWERTY:
			{
				need = 25;
				break;
			}
			
			default:
			{
				need = 50;
				break;
			}
		}
		if(h != need)
		{
			return VikaUtils.resize(img, need, need);
		}
		return img;
	}
	

	public static int checkdisplay(Canvas c)
	{
		int i = 0; 
		width = c.getWidth();
		height = c.getHeight();
		if(width != lwidth || height != lheight)
		{
			if(width == 360 && height == 640)
			{
				i = PORTRAIT;
				ScrollableCanvas.oneitemheight = 50;
				ScrollableCanvas.vmeshautsa = 528;
				if(VikaTouch.menu != null)
					VikaTouch.menu.itemsh = (VikaTouch.menu.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
			} 
			else if(width == 240)
			{
				if(height == 320)
				{
					i = S40;
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 265;
					if(VikaTouch.menu != null)
						VikaTouch.menu.itemsh = (VikaTouch.menu.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
				}
				else if(height == 400)
				{
					ScrollableCanvas.oneitemheight = 32;
					i = ASHA311;
				}
			}
			else if(width == 320)
			{
				if(height == 240)
				{
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 185;
					i = EQWERTY;
				}
			}
			else if(width == 640)
			{
				if(height == 360)
				{
					i = ALBUM;
					ScrollableCanvas.oneitemheight = 50;
					ScrollableCanvas.vmeshautsa = 248;
					if(VikaTouch.menu != null)
						VikaTouch.menu.itemsh = (VikaTouch.menu.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
				}
				else if(height == 480)
				{
					ScrollableCanvas.oneitemheight = 50;
					ScrollableCanvas.vmeshautsa = 368;
					i = E6;
				}
			}
			else
				i = UNDEFINED;
		}
		
		lwidth = width;
		lheight = height;
		if(i != 0)
			idispi = i;
		return i;
	}

}
