package ru.nnproject.vikaui;

import javax.microedition.lcdui.Graphics;

public interface UIItem
{
	public void paint(Graphics g, int y, int scrolled);
	
	public int getDrawHeight();
}
