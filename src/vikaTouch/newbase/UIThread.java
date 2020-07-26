package vikaTouch.newbase;

import javax.microedition.lcdui.Canvas;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.LoadingCanvas;

public class UIThread
	implements Runnable
{

	public void run()
	{
		if(VikaTouch.loading == null)
		{
			VikaTouch.loading = new LoadingCanvas();
		}
		
		while(VikaTouch.inst != null && VikaTouch.inst.started && !VikaTouch.inst.isPaused)
		{
			if(VikaTouch.loadingAnimation && !(VikaTouch.getCurrentDisplay() instanceof LoadingCanvas))
			{
				VikaTouch.setDisplay(VikaTouch.loading);
			}
			try
			{
				((Canvas) VikaTouch.getCurrentDisplay()).repaint();
			}
			catch (Exception e)
			{
				
			}
		}
	}

}
