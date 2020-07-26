package vikaTouch.canvas;

import vikaTouch.VikaTouch;
import vikaTouch.newbase.DisplayUtils;

public abstract class MainCanvas 
	extends ScrollableCanvas 
{

	protected void scrollHorizontally(int deltaX)
	{
		if(deltaX < -7)
		{
			VikaTouch.inst.cmdsInst.commandAction(10, this);
		}
		if(deltaX > 7)
		{
			VikaTouch.inst.cmdsInst.commandAction(11, this);
		}
	}
	
	protected void pointerReleased(int x, int y)
	{
		if(!dragging || !canScroll)
		{
			int wyw = bbw(DisplayUtils.idispi);
			if(y < oneitemheight + 10)
			{
				
			}
			else if(y >= DisplayUtils.height - oneitemheight)
			{
				int acenter = (DisplayUtils.width - wyw) / 2;
				if(x < wyw)
				{
					VikaTouch.inst.cmdsInst.commandAction(0, this);
				}

				if(x > DisplayUtils.width - wyw)
				{
					VikaTouch.inst.cmdsInst.commandAction(2, this);
				}

				if(x > acenter && x < acenter + wyw)
				{
					VikaTouch.inst.cmdsInst.commandAction(1, this);
				}
			}
		}

		super.pointerReleased(x, y);
	}

	private int bbw(int i)
	{
		switch(i)
		{
			case 1:
				return 96;
			case 2:
				return 64;
			case 5:
				return 96;
			case 6:
				return 96;
			case 3:
				return 64;
			case 4:
				return 64;
			default:
				return 64;
		}
	}

}
