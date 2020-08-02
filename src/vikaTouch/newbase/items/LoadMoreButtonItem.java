package vikaTouch.newbase.items;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import vikaTouch.canvas.INextLoadable;
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;
import vikaUI.PressableUIItem;
import vikaUI.ScrollableCanvas;

public class LoadMoreButtonItem implements PressableUIItem {

	public LoadMoreButtonItem (INextLoadable list) {
		l = list;
		// precalcs
		f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		h = f.getHeight();
		w = f.stringWidth(text);
	}
	
	private INextLoadable l;
	private int w; private int h;
	private Font f;
	
	public boolean selected;
	
	public static final String text = "Загрузить ещё...";
	public void paint(Graphics g, int y, int scrolled) {
		if(ScrollableCanvas.keysMode && selected)
		{
			ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
			g.fillRect(0, y, DisplayUtils.width, 40);
		}
		ColorUtils.setcolor(g, 0);
		g.setFont(f);
		g.drawString(text, DisplayUtils.width/2 - (w/2), y + (40 - (h/2)), 0);
	}

	public int getDrawHeight() {
		return 40;
	}

	public void tap(int x, int y) {
		l.LoadNext();
	}

	public void keyPressed(int key) {
		if(key == KEY_OK) {
			l.LoadNext();
		}
	}

	public boolean isSelected()
	{
		return selected;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

}
