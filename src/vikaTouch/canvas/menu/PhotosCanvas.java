package vikaTouch.canvas.menu;

import javax.microedition.lcdui.Graphics;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.MainCanvas;

public class PhotosCanvas
	extends MainCanvas
{

	public PhotosCanvas()
	{
		this.menuImg = MenuCanvas.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
	}
	
	public void paint(Graphics g)
	{
		drawHeaders(g, "Фотографии");
	}

}
