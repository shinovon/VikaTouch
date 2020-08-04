package ru.nnproject.vikatouch;

import java.io.*;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import org.json.me.JSONObject;

import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.UIThread;
import ru.nnproject.vikaui.VikaScreen;
import vikamobilebase.ImageStorage;
import vikamobilebase.VikaUtils;
import vikatouch.base.CaptchaObject;
import vikatouch.base.CommandsImpl;
import vikatouch.base.Dialogs;
import vikatouch.base.IconsManager;
import vikatouch.base.Settings;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaCanvasInst;
import vikatouch.base.VikaTouch;
import vikatouch.screens.*;
import vikatouch.screens.menu.DocsScreen;
import vikatouch.screens.menu.FriendsScreen;
import vikatouch.screens.menu.GroupsScreen;
import vikatouch.screens.menu.MenuScreen;
import vikatouch.screens.menu.PhotosScreen;
import vikatouch.screens.menu.VideosScreen;

import javax.microedition.lcdui.game.GameCanvas;

public final class VikaTouchApp
	extends MIDlet
	implements Runnable
{
	public boolean isPaused;
	public boolean started = false;
	

	public void destroyApp(boolean arg0)
	{
		this.notifyDestroyed();
	}

	protected void pauseApp()
	{
		isPaused = true;
	}

	protected void startApp()
	{
		VikaTouch.mobilePlatform = System.getProperty("microedition.platform");
		isPaused = false;
		
		if(!started)
		{
			started = true;
			VikaTouch.appInst = this;
			VikaTouch.inst = new VikaTouch();
			VikaTouch.inst.start();
		}
	}

	public void run()
	{
		VikaTouch.inst.threadRun();
	}
}