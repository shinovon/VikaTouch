package vikatouch.screens.menu;

import javax.microedition.lcdui.Graphics;

import vikatouch.VikaTouch;
import vikatouch.local.TextLocal;
import vikatouch.screens.MainScreen;

public class PhotosScreen
	extends MainScreen
{

	private String photosStr;

	public PhotosScreen()
	{
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuScr.newsImg;
		photosStr = TextLocal.inst.get("title.photos");
	}
	
	public void draw(Graphics g)
	{
		drawHUD(g, photosStr);
	}

}
