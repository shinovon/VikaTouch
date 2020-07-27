package vikaTouch.newbase;

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
				VikaTouch.canvas.paint();
				Thread.sleep(100L);
			}
			catch (Exception e)
			{
				
			}
			Thread.yield();
		}
	}

}
