package ru.nnproject.vikaui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;

public class DisplayUtils
{
	public static int idispi;
	public static int width;
	public static int height;
	public static int lwidth;
	public static int lheight;
	public static int current;
	public static VikaCanvas canvas;
	
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
	public static final int CANVAS_DOCSLIST = 11;
	public static final int CANVAS_VIDEOSLIST = 12;
	public static final int CANVAS_FRIENDSLIST = 13;
	public static final int CANVAS_GROUPSLIST = 14;
	public static final int CANVAS_PHOTOSLIST = 15;

	//Экраны
	public static final int DISPLAY_PORTRAIT = 1;
	public static final int DISPLAY_S40 = 2;
	public static final int DISPLAY_ASHA311 = 3;
	public static final int DISPLAY_EQWERTY = 4;
	public static final int DISPLAY_ALBUM = 5;
	public static final int DISPLAY_E6 = 6;
	public static final int DISPLAY_UNDEFINED = -1;

	public static int checkdisplay()
	{
		int i = 0; 
		if(canvas == null)
			return 0;
		width = canvas.getWidth();
		height = canvas.getHeight();
		if(width != lwidth || height != lheight)
		{
			if(width == 360 && height == 640)
			{
				i = DISPLAY_PORTRAIT;
				ScrollableCanvas.oneitemheight = 50;
				ScrollableCanvas.vmeshautsa = 528;
			} 
			else if(width == 240)
			{
				if(height == 320)
				{
					i = DISPLAY_S40;
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 265;
				}
				else if(height == 400)
				{
					i = DISPLAY_ASHA311;
					ScrollableCanvas.oneitemheight = 25;
				}
			}
			else if(width == 320)
			{
				if(height == 240)
				{
					i = DISPLAY_EQWERTY;
					ScrollableCanvas.oneitemheight = 25;
					ScrollableCanvas.vmeshautsa = 185;
				}
			}
			else if(width == 640)
			{
				if(height == 360)
				{
					i = DISPLAY_ALBUM;
					ScrollableCanvas.oneitemheight = 50;
					ScrollableCanvas.vmeshautsa = 248;
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

}
