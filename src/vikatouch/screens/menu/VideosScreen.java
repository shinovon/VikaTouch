package vikatouch.screens.menu;

import javax.microedition.lcdui.Graphics;

import vikatouch.base.VikaTouch;
import vikatouch.screens.MainScreen;

public class VideosScreen
	extends MainScreen
{

	public VideosScreen()
	{
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}
	
	public void draw(Graphics g)
	{
		drawHeaders(g, "Видео");
	}

}