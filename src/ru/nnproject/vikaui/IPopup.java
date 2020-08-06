package ru.nnproject.vikaui;

import javax.microedition.lcdui.Graphics;

public interface IPopup {
	public void Draw(Graphics g);
	public void OnKey(int key);
	public void OnTap(int x, int y);
}
