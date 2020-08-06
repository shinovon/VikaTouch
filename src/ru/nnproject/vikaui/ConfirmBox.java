package ru.nnproject.vikaui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import vikatouch.screens.MainScreen;

public class ConfirmBox {

	private String line1;
	private String line2;
	private Thread ok;
	private Thread cancel;
	
	public ConfirmBox(String text, String subtext, Thread onOk, Thread onCancel)
	{
		line1 = text;
		line2 = subtext;
		ok = onOk;
		cancel = onCancel;
	}
	
	public void Draw(Graphics g) {
		int width = Math.min(DisplayUtils.width-20, 300);
		Font f = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		int h1 = f.getHeight();
		int th = h1*6;
		int y = DisplayUtils.height/2 - th/2;
		int x = DisplayUtils.width/2 - width/2;
		String okT = "Да"; String cancT = "Отмена";
		
		// drawing
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRoundRect(x, y, width, th, 16, 16);
		
		ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
		g.fillRoundRect(x+20, y+h1*3, width/2-40, h1*2, 14, 14);
		g.fillRoundRect(DisplayUtils.width/2+20, y+h1*3, width/2-40, h1*2, 14, 14);
		
		g.setFont(f);
		g.setStrokeStyle(Graphics.SOLID);
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawRoundRect(x, y, width, th, 16, 16);
		g.drawString(line1, DisplayUtils.width/2 - f.stringWidth(line1)/2, y+h1/2, 0);
		g.drawString(line2, DisplayUtils.width/2 - f.stringWidth(line2)/2, y+h1+h1/2, 0);
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.drawString(okT, ((x+20)+(DisplayUtils.width/2-20))/2-f.stringWidth(okT)/2, y+h1*3+h1/2, 0);
		g.drawString(cancT, ((DisplayUtils.width/2+20)+(DisplayUtils.width/2+20+(width/2-40)))/2-f.stringWidth(cancT)/2, y+h1*3+h1/2, 0);
	}
	
	public void OnKey(int key)
	{
		if(key==PressableUIItem.KEY_OK||key==PressableUIItem.KEY_FUNC)
		{
			MainScreen.activeDialog = null;
			if(ok!=null)
				ok.start();
		}
		else if(key==PressableUIItem.KEY_RFUNC|| key==PressableUIItem.KEY_BACK)
		{
			MainScreen.activeDialog = null;
			if(cancel!=null)
				cancel.start();
		}
	}
	
	public void OnTap(int x, int y)
	{
		int width = Math.min(DisplayUtils.width-20, 300);
		Font f = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		int h1 = f.getHeight();
		int th = h1*6;
		int ry = DisplayUtils.height/2 - th/2;
		int rx = DisplayUtils.width/2 - width/2; // углы диалога
		
		if(y>ry+h1*3 && y<y+h1*5)
		{
			if(x>rx+20 && x<DisplayUtils.width/2-20)
			{
				OnKey(PressableUIItem.KEY_OK);
			}
			else if(x>DisplayUtils.width/2+20 && x<DisplayUtils.width/2+20+(width/2-40))
			{
				OnKey(PressableUIItem.KEY_RFUNC);
			}
		}
	}
}
