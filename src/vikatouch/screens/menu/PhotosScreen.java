package vikatouch.screens.menu;

import javax.microedition.lcdui.Graphics;

import vikatouch.base.VikaTouch;
import vikatouch.screens.MainScreen;

public class PhotosScreen
	extends MainScreen
{

	public PhotosScreen()
	{
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}
	
	public void draw(Graphics g)
	{
		drawHUD(g, "Фотографии");
	}

}
