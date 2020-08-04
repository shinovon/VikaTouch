package ru.nnproject.vikaui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.game.GameCanvas;

import vikatouch.base.VikaTouch;

public class UIThread
	extends Thread
{

	public void run()
	{
		while(VikaTouch.appInst.started)
		{
			try
			{
				if(VikaTouch.loading)
					VikaTouch.canvas.updategif();
				else
				{
					VikaTouch.canvas.paint();
				}
			}
			catch (Exception e)
			{
				
			}
			Thread.yield();
		}
	}

}
