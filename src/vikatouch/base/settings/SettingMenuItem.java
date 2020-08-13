package vikatouch.base.settings;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import ru.nnproject.vikaui.menu.IMenu;
import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.popup.ContextMenu;
import ru.nnproject.vikaui.popup.InfoPopup;
import ru.nnproject.vikaui.screen.ScrollableCanvas;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikatouch.base.IconsManager;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.OptionItem;
import vikatouch.base.local.TextLocal;

public class SettingMenuItem implements PressableUIItem, IMenu {

	public SettingsScreen ss;
	public String text;
	public int icon;
	public int h;
	public int optN;
	boolean s;
	static Font f;
	public String[] opts;
	public int currentOption;
	String help;
	
	public int drawX; public int fillW; // for context menu
	
	public SettingMenuItem(SettingsScreen s, String title, int ic, int optN, int h, int[] list, int curr, String info)
	{
		this.h = h;
		this.optN = optN;
		icon = ic;
		text = title;
		ss = s;
		opts = new String[list.length];
		for(int i=0;i<list.length;i++)
		{
			opts[i] = String.valueOf(list[i]);
		}
		currentOption = curr;
		help = info;
		f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	}
	
	public SettingMenuItem(SettingsScreen s, String title, int ic, int optN, int h, String[] list, int curr, String info)
	{
		this.h = h;
		this.optN = optN;
		icon = ic;
		text = title;
		ss = s;
		opts = list;
		currentOption = curr;
		help = info;
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
		if(icon != -1)
			g.drawImage(IconsManager.ico[icon], drawX + 12, y + (h/2 - 12), 0);
		try
		{
			g.drawString(text, x, y + ((h/4) - (f.getHeight()/2)), 0);
			ColorUtils.setcolor(g, ColorUtils.TEXT2);
			g.drawString(opts[currentOption], x, y + ((h*3/4) - (f.getHeight()/2)), 0);
		}
		catch(Exception e) { e.printStackTrace(); }
	}

	public int getDrawHeight() {
		return h;
	}

	public void tap(int x, int y) {
		keyPressed(-5);
	}

	public void keyPressed(int key) {
		if(key == KEY_OK)
		{
			OptionItem[] po = new OptionItem[opts.length];
			for(int i=0;i<opts.length;i++)
			{
				po[i] = new OptionItem(this, opts[i], IconsManager.SETTINGS, i, 40);
			}
			VikaTouch.popup(new ContextMenu(po));
		} else if(help!=null && key == KEY_FUNC)
		{
			VikaTouch.popup(new InfoPopup(help, null, TextLocal.inst.get("settings.help"), null));
		}
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

	public void onMenuItemPress(int i) {
		ss.settingSet(optN, i);
		currentOption = i;
	}

	public void onMenuItemOption(int i) {
		if(help!=null)
		{
			VikaTouch.popup(new InfoPopup(help, null, TextLocal.inst.get("settings.help"), null));
		}
	}
}
