package vikatouch.screens;

import javax.microedition.lcdui.Graphics;

import vikatouch.base.VikaTouch;
import vikatouch.screens.menu.MenuScreen;

public class SettingsScreen
	extends MainScreen
{
	
	public SettingsScreen()
	{
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}

	public void draw(Graphics g)
	{
		drawHeaders(g, "Настройки");
	}

}
