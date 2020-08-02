package vikaUI;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import vikaTouch.VikaTouch;
import vikaTouch.newbase.Settings;

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
		if(Settings.sensorMode == Settings.SENSOR_OK)
		{
			if(ndeltaY > 0 || ndeltaX > 0)
			{
				dragging = true;
			}
		}
		else if(Settings.sensorMode == Settings.SENSOR_J2MELOADER)
		{
			if(ndeltaY > 1 || ndeltaX > 1)
			{
				dragging = true;
			}
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
		else if(key == -2)
		{
			down();
		}
		else if(key == -3)
		{
			VikaTouch.inst.cmdsInst.commandAction(10, this);
		}
		else if(key == -4)
		{
			VikaTouch.inst.cmdsInst.commandAction(11, this);
		}
		else if(key == -7)
		{
			VikaTouch.inst.cmdsInst.commandAction(14, this);
		}
		else
		{
			uiItems[currentItem].keyPressed(key);
		}
		repaint();
		try {
			VikaCanvas.debugString = "" + key + " " + VikaTouch.canvas.getKeyName(key) + " " + currentItem + " " + itemsCount + " " + uiItems[currentItem].isSelected();
		} catch (Exception e) { e.printStackTrace(); }
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
	
	protected void down()
	{
		try
		{
			uiItems[currentItem].setSelected(false);
		}
		catch (Exception e)
		{
			
		}
		currentItem++;
		if(currentItem >= itemsCount)
		{
			currentItem = 0;
			scrolled += 1900;
		}
		else
			scrolled -= uiItems[currentItem].getDrawHeight();
		uiItems[currentItem].setSelected(true);
	}

	protected void up()
	{
		try
		{
			uiItems[currentItem].setSelected(false);
		}
		catch (Exception e)
		{
			
		}
		currentItem--;
		if(currentItem < 0)
		{
			currentItem = itemsCount - 1;
			scrolled -= 1900;
		}
		else
			scrolled += uiItems[currentItem].getDrawHeight();
		uiItems[currentItem].setSelected(true);
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
		{
			if(scrolled > 0)
			{
				scrolled = 0;
			}
			if(scrolled < vmeshautsa - itemsh && scrolled != 0)
			{
				scrolled = vmeshautsa - itemsh;
			}
			g.translate(0, scrolled);
		}
		scroll = 0;
	}

	protected void callRefresh()
	{
		
	}
	
}
