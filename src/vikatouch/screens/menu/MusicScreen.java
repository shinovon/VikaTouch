package vikatouch.screens.menu;

import javax.microedition.lcdui.Graphics;

import ru.nnproject.vikaui.menu.*;
import ru.nnproject.vikaui.popup.ContextMenu;
import vikatouch.IconsManager;
import vikatouch.VikaTouch;
import vikatouch.items.menu.OptionItem;
import vikatouch.screens.*;

public class MusicScreen
	extends MainScreen
{

	public void draw(Graphics g)
	{
		
	}

	public static void open(final int id, final String name)
	{
		IMenu m = new EmptyMenu()
		{
			public void onMenuItemPress(int i) 
			{ 
				if(i==0)
				{
					// вся музыка
				}
				else if(i==1)
				{
					PlaylistsScreen pls = new PlaylistsScreen();
					pls.load(id,name);
					VikaTouch.setDisplay(pls, 1);
				}
			}
		};
		OptionItem[] oi = new OptionItem[]
		{ 
			new OptionItem(m, "Вся музыка", IconsManager.MUSIC, 0, 50),
			new OptionItem(m, "Плейлисты", IconsManager.MENU, 1, 50)
		};
		VikaTouch.popup(new ContextMenu(oi));
	}
}
