package vikaTouch.base;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import vikaTouch.newbase.DisplayUtils;

public final class InvisibleScreen extends Canvas {

	public InvisibleScreen()
	{
		DisplayUtils.checkdisplay(this);
	}

	protected final void paint(Graphics g) 
	{
		
	}
}