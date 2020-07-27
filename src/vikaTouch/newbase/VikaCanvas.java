package vikaTouch.newbase;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import vikaTouch.VikaTouch;

public class VikaCanvas
	extends GameCanvas
{
	public VikaScreen currentScreen;
	public boolean showCaptcha;

	public VikaCanvas()
	{
		super(false);
		this.setFullScreenMode(true);
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
			currentScreen.paint(g);
		}
		
		if(showCaptcha)
		{
			VikaTouch.captcha.paint(g);
		}
		
		if(VikaTouch.loadingAnimation)
		{
			drawLoading(g);
		}
	}
	
	private void drawLoading(Graphics g)
	{
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawString("Загрузка", DisplayUtils.width / 2, DisplayUtils.height - 64, Graphics.TOP | Graphics.HCENTER);
	}

	public void pointerPressed(int x, int y)
	{
		if(showCaptcha)
		{
			VikaTouch.captcha.pointerPressed(x, y);
		}
		else if(currentScreen != null)
		{
			currentScreen.pointerPressed(x, y);
		}
	}
	
	public void pointerReleased(int x, int y)
	{
		if(showCaptcha)
		{
			VikaTouch.captcha.pointerReleased(x, y);
		}
		else if(currentScreen != null)
		{
			currentScreen.pointerReleased(x, y);
		}
	}
	
	public void pointerDragged(int x, int y)
	{
		if(currentScreen != null)
		{
			currentScreen.pointerDragged(x, y);
		}
	}
	
	public void keyPressed(int i)
	{
		if(currentScreen != null)
		{
			currentScreen.keyPressed(i);
		}
	}
	
	public void keyRepeated(int i)
	{
		if(currentScreen != null)
		{
			currentScreen.keyRepeated(i);
		}
	}
	
	public void keyReleased(int i)
	{
		if(currentScreen != null)
		{
			currentScreen.keyReleased(i);
		}
	}

	public void paint()
	{
		if(!VikaTouch.inst.isPaused)
		{
			repaint();
			//serviceRepaints();
		}
	}

}
