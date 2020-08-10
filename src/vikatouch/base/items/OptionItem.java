package vikatouch.base.items;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import ru.nnproject.vikaui.menu.IMenu;
import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.screen.ScrollableCanvas;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikatouch.base.IconsManager;

public class OptionItem
	implements PressableUIItem
{

	public IMenu menu;
	public String text;
	public int icon;
	public int h;
	public int i;
	boolean s;
	static Font f;
	
	public int drawX; public int fillW; // for context menu
	
	public OptionItem(IMenu m, String t, int ic, int i, int h)
	{
		this.h = h;
		this.i = i;
		icon = ic;
		text = t;
		menu = m;
		f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	}
	
	public void paint(Graphics g, int y, int scrolled) {
		if(ScrollableCanvas.keysMode && s)
		{
			ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
			g.fillRect(drawX, y, fillW==0?DisplayUtils.width:fillW, h);
		}
		ColorUtils.setcolor(g, 0);
		g.setFont(f);
		int x = drawX + 48;
		if(icon == -1)
			x = x - 40;
		g.drawString(text, x, y + ((h/2) - (f.getHeight()/2)), 0);
		if(icon != -1)
			g.drawImage(IconsManager.ico[icon], drawX + 12, y + (h/2 - 12), 0);
	}

	public int getDrawHeight() {
		return h;
	}

	public void tap(int x, int y) {
		menu.onMenuItemPress(i);
	}

	public void keyPressed(int key) {
		if(key == KEY_OK)
			menu.onMenuItemPress(i);
	}

	public boolean isSelected() {
		return s;
	}

	public void setSelected(boolean selected) {
		s = selected;
	}

	public void addDrawHeight(int i)
	{
		h += i;
	}

	public void setDrawHeight(int i)
	{
		h = i;
	}

}
