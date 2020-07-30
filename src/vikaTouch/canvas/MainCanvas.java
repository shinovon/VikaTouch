package vikaTouch.canvas;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.menu.MenuCanvas;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;

public abstract class MainCanvas 
	extends ScrollableCanvas 
{

	public static int lastMenu;
	public Image menuImg;
	public Image newsImg;

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
	
	public void pointerReleased(int x, int y)
	{
		if(!dragging || !canScroll)
		{
			int wyw = bbw(DisplayUtils.idispi);
			if(y < oneitemheight + 10)
			{
				if(!(this instanceof SettingsCanvas) && x > DisplayUtils.width - oneitemheight)
				{
					VikaTouch.inst.cmdsInst.commandAction(13, this);
				}
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
				g.fillRect(0, 0, 360, 58);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, 590, 360, 50);

				if(menuImg != null)
				{
					g.drawImage(menuImg, 304, 606, 0);
				}
				if(MenuCanvas.logoImg != null)
				{
					g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
				}
				if(newsImg != null)
				{
					g.drawImage(newsImg, 37, 604, 0);
				}
				if(VikaTouch.unreadCount > 0)
				{
					if(MenuCanvas.dialImg2 != null)
					{
						g.drawImage(MenuCanvas.dialImg2, 168, 599, 0);
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawString(""+VikaTouch.unreadCount, 191, 598, 0);
					}
					else if(MenuCanvas.dialImg != null)
					{
						g.drawImage(MenuCanvas.dialImg, 168, 604, 0);
					}

				}
				else
				{
					if(MenuCanvas.dialImg != null)
					{
						g.drawImage(MenuCanvas.dialImg, 168, 604, 0);
					}
				}
				g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
				g.drawString(title, 72, 14, 0);
				g.setFont(Font.getFont(0, 0, 8));
				break;
			}
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, 240, 30);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, 295, 240, 25);
				
				if(MenuCanvas.logoImg != null)
				{
					g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
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
					if(MenuCanvas.dialImg2 != null)
					{
						g.drawImage(MenuCanvas.dialImg2, 114, 299, 0);
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawString(""+VikaTouch.unreadCount, 126, 300, 0);
					}
					else if(MenuCanvas.dialImg != null)
					{
						g.drawImage(MenuCanvas.dialImg, 114, 302, 0);
					}

				}
				else
				{
					if(MenuCanvas.dialImg != null)
					{
						g.drawImage(MenuCanvas.dialImg, 114, 302, 0);
					}
				}
				
				g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
				g.drawString(title, 52, 2, 0);
				g.setFont(Font.getFont(0, 0, 8));
				break;
			}

			case DisplayUtils.DISPLAY_ALBUM:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, 640, 58);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, 310, 640, 50);

				if(MenuCanvas.logoImg != null)
				{
					g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
				}
				if(newsImg != null)
				{
					g.drawImage(newsImg, 36, 324, 0);
				}
				if(VikaTouch.unreadCount > 0)
				{

					if(MenuCanvas.dialImg2 != null)
					{
						g.drawImage(MenuCanvas.dialImg2, 308, 319, 0);
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawString(""+VikaTouch.unreadCount, 330, 318, 0);
					}
					else if(MenuCanvas.dialImg != null)
					{
						g.drawImage(MenuCanvas.dialImg, 308, 324, 0);
					}

				}
				else
				{
					if(MenuCanvas.dialImg != null)
					{
						g.drawImage(MenuCanvas.dialImg, 308, 324, 0);
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
			default:
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.fillRect(0, 0, DisplayUtils.width, oneitemheight + w);
				ColorUtils.setcolor(g, -3);
				g.fillRect(0, DisplayUtils.height - oneitemheight, DisplayUtils.width, oneitemheight);
				
				if(MenuCanvas.logoImg != null)
				{
					g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
				}

				g.drawString(title, 72, 14, 0);
			}
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
