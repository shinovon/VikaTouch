package vikatouch.items.music;

import javax.microedition.lcdui.Graphics;

import ru.nnproject.vikaui.menu.IMenu;
import ru.nnproject.vikaui.utils.images.IconsManager;
import vikatouch.items.menu.OptionItem;
import vikatouch.music.MusicPlayer;

// а-ля по канонам ООП
public class MusicMenuItem extends OptionItem {

	public MusicMenuItem(IMenu m, String t, int ic, int i, int h) {
		super(m, t, ic, i, h);
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		icon = (MusicPlayer.inst==null)?IconsManager.MUSIC:IconsManager.PLAY;
		super.paint(g, y, scrolled);
	}

}
