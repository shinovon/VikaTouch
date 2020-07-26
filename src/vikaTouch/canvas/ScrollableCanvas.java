package vikaTouch.canvas;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import vikaTouch.VikaTouch;
import vikaTouch.newbase.DisplayUtils;

public abstract class ScrollableCanvas extends GameCanvas {
	
	protected int startx;
	protected int starty;
	protected int endx;
	protected int endy;
	protected int scroll;
	protected int scrolled;
	protected int lasty;
	protected boolean dragging;
	protected boolean canScroll;
	public static int oneitemheight = 50;
	public int itemsCount = 5;
	public int itemsh = itemsCount * oneitemheight;
	private int lastx;
	public static int vmeshautsa = 528;
	public static final double speed = 2.1;
	
	public ScrollableCanvas()
	{
		super(false);
		this.setFullScreenMode(true);
		DisplayUtils.checkdisplay(this);
	}

	public abstract void paint(Graphics g);

	protected final void pointerDragged(int x, int y)
	{
		if(!dragging)
			lasty = starty;
		final int deltaX = lastx - x;
		final int deltaY = lasty - y;
		final int ndeltaX = Math.abs(deltaX);
		final int ndeltaY = Math.abs(deltaY);
		if(canScroll)
		{
			if(ndeltaY > ndeltaX)
			{
				scroll = (int)((double) -deltaY * speed);
			}
			else
			{
				scrollHorizontally(deltaX);
			}
		}
		if(ndeltaY > 0 || ndeltaX > 0)
		{
			dragging = true;
		}
		repaint();
		lastx = x;
		lasty = y;
	}

	protected abstract void scrollHorizontally(int deltaX);

	protected void pointerPressed(int x, int y)
	{
		startx = x;
		starty = y;
	}

	protected void pointerReleased(int x, int y)
	{
		endx = x;
		endy = y;
		dragging = false;
		repaint();
	}
	
	protected void keyPressed(int key)
	{
		if(key == -1)
		{
			scroll += 25;
		}
		if(key == -2)
		{
			scroll -= 25;
		}
		repaint();
	}
	
	protected void keyRepeated(int key)
	{
		if(key == -1)
		{
			scroll += 75;
		}
		if(key == -2)
		{
			scroll -= 75;
		}
		repaint();
	}
	
	protected final void update(Graphics g)
	{
		boolean d2 = scroll != 0;
		if(itemsh > vmeshautsa)
		{
			canScroll = true;
			if(scrolled < vmeshautsa - itemsh && scrolled != 0)
			{
				scrolled = vmeshautsa - itemsh;
				d2 |= true;
			}
		}
		else
		{
			canScroll = false;
			if(!dragging && scrolled < 0)
			{
				scrolled = 0;
			}
		}
		if(d2)
		{
			g.translate(0, scrolled+scroll);
			scrolled = g.getTranslateY();
			scroll = 0;
		}
		else
			g.translate(0, scrolled);
		
		if(scrolled > 0)
		{
			scrolled = 0;
		}
	}

	protected void callRefresh()
	{
		
	}
	
}
