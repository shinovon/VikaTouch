package vikatouch.base;

import java.io.InputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.ConfirmBox;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.GifDecoder;
import ru.nnproject.vikaui.VikaCanvas;
import ru.nnproject.vikaui.VikaNotice;
import ru.nnproject.vikaui.VikaScreen;

public class VikaCanvasInst
extends VikaCanvas
{
	public VikaScreen currentScreen;
	public VikaScreen lastTempScreen;
	public boolean showCaptcha;
	private Image frame;
	private GifDecoder d;
	public VikaNotice currentAlert;

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
			VikaTouch.error(e, ErrorCodes.VIKACANVASPAINT);
		}
	}
	
	public void updateScreen(Graphics g)
	{
		DisplayUtils.checkdisplay();
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
		
		if(currentScreen != null && !VikaTouch.crashed)
		{
			currentScreen.draw(g);
		}
		
		if(showCaptcha && !VikaTouch.crashed)
		{
			VikaTouch.captchaScr.draw(g);
		}
		
		if(currentAlert != null)
		{
			currentAlert.draw(g);
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
		if(currentAlert != null)
		{
			currentAlert.press(x, y);
		}
		else if(showCaptcha)
		{
			VikaTouch.captchaScr.press(x, y);
		}
		else if(currentScreen != null)
		{
			currentScreen.press(x, y);
		}
	}
	
	public void pointerReleased(int x, int y)
	{
		if(currentAlert != null)
		{
			currentAlert.release(x, y);
		}
		else if(showCaptcha)
		{
			VikaTouch.captchaScr.release(x, y);
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
		if(currentAlert != null)
		{
			currentAlert.press(i);
		}
		else if(currentScreen != null)
		{
			currentScreen.press(i);
		}
	}
	
	public void keyRepeated(int i)
	{
		if(currentAlert != null)
		{
			currentAlert.repeat(i);
		}
		else if(currentScreen != null)
		{
			currentScreen.repeat(i);
		}
	}
	
	public void keyReleased(int i)
	{
		if(currentAlert != null)
		{
			currentAlert.release(i);
		}
		else if(currentScreen != null)
		{
			currentScreen.release(i);
		}
	}

	public void paint()
	{
		if(!VikaTouch.appInst.isPaused && (!VikaTouch.crashed || currentAlert != null))
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
