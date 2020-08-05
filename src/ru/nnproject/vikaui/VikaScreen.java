package ru.nnproject.vikaui;

import javax.microedition.lcdui.Graphics;

public abstract class VikaScreen
{
	public abstract void draw(Graphics g);
	
	public void press(int x, int y)
	{
		
	}
	
	public void release(int x, int y)
	{
		
	}
	
	public void drag(int x, int y)
	{
		
	}

	public void press(int i)
	{
		
	}

	public void release(int i)
	{
		
	}

	public void repeat(int i)
	{
		
	}
	
	protected void repaint()
	{
		if(DisplayUtils.canvas != null)
		{
			DisplayUtils.canvas.paint();
		}
	}
}