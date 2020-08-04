package ru.nnproject.vikaui;

public class UIThread
	extends Thread
{
	
	private VikaCanvas canvas;

	public UIThread(VikaCanvas canvas)
	{
		super();
		this.canvas = canvas;
	}

	public void run()
	{
		while(true)
		{
			try
			{
				canvas.tick();
			}
			catch (Exception e)
			{
				
			}
			Thread.yield();
		}
	}

}
