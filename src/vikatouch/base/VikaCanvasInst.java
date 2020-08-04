package vikatouch.base;

import java.io.InputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.GifDecoder;
import ru.nnproject.vikaui.VikaCanvas;
import ru.nnproject.vikaui.VikaScreen;

public class VikaCanvasInst
extends VikaCanvas
{
	public VikaScreen currentScreen;
	public VikaScreen lastTempScreen;
	public boolean showCaptcha;
	private Image frame;
	private GifDecoder d;

	public VikaCanvasInst()
	{
		super();
		this.setFullScreenMode(true);

		DisplayUtils.canvas = this;
		
		try
		{
			final InputStream in = this.getClass().getResourceAsStream("/loading.gif");
			d = new GifDecoder();
			int err = d.read(in);
	        if (err == 0)
	        {
	           frame = d.getImage();
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g)
	{
		try
		{
			this.updateScreen(g);
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Прорисовка: " + DisplayUtils.getCanvasString());
		}
	}
	
	public void updateScreen(Graphics g)
	{
		DisplayUtils.checkdisplay();
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
		
		if(currentScreen != null)
		{
			currentScreen.draw(g);
		}
		
		if(showCaptcha)
		{
			VikaTouch.captchaCanv.draw(g);
		}
		
		if(VikaTouch.loading)
		{
			drawLoading(g);
		}
		if(Settings.debugInfo)
		{
			if(debugString != null)
			{
				g.setColor(0xffff00);
				g.drawString(debugString, 65, 2, 0);
			}
		}
	}
	
	private void drawLoading(Graphics g)
	{
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawString("Загрузка", DisplayUtils.width / 2, DisplayUtils.height - 80, Graphics.TOP | Graphics.HCENTER);
		
		if(frame != null)
		{
			g.drawImage(frame, DisplayUtils.width / 2, DisplayUtils.height - 128, Graphics.TOP | Graphics.HCENTER);
		}
		

		if(Settings.debugInfo)
		{
			g.setColor(0x00ffff);
			g.drawString("", 65, 2, 0);
		}
	}
	
	public void updategif()
	{
        int n = d.getFrameCount();
        for (int i = 0; i < n; i++)
        {
            frame = d.getFrame(i);
            repaint();
            try
            {
            	Thread.sleep(40);
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            }
        }
    }

	public void pointerPressed(int x, int y)
	{
		if(showCaptcha)
		{
			VikaTouch.captchaCanv.press(x, y);
		}
		else if(currentScreen != null)
		{
			currentScreen.press(x, y);
		}
	}
	
	public void pointerReleased(int x, int y)
	{
		if(showCaptcha)
		{
			VikaTouch.captchaCanv.release(x, y);
		}
		else if(currentScreen != null)
		{
			currentScreen.release(x, y);
		}
	}
	
	public void pointerDragged(int x, int y)
	{
		if(currentScreen != null)
		{
			currentScreen.drag(x, y);
		}
	}
	
	public void keyPressed(int i)
	{
		if(currentScreen != null)
		{
			currentScreen.press(i);
		}
	}
	
	public void keyRepeated(int i)
	{
		if(currentScreen != null)
		{
			currentScreen.repeat(i);
		}
	}
	
	public void keyReleased(int i)
	{
		if(currentScreen != null)
		{
			currentScreen.release(i);
		}
	}

	public void paint()
	{
		if(!VikaTouch.appInst.isPaused)
		{
			repaint();
			//serviceRepaints();
		}
	}

	public void tick() 
	{
		if(VikaTouch.loading)
		{
			updategif();
		}
		else
		{
			paint();
		}
	}

	protected void callCommand(int i, VikaScreen screen)
	{
		VikaTouch.inst.cmdsInst.command(i, screen);
	}

	protected boolean isSensorModeOK()
	{
		return Settings.sensorMode == Settings.SENSOR_OK;
	}

	protected boolean isSensorModeJ2MELoader()
	{
		return Settings.sensorMode == Settings.SENSOR_J2MELOADER;
	}

}
