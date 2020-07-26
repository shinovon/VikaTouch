package vikaTouch.newbase;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.canvas.MenuCanvas;
import vikaTouch.canvas.ScrollableCanvas;

public class DisplayUtils {
	
	public static String disp;
	public static int idispi;
	public static int width;
	public static int height;
	public static int lwidth;
	public static int lheight;
	

	public static Image resizeava(Image img) {
		int h = img.getHeight();
		int need = h;
		switch(idispi)
		{
			case 1:
			{
				need = 50;
				break;
			}
			
			case 2:
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
				disp = "portrait";
				i = 1;
				ScrollableCanvas.oneitemheight = 50;
				ScrollableCanvas.vmeshautsa = 528;
				if(VikaTouch.menu != null)
					VikaTouch.menu.itemsh = (VikaTouch.menu.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
			} 
			else if(width == 240)
			{
				if(height == 320)
				{
					disp = "standart34";
					i = 2;
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 265;
					if(VikaTouch.menu != null)
						VikaTouch.menu.itemsh = (VikaTouch.menu.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
				}
				else if(height == 400)
				{
					ScrollableCanvas.oneitemheight = 32;
					disp = "asha311";
					i = 3;
				}
			}
			else if(width == 320)
			{
				if(height == 240)
				{
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 185;
					disp = "eqwerty";
					i = 4;
				}
			}
			else if(width == 640)
			{
				if(height == 360)
				{
					disp = "album";
					i = 5;
					ScrollableCanvas.oneitemheight = 50;
					ScrollableCanvas.vmeshautsa = 248;
					if(VikaTouch.menu != null)
						VikaTouch.menu.itemsh = (VikaTouch.menu.itemsCount * ScrollableCanvas.oneitemheight) + ScrollableCanvas.oneitemheight;
				}
				else if(height == 480)
				{
					ScrollableCanvas.oneitemheight = 50;
					ScrollableCanvas.vmeshautsa = 368;
					disp = "e6";
					i = 6;
				}
			}
			else
				i = -1;
		}
		
		lwidth = width;
		lheight = height;
		if(i != 0)
			idispi = i;
		return i;
	}

}
