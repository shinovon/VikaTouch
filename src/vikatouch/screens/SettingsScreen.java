package vikatouch.screens;

import javax.microedition.lcdui.Graphics;

import ru.nnproject.vikaui.menu.IMenu;
import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.DocItem;
import vikatouch.base.items.OptionItem;
import vikatouch.screens.menu.MenuScreen;

public class SettingsScreen
	extends MainScreen
	implements IMenu 
{
	
	
	
	public SettingsScreen()
	{
		super();
		oneitemheight = 40;
		uiItems = new PressableUIItem[15];
		uiItems[0] = new OptionItem(this, "123", -1, 0, 50);
		uiItems[1] = new OptionItem(this, "", -1, 1, 50);
		itemsh = 58 + (oneitemheight * uiItems.length);
	}

	public void draw(Graphics g)
	{
		update(g);
		
		int y = 58;
		if(uiItems!=null)
		{
			for (int i=0;i<uiItems.length;i++)
			{
				if(uiItems[i]!=null) {
					uiItems[i].paint(g, y, scrolled);
					y+=uiItems[i].getDrawHeight();
				}
			}
		}
		
		g.translate(0, -g.getTranslateY());
		drawHUD(g, "Настройки");
	}
	
	public final void release(int x, int y)
	{
		try
		{
			if(y > 58 && y < DisplayUtils.height - oneitemheight)
			{
				int h = oneitemheight;
				int yy1 = y - (scrolled + 58);
				int i = yy1 / h;
				if(i < 0)
					i = 0;
				if(!dragging)
				{
					uiItems[i].tap(x, yy1 - (h * i));
				}
			}
				
		}
		catch (ArrayIndexOutOfBoundsException e) 
		{ 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		super.release(x, y);
	}
	
	public void SettingSet (int setIndex, int var)
	{
		
	}

	public void onMenuItemPress(int i)
	{
		switch(i)
		{
			case 0:
			{
				
			}
			case 1:
			{
				
			}
		}
	}

	public void onMenuItemOption(int i)
	{
		
	}

}
