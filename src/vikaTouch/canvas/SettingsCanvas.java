package vikaTouch.canvas;

import javax.microedition.lcdui.Graphics;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.menu.MenuCanvas;

public class SettingsCanvas
	extends MainCanvas
{
	
	public SettingsCanvas()
	{
		this.menuImg = MenuCanvas.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}

	public void draw(Graphics g)
	{
		drawHeaders(g, "Настройки");
	}

}
