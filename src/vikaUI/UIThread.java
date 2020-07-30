package vikaUI;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.GameCanvas;

import vikaTouch.VikaTouch;

public class UIThread
	extends Thread
{

	public void run()
	{
		while(VikaTouch.inst.started)
		{
			try
			{
				if(VikaTouch.loading)
					VikaTouch.canvas.updategif();
				VikaTouch.canvas.paint();
			}
			catch (Exception e)
			{
				
			}
			Thread.yield();
		}
	}

}
