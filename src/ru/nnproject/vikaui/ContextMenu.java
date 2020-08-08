package ru.nnproject.vikaui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import vikatouch.base.VikaTouch;
import vikatouch.base.items.OptionItem;

public class ContextMenu extends VikaNotice {

	public OptionItem[] items;
	public int selected;
	
	public ContextMenu(OptionItem[] list) 
	{
		items = list;
	}
	
	public void draw(Graphics g) {
		int itemsH = 16; // margin = 8
		int width = Math.min(DisplayUtils.width-8, 350);
		int x = DisplayUtils.width/2 - width/2;
		for(int i=0; i < items.length; i++)
		{
			items[i].drawX = x+8;
			items[i].fillW = width-16;
			itemsH = itemsH + items[i].getDrawHeight();
		}
		
		Font f = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		int h1 = f.getHeight();
		int th = itemsH;
		int y = DisplayUtils.height/2 - th/2;
		
		// BG
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRoundRect(x, y, width, th, 16, 16);
		// border
		g.setStrokeStyle(Graphics.SOLID);
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawRoundRect(x, y, width, th, 16, 16);
		
		int cy = 8;
		for(int i=0; i < items.length; i++)
		{
			items[i].paint(g, cy, 0);
			cy = cy + items[i].getDrawHeight();
		}
	}
	
	public void key(int key)
	{
		ScrollableCanvas.keysMode = true;
		if(key == PressableUIItem.KEY_OK)
		{
			VikaTouch.canvas.currentAlert = null;
			items[selected].keyPressed(PressableUIItem.KEY_OK);
		}
		else if(key == -1)
		{
			selected--; 
			if(selected<0) selected = items.length-1;
		}
		else if(key == -2)
		{
			selected++;
			if(selected>=items.length) selected = 0;
		}
		else if(key == PressableUIItem.KEY_RFUNC)
		{
			VikaTouch.canvas.currentAlert = null;
		}
	}
	
	public void release(int x, int y)
	{
		int width = Math.min(DisplayUtils.width-8, 350);
		// TODO
	}

}
