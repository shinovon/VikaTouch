package vikatouch.screens;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.ScrollableCanvas;
import vikatouch.base.Settings;
import vikatouch.base.VikaTouch;
import vikatouch.screens.menu.MenuScreen;

public abstract class MainScreen 
	extends ScrollableCanvas 
{

	public static int lastMenu;
	public Image menuImg;
	public Image newsImg;
	protected boolean hasBackButton;
	public static Image backImg;

	protected void scrollHorizontally(int deltaX)
	{
		/*
		if(deltaX < -7)
		{
			VikaTouch.inst.cmdsInst.commandAction(10, this);
		}
		if(deltaX > 7)
		{
			VikaTouch.inst.cmdsInst.commandAction(11, this);
		}
		*/
	}
	
	public void release(int x, int y)
	{
		if(!dragging || !canScroll)
		{
			int wyw = bbw(DisplayUtils.idispi);
			if(y < oneitemheight + 10)
			{
				if(hasBackButton && x < oneitemheight)
				{
					VikaTouch.inst.cmdsInst.command(14, this);
				}
				if(!(this instanceof SettingsScreen) && x > DisplayUtils.width - oneitemheight)
				{
					VikaTouch.inst.cmdsInst.command(13, this);
				}
			}
			else if(y >= DisplayUtils.height - oneitemheight)
			{
				int acenter = (DisplayUtils.width - wyw) / 2;
				if(x < wyw)
				{
					VikaTouch.inst.cmdsInst.command(0, this);
				}

				if(x > DisplayUtils.width - wyw)
				{
					VikaTouch.inst.cmdsInst.command(2, this);
				}

				if(x > acenter && x < acenter + wyw)
				{
					VikaTouch.inst.cmdsInst.command(1, this);
				}
			}
		}

		super.release(x, y);
	}
	
	protected void drawHeaders(Graphics g, String title)
	{
		double multiplier = (double)DisplayUtils.height / 640.0;
		double ww = 10.0 * multiplier;
		int w = (int)ww;
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_PORTRAIT:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, DisplayUtils.width, 58);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, DisplayUtils.height - 50, DisplayUtils.width, 50);

				if(menuImg != null)
				{
					g.drawImage(menuImg, 304, 606, 0);
				}
				if(!hasBackButton && MenuScreen.logoImg != null)
				{
					g.drawImage(MenuScreen.logoImg, 2, 2, 0);
				}
				if(hasBackButton && backImg != null)
				{
					g.drawImage(backImg, 2, 2, 0);
				}
				if(newsImg != null)
				{
					g.drawImage(newsImg, 37, 604, 0);
				}
				if(VikaTouch.unreadCount > 0)
				{
					if(MenuScreen.dialImg2 != null)
					{
						g.drawImage(MenuScreen.dialImg2, 168, 599, 0);
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawString(""+VikaTouch.unreadCount, 191, 598, 0);
					}
					else if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 168, 604, 0);
					}

				}
				else
				{
					if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 168, 604, 0);
					}
				}
				g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
				g.drawString(title, 72, 29-g.getFont().getHeight()/2, 0);
				g.setFont(Font.getFont(0, 0, 8));
				break;
			}
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, 240, 30);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, DisplayUtils.height - 25, 240, 25);
				
				if(!hasBackButton && MenuScreen.logoImg != null)
				{
					g.drawImage(MenuScreen.logoImg, 2, 1, 0);
				}
				
				if(menuImg != null)
				{
					g.drawImage(menuImg, 212, 303, 0);
				}
				
				if(newsImg != null)
				{
					g.drawImage(newsImg, 18, 301, 0);
				}
				
				if(VikaTouch.unreadCount > 0)
				{
					if(MenuScreen.dialImg2 != null)
					{
						g.drawImage(MenuScreen.dialImg2, 114, 299, 0);
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawString(""+VikaTouch.unreadCount, 126, 300, 0);
					}
					else if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 114, 302, 0);
					}

				}
				else
				{
					if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 114, 302, 0);
					}
				}
				
				g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
				g.drawString(title, 52, 0, 0);
				g.setFont(Font.getFont(0, 0, 8));
				break;
			}

			case DisplayUtils.DISPLAY_ALBUM:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, 640, 58);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, 310, 640, 50);

				if(!hasBackButton && MenuScreen.logoImg != null)
				{
					g.drawImage(MenuScreen.logoImg, 2, 2, 0);
				}
				if(newsImg != null)
				{
					g.drawImage(newsImg, 36, 324, 0);
				}
				if(VikaTouch.unreadCount > 0)
				{

					if(MenuScreen.dialImg2 != null)
					{
						g.drawImage(MenuScreen.dialImg2, 308, 319, 0);
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawString(""+VikaTouch.unreadCount, 330, 318, 0);
					}
					else if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 308, 324, 0);
					}

				}
				else
				{
					if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 308, 324, 0);
					}
				}
				if(menuImg != null)
				{
					g.drawImage(menuImg, 584, 326, 0);
				}
				g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
				g.drawString(title, 72, 14, 0);
				g.setFont(Font.getFont(0, 0, 8));
				
				break;
			}
			
			case DisplayUtils.DISPLAY_EQWERTY:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, 320, 30);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, 215, 320, 25);
				
				if(!hasBackButton && MenuScreen.logoImg != null)
				{
					g.drawImage(MenuScreen.logoImg, 2, 1, 0);
				}
				
				if(menuImg != null)
				{
					g.drawImage(menuImg, 292, 303-75, 0);
				}
				
				if(newsImg != null)
				{
					g.drawImage(newsImg, 18, 301-75, 0);
				}
				
				if(VikaTouch.unreadCount > 0)
				{
					if(MenuScreen.dialImg2 != null)
					{
						g.drawImage(MenuScreen.dialImg2, 114, 299-75, 0);
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawString(""+VikaTouch.unreadCount, 126, 300-75, 0);
					}
					else if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 114, 302-75, 0);
					}

				}
				else
				{
					if(MenuScreen.dialImg != null)
					{
						g.drawImage(MenuScreen.dialImg, 114, 302-75, 0);
					}
				}
				
				g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
				g.drawString(title, 52, 0, 0);
				g.setFont(Font.getFont(0, 0, 8));
				break;
			}
			
			default:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, DisplayUtils.width, oneitemheight + w);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, DisplayUtils.height - oneitemheight, DisplayUtils.width, oneitemheight);
				
				if(!hasBackButton && MenuScreen.logoImg != null)
				{
					g.drawImage(MenuScreen.logoImg, 2, 1, 0);
				}

				g.drawString(title, 72, 0, 0);
			}
		}
		if(Settings.debugInfo)
		{
			g.setColor(0xffff00);
			int xx = endx;
			int yy = endy;
			if(xx == -1)
			{
				xx = lastx;
				yy = lasty;
			}
			g.drawLine(startx, starty, xx, yy);
			g.drawRect(startx-2, starty-2, 4, 4);
			g.setColor(0xff0000);
			g.drawRect(endx-2, endy-2, 4, 4);
			g.drawString("cs"+scroll + " sc" + scrolled + " d" + drift + " ds" + driftSpeed + " st" + scrollingTimer + " sp" + scrollPrev + " t" + timer, 0, 30, 0);
		}
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
