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
			try
			{
				((Canvas) VikaTouch.getCurrentDisplay()).repaint();
				Thread.sleep(100L);
			}
			catch (Exception e)
			{
				
			}
			if(VikaTouch.loadingAnimation && !(VikaTouch.getCurrentDisplay() instanceof LoadingCanvas))
			{
				VikaTouch.setDisplay(VikaTouch.loading);
				VikaTouch.loading.repaint();
			}
		}
	}

}
