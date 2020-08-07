package vikatouch.screens;

import javax.microedition.lcdui.Graphics;

import vikatouch.base.VikaTouch;
import vikatouch.screens.menu.MenuScreen;

public class SettingsScreen
	extends MainScreen
{
	
	public SettingsScreen()
	{
		super();
	}

	public void draw(Graphics g)
	{
		if(VikaTouch.menuScr != null && newsImg == null)
		{
			this.menuImg = MenuScreen.menuImg;
			this.newsImg = VikaTouch.menuScr.newsImg;
		}
		drawHUD(g, "Настройки");
	}

}
