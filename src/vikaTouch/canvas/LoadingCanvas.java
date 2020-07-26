package vikaTouch.canvas;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import vikaTouch.VikaTouch;
import vikaTouch.base.GifDecoder;
import vikaTouch.newbase.DisplayUtils;

public class LoadingCanvas
	extends Canvas
{
	
	private GifDecoder gifDecoder;
	private Thread animationThread;
	protected Image frame;
	public static Canvas parent;
	
	public LoadingCanvas()
	{
		//gifDecoder = new GifDecoder();
		//gifDecoder.read(getClass().getResourceAsStream("/loading.gif"));
		startAnimationThread();
	}

	public void paint(Graphics g)
	{
		//если есть фоновой канвас то рисовать его
		if(parent != null)
		{
			parent.repaint();
		}
		//если же его нету то рисовать фон
		else
		{
			g.setColor(0xffffff);
			g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
		}
		g.setColor(0);
		g.drawString("Загрузка", DisplayUtils.width / 2, DisplayUtils.height - 72, Graphics.TOP | Graphics.HCENTER);
		if(frame != null)
		{
			g.drawImage(frame, (DisplayUtils.width - frame.getWidth() ) / 2, DisplayUtils.height - 64, 0);
		}
	}
	
	public void startAnimationThread()
	{
		/*
		animationThread = new Thread("Animation Thread")
		{
			public void run()
			{
				while(VikaTouch.inst.started && !VikaTouch.inst.isPaused)
				{
					if(VikaTouch.loadingAnimation)
					{
						int n = gifDecoder.getFrameCount();
						for (int i = 0; i < n; i++)
						{
							frame = gifDecoder.getFrame(i);
							repaint();
							
							int t = gifDecoder.getDelay(i);
							
							try
							{
								Thread.sleep(t);
							}
							catch (InterruptedException e)
							{
								
							}
						}
					}
				}
			}
		};
		
		animationThread.start();
		*/
	}

}
