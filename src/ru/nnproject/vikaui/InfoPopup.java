package ru.nnproject.vikaui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import vikatouch.base.VikaTouch;
import vikatouch.screens.MainScreen;

public class InfoPopup
	extends VikaNotice
{
	private String[] lines;
	private Thread ok;
	private int linesCount;
	
	public InfoPopup(String text, Thread onOk)
	{
		lines = TextBreaker.breakText(text, false, null, true, Math.min(DisplayUtils.width-10, 350)-60);
		ok = onOk;
		int i=0;
		while(i<lines.length && lines[i]!=null)
		{
			i++;
		}
		linesCount = i;
	}
	
	public void draw(Graphics g) {
		int width = Math.min(DisplayUtils.width-10, 350);
		Font f = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		int h1 = f.getHeight();
		int th = h1*4 + h1*linesCount;
		int y = DisplayUtils.height/2 - th/2;
		int x = DisplayUtils.width/2 - width/2;
		String okT = "ОК";
		
		// drawing
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRoundRect(x, y, width, th, 16, 16);
		
		ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
		g.fillRoundRect(DisplayUtils.width/2-25, y+h1*(linesCount+1), 50, h1*2, 14, 14);
		
		g.setFont(f);
		g.setStrokeStyle(Graphics.SOLID);
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawRoundRect(x, y, width, th, 16, 16); // бордер
		for(int i = 0; i < linesCount;i++)
		{
			if(lines[i]!=null) g.drawString(lines[i], DisplayUtils.width/2 - f.stringWidth(lines[i])/2, y+h1/2+h1*i, 0);
		}
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.drawString(okT, DisplayUtils.width/2-f.stringWidth(okT)/2, y+h1*(linesCount+1)+h1/2, 0);
	}
	
	public void key(int key)
	{
		VikaTouch.canvas.currentAlert = null;
		if(ok!=null)
			ok.start();
	}
	
	public void tap(int x, int y)
	{
		int width = Math.min(DisplayUtils.width-20, 300);
		Font f = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		int h1 = f.getHeight();
		int th = h1*4 + h1*linesCount;
		int ry = DisplayUtils.height/2 - th/2;
		int rx = DisplayUtils.width/2 - width/2;
		
		if(y>ry+h1*(linesCount+1) && y<ry+th-h1)
		{
			if(x>DisplayUtils.width/2-25 && x<DisplayUtils.width/2+25)
			{
				key(PressableUIItem.KEY_OK);
			}
		}
	}
}
