package vikaTouch.canvas;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import vikaTouch.VikaTouch;
import vikaTouch.newbase.CommandsImpl;
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;
import vikaUI.VikaScreen;

public class AboutCanvas
	extends VikaScreen
{

	public void paint(Graphics g)
	{
		ColorUtils.setcolor(g, 0);
		
		g.setFont(Font.getFont(0, Font.STYLE_BOLD, Font.SIZE_LARGE));
		
		g.drawString("Vika Touch", 8, 8, 0);
		
		g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
		
		g.drawString("Версия: " + VikaTouch.getVersion(), Font.getFont(0, 0, Font.SIZE_LARGE).stringWidth("Vika Touch") + 12, 20, 0);

		g.setFont(Font.getFont(0, 0, Font.SIZE_MEDIUM));
		
		g.drawString("Издатель:", 32, 48, 0);
		g.drawString("Ilya Visotsky", 32, 72, 0);
		
		g.drawString("Разработчик:", 32, 120, 0);
		g.drawString("shinovon", 32, 144, 0);

		g.drawString("Разработчик/Бета-тестер:", 32, 192, 0);
		g.drawString("Feodor0090", 32, 216, 0);
		
		g.drawString("Назад", 0, DisplayUtils.height - 24, 0);
	}
	
	public void pointerReleased(int x, int y)
	{
		if(x < 50 && y > DisplayUtils.height - 30)
		{
			VikaTouch.setDisplay(VikaTouch.menuCanv);
		}
	}
	
	public void keyPressed(int key)
	{
		if(key == 8 || key == -6)
		{
			VikaTouch.setDisplay(VikaTouch.menuCanv);
		}
	}

}
