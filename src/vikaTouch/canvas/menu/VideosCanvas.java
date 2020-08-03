package vikaTouch.canvas.menu;

import javax.microedition.lcdui.Graphics;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.MainCanvas;

public class VideosCanvas
	extends MainCanvas
{

	public VideosCanvas()
	{
		this.menuImg = MenuCanvas.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}
	
	public void draw(Graphics g)
	{
		drawHeaders(g, "Видео");
	}

}
