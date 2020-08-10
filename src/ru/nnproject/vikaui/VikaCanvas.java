package ru.nnproject.vikaui;

import javax.microedition.lcdui.game.GameCanvas;

import ru.nnproject.vikaui.screen.VikaScreen;

public abstract class VikaCanvas
	extends GameCanvas
{
	public static String debugString = "";

	protected VikaCanvas()
	{
		super(false);
	}

	public abstract void tick();

	protected abstract void callCommand(int i, VikaScreen scrollableCanvas);

	public abstract void paint();

	protected abstract boolean isSensorModeOK();

	protected abstract boolean isSensorModeJ2MELoader();

}
