package vikaTouch.newbase;

import javax.microedition.lcdui.Graphics;

import vikaTouch.VikaTouch;

public abstract class VikaScreen
{

	public abstract void paint(Graphics g);
	
	public void pointerPressed(int x, int y)
	{
		
	}
	
	public void pointerReleased(int x, int y)
	{
		
	}
	
	public void pointerDragged(int x, int y)
	{
		
	}

	public void keyPressed(int i)
	{
		
	}

	public void keyReleased(int i)
	{
		
	}

	public void keyRepeated(int i)
	{
		
	}
	
	protected void repaint()
	{
		if(VikaTouch.canvas != null)
		{
			VikaTouch.canvas.paint();
		}
	}
	
}
