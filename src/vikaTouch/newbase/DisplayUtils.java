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
	public static final int CANVAS_LOGIN = 1;
	public static final int CANVAS_MENU = 2;
	public static final int CANVAS_NEWS = 3;
	public static final int CANVAS_CHATSLIST = 4;
	public static final int CANVAS_CHAT = 5;
	public static final int CANVAS_ABOUT = 6;
	public static final int CANVAS_SETTINGS = 7;
	public static final int CANVAS_TEMPLIST = 8;
	public static final int CANVAS_LOADING = 9;
	public static final int CANVAS_MUSICLIST = 10;

	//Экраны
	public static final int DISPLAY_PORTRAIT = 1;
	public static final int DISPLAY_S40 = 2;
	public static final int DISPLAY_ASHA311 = 3;
	public static final int DISPLAY_EQWERTY = 4;
	public static final int DISPLAY_ALBUM = 5;
	public static final int DISPLAY_E6 = 6;
	public static final int DISPLAY_UNDEFINED = -1;

	public static Image resizeava(Image img)
	{
		int h = img.getHeight();
		int need = h;
		switch(idispi)
		{
			case DISPLAY_E6:
			case DISPLAY_PORTRAIT:
			{
				need = 50;
				break;
			}
			
			case DISPLAY_S40:
			case DISPLAY_ASHA311:
			case DISPLAY_EQWERTY:
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
	

	public static int checkdisplay()
	{
		int i = 0; 
		width = VikaTouch.canvas.getWidth();
		height = VikaTouch.canvas.getHeight();
		if(width != lwidth || height != lheight)
		{
			if(width == 360 && height == 640)
			{
				i = DISPLAY_PORTRAIT;
				ScrollableCanvas.oneitemheight = 50;
				ScrollableCanvas.vmeshautsa = 528;
				if(VikaTouch.menuCanv != null)
					VikaTouch.menuCanv.itemsh = (VikaTouch.menuCanv.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
			} 
			else if(width == 240)
			{
				if(height == 320)
				{
					i = DISPLAY_S40;
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 265;
					if(VikaTouch.menuCanv != null)
						VikaTouch.menuCanv.itemsh = (VikaTouch.menuCanv.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
				}
				else if(height == 400)
				{
					ScrollableCanvas.oneitemheight = 32;
					i = DISPLAY_ASHA311;
				}
			}
			else if(width == 320)
			{
				if(height == 240)
				{
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 185;
					i = DISPLAY_EQWERTY;
				}
			}
			else if(width == 640)
			{
				if(height == 360)
				{
					i = DISPLAY_ALBUM;
					ScrollableCanvas.oneitemheight = 50;
					ScrollableCanvas.vmeshautsa = 248;
					if(VikaTouch.menuCanv != null)
						VikaTouch.menuCanv.itemsh = (VikaTouch.menuCanv.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
				}
				else if(height == 480)
				{
					ScrollableCanvas.oneitemheight = 50;
					ScrollableCanvas.vmeshautsa = 368;
					i = DISPLAY_E6;
				}
			}
			else
				i = DISPLAY_UNDEFINED;
		}
		
		lwidth = width;
		lheight = height;
		if(i != 0)
			idispi = i;
		return i;
	}


	public static String getCanvasString()
	{
		switch(current)
		{
			case CANVAS_LOGIN:
				return "Логин";
			case CANVAS_MENU:
				return "Меню";
			case CANVAS_NEWS:
				return "Лента";
			case CANVAS_CHATSLIST:
				return "Диалоги";
			case CANVAS_TEMPLIST:
				return "Список";
			case CANVAS_MUSICLIST:
				return "Музыка";
			default:
				return "default";
		}
	}


	public static Image resizeItemPreview(Image img)
	{
		int h = img.getHeight();
		int need = h;
		switch(idispi)
		{
			case DISPLAY_E6:
			case DISPLAY_PORTRAIT:
			case DISPLAY_ALBUM:
			{
				need = 48;
				break;
			}
			
			case DISPLAY_S40:
			case DISPLAY_ASHA311:
			case DISPLAY_EQWERTY:
			{
				need = 24;
				break;
			}

			case DISPLAY_UNDEFINED:
			default:
			{
				need = 48;
				break;
			}
		}
		if(h != need)
		{
			return VikaUtils.resize(img, need, need);
		}
		return img;
	}

}
