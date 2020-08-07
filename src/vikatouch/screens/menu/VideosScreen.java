package vikatouch.screens.menu;

import javax.microedition.lcdui.Graphics;

import vikatouch.base.VikaTouch;
import vikatouch.base.local.TextLocal;
import vikatouch.screens.MainScreen;

public class VideosScreen
	extends MainScreen
{

	private String videosStr;

	public VideosScreen()
	{
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuScr.newsImg;
		videosStr = TextLocal.inst.get("title.videos");
	}
	
	public void draw(Graphics g)
	{
		drawHUD(g, videosStr);
	}
	
	public void load(int from, int id, String name) 
	{
		
	}

}
