package vikaUI;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import vikaTouch.VikaTouch;

public abstract class ScrollableCanvas
	extends VikaScreen
{
	
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
	public PressableUIItem[] uiItems;
	public int scrollOffset;
	public int currentItem;
	public static boolean keysMode = false;
	
	public ScrollableCanvas()
	{
		super();
	}

	public abstract void paint(Graphics g);

	public final void pointerDragged(int x, int y)
	{
		keysMode = false;
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

	public void pointerPressed(int x, int y)
	{
		keysMode = false;
		startx = x;
		starty = y;
	}

	public void pointerReleased(int x, int y)
	{
		keysMode = false;
		endx = x;
		endy = y;
		dragging = false;
		repaint();
	}
	
	public void keyPressed(int key)
	{
		keysMode = true;
		if(key == -1)
		{
			up();
		}
		else if(key == Canvas.DOWN)
		{
			down();
		}
		else if(key == 0)
		{
			
		}
		else
		{
			uiItems[currentItem].keyPressed(key);
		}
		repaint();
	}
	
	public void keyRepeated(int key)
	{
		keysMode = true;
		if(key == -1)
		{
			up();
		}
		if(key == -2)
		{
			down();
		}
		repaint();
	}
	
	private void down()
	{
		currentItem--;
		if(currentItem < 0)
			currentItem = 0;
		scrolled += uiItems[currentItem].getDrawHeight();
	}

	private void up()
	{
		currentItem++;
		if(currentItem >= itemsCount)
			currentItem = itemsCount - 1;
		scrolled -= uiItems[currentItem].getDrawHeight();
	}

	protected final void update(Graphics g)
	{
		boolean d2 = scroll != 0;
		if(itemsh > vmeshautsa)
		{
			canScroll = true;
		}
		else
		{
			canScroll = false;
			if(scrolled < 0)
			{
				scrolled = 0;
			}
		}
		if(d2)
		{
			scrolled = scrolled + scroll;
			if(scrolled > 0)
			{
				scrolled = 0;
			}
			if(scrolled < vmeshautsa - itemsh && scrolled != 0)
			{
				scrolled = vmeshautsa - itemsh;
			}
			g.translate(0, scrolled);
			scroll = 0;
		}
		else
			g.translate(0, scrolled);

		scroll = 0;
	}

	protected void callRefresh()
	{
		
	}
	
}
