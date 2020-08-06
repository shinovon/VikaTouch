package ru.nnproject.vikaui;

import javax.microedition.lcdui.Graphics;

import vikatouch.screens.MainScreen;

public abstract class ScrollableCanvas
	extends VikaScreen
{
	
	protected int startx;
	protected int starty;
	protected int endx;
	protected int endy;
	protected short scroll;
	protected int scrolled;
	protected int lasty;
	protected boolean dragging;
	protected boolean canScroll;
	public static short oneitemheight = 50;
	public short itemsCount = 5;
	public int itemsh = itemsCount * oneitemheight;
	protected int lastx;
	public static short vmeshautsa = 528;
	public static final double scrollSpeed = 1.8;
	public PressableUIItem[] uiItems;
	public short scrollOffset;
	public short currentItem;
	public static boolean keysMode = false;
	
	public short drift;
	public short driftSpeed;
	public short scrollingTimer;
	protected short scrollPrev;
	protected short timer;
	
	public ScrollableCanvas()
	{
		super();
	}

	public abstract void draw(Graphics g);

	public final void drag(int x, int y)
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
				scroll = (short)((double) -deltaY * scrollSpeed);
				scrollPrev += scroll;
				scrollingTimer += Math.abs(scroll) / 14;
				if(Math.abs(scroll / 3) > Math.abs(driftSpeed))
					driftSpeed = (short) (scroll / 3);
			}
			else
			{
				scrollHorizontally(deltaX);
			}
		}
		if(DisplayUtils.canvas.isSensorModeOK())
		{
			if(ndeltaY > 0 || ndeltaX > 0)
			{
				dragging = true;
			}
		}
		else if(DisplayUtils.canvas.isSensorModeJ2MELoader())
		{
			if(ndeltaY > 1 || ndeltaX > 1)
			{
				dragging = true;
			}
		}
		lastx = x;
		lasty = y;
		timer = 0;
	}

	protected abstract void scrollHorizontally(int deltaX);

	public void press(int x, int y)
	{
		timer = 0;
		VikaCanvas.debugString = "pressed " + x +" " +y;
		scrollingTimer = 0;
		drift = 0;
		driftSpeed = 0;
		scrollPrev = 0;
		keysMode = false;
		startx = x;
		starty = y;
		endx = -1;
		endy = -1;
	}

	public void release(int x, int y)
	{
		VikaCanvas.debugString = "released " + x +" " +y;
		if(timer < 7)
		{
			if(scrollPrev > 0)
				drag(x, y);
			drift = scrollPrev;
		}
		scrollPrev = 0;
		keysMode = false;
		endx = x;
		endy = y;
		dragging = false;
		repaint();
	}
	
	public void press(int key)
	{
		if(MainScreen.activeDialog!=null)
		{
			MainScreen.activeDialog.OnKey(key);
			repaint();
			return;
		}
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
			DisplayUtils.canvas.callCommand(10, this);
		}
		else if(key == -4)
		{
			DisplayUtils.canvas.callCommand(11, this);
		}
		else if(key == -7)
		{
			DisplayUtils.canvas.callCommand(14, this);
		}
		else
		{
			uiItems[currentItem].keyPressed(key);
		}
		repaint();
		try
		{
			VikaCanvas.debugString = "" + key + " " + DisplayUtils.canvas.getKeyName(key) + " " + currentItem + " " + itemsCount + " " + uiItems[currentItem].isSelected();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void repeat(int key)
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
		catch (Exception e) { }
		currentItem--;
		if(currentItem < 0)
		{
			currentItem = (short) (itemsCount-1);
			scrolled -= 1900;
		}
		else
		{
			scrolled += uiItems[currentItem].getDrawHeight();
		}
		try 
		{
			uiItems[currentItem].setSelected(true);
		}
		catch (Exception e) { }
	}

	protected final void update(Graphics g)
	{
		try
		{
			if(timer < 3200)
				timer++;
			if(scrollingTimer > 0)
				scrollingTimer--;
			
			if(drift != 0 && driftSpeed != 0 && scrollingTimer > 5)
			{
				scroll += driftSpeed;
				drift -= driftSpeed;
				driftSpeed *= 0.975;
			}
		}
		catch(ArithmeticException e)
		{
			
		}
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
