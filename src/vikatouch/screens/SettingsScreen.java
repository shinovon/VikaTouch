package vikatouch.screens;

import javax.microedition.lcdui.Graphics;

import ru.nnproject.vikaui.menu.IMenu;
import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikatouch.base.IconsManager;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.*;
import vikatouch.base.local.TextLocal;
import vikatouch.base.settings.Settings;
import vikatouch.screens.menu.MenuScreen;

public class SettingsScreen
	extends MainScreen
	implements IMenu 
{
	
	
	
	public SettingsScreen()
	{
		super();
		String[] eOd = new String[] { TextLocal.inst.get("settings.disabled"), TextLocal.inst.get("settings.enabled") };
		int[] countVals = new int[] { 10, 20, 30, 50, 80, 100 }; int countValDef = 1;
		int[] refreshVals = new int[] { 0, 2, 5, 8, 10, 15 }; int refreshValDef = 3;
		oneitemheight = 40;
		uiItems = new PressableUIItem[16];
		// анимация
		uiItems[0] = new SettingMenuItem(this, TextLocal.inst.get("settings.animTr"), IconsManager.MENU, 0, 
				oneitemheight, eOd, Settings.animateTransition?1:0, null);
		// кэш
		uiItems[1] = new SettingMenuItem(this, TextLocal.inst.get("settings.enableCache"), IconsManager.PHOTOS, 1, 
				oneitemheight, eOd, Settings.cacheImages?1:0, null);
		// минус авы
		uiItems[2] = new SettingMenuItem(this, TextLocal.inst.get("settings.dontLoadAvas"), IconsManager.PHOTOS, 2, 
				oneitemheight, eOd, Settings.dontLoadAvas?1:0, null);
		// соединение
		uiItems[3] = new SettingMenuItem(this, TextLocal.inst.get("settings.conn"), IconsManager.LINK, 3, 
				oneitemheight, new String[] { TextLocal.inst.get("settings.proxy"), TextLocal.inst.get("settings.https") }, 
				Settings.https?1:0, null);
		// сенсор
		uiItems[4] = new SettingMenuItem(this, TextLocal.inst.get("settings.sensor"), IconsManager.OPTIONS, 4, 
				oneitemheight, new String[] { TextLocal.inst.get("settings.disabled"), TextLocal.inst.get("settings.j2meLs"), TextLocal.inst.get("settings.resistive") }, 
				Settings.sensorMode, TextLocal.inst.get("settings.sensorInfo"));
		// списки
		int j = -1;
		for(int i=0;i<countVals.length;i++)
		{
			if(countVals[i]==Settings.simpleListsLength) j = i;
		}
		if(j==-1)
		{
			j = countValDef;
			Settings.simpleListsLength = countVals[j];
		}
		uiItems[5] = new SettingMenuItem(this, TextLocal.inst.get("settings.listsLength"), IconsManager.MENU, 5, 
				oneitemheight, countVals, j, null);
		// сообщения за раз
		j = -1;
		for(int i=0;i<countVals.length;i++)
		{
			if(countVals[i]==Settings.messagesPerLoad) j = i;
		}
		if(j==-1)
		{
			j = countValDef;
			Settings.messagesPerLoad = countVals[j];
		}
		uiItems[6] = new SettingMenuItem(this, TextLocal.inst.get("settings.msgsLength"), IconsManager.MSGS, 6, 
				oneitemheight, countVals, j, null);
		// частота обновления
		j = -1;
		for(int i=0;i<refreshVals.length;i++)
		{
			if(refreshVals[i]==Settings.refreshRate) j = i;
		}
		if(j==-1)
		{
			j = refreshValDef;
			Settings.messagesPerLoad = refreshVals[j];
		}
		uiItems[7] = new SettingMenuItem(this, TextLocal.inst.get("settings.refreshRate"), IconsManager.MSGS, 7, 
				oneitemheight, refreshVals, j, null);
		// размер видео
		uiItems[8] = new OptionItem(this, TextLocal.inst.get("settings.videoRes"), IconsManager.VIDEOS, 21, oneitemheight);
		// поведение аудио
		uiItems[9] = new OptionItem(this, TextLocal.inst.get("settings.audio"), IconsManager.MUSIC, 22, oneitemheight);
		// отладка тача
		uiItems[10] = new SettingMenuItem(this, TextLocal.inst.get("settings.touchDebug"), IconsManager.MENU, 10, 
				oneitemheight, eOd, Settings.debugInfo?1:0, null);
		// статистика
		uiItems[11] = new SettingMenuItem(this, TextLocal.inst.get("settings.telemetry"), IconsManager.MENU, 11, 
				oneitemheight, eOd, Settings.telemetry?1:0, TextLocal.inst.get("settings.telemetryInfo"));
		// ошибки
		uiItems[12] = new SettingMenuItem(this, TextLocal.inst.get("settings.sendErrors"), IconsManager.MENU, 12, 
				oneitemheight, eOd, Settings.sendErrors?1:0, null);
		
		uiItems[13] = new OptionItem(this, TextLocal.inst.get("settings.logout"), IconsManager.CLOSE, -1, oneitemheight);
		uiItems[14] = new OptionItem(this, TextLocal.inst.get("settings.clearCache"), IconsManager.CLOSE, -2, oneitemheight);
		uiItems[15] = new OptionItem(this, TextLocal.inst.get("settings.reset"), IconsManager.CLOSE, -3, oneitemheight);
		itemsh = 58 + (oneitemheight * uiItems.length);
	}

	public void draw(Graphics g)
	{
		update(g);
		
		int y = 58;
		if(uiItems!=null)
		{
			for (int i=0;i<uiItems.length;i++)
			{
				if(uiItems[i]!=null) {
					uiItems[i].paint(g, y, scrolled);
					y+=uiItems[i].getDrawHeight();
				}
			}
		}
		
		g.translate(0, -g.getTranslateY());
		drawHUD(g, "Настройки");
	}
	
	public final void release(int x, int y)
	{
		try
		{
			if(y > 58 && y < DisplayUtils.height - oneitemheight)
			{
				int h = oneitemheight;
				int yy1 = y - (scrolled + 58);
				int i = yy1 / h;
				if(i < 0)
					i = 0;
				if(!dragging)
				{
					uiItems[i].tap(x, yy1 - (h * i));
				}
			}
				
		}
		catch (ArrayIndexOutOfBoundsException e) 
		{ 
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		super.release(x, y);
	}
	
	public void SettingSet (int setIndex, int var)
	{
		switch(setIndex)
		{
			case 0:
			{
				Settings.animateTransition = var==1;
				break;
			}
			case 1:
			{
				Settings.cacheImages = var==1;
				break;
			}
			case 2:
			{
				Settings.dontLoadAvas = var==1;
				break;
			}
			case 3:
			{
				Settings.https = var==1;
				Settings.proxy = var!=1;
				break;
			}
			case 4:
			{
				Settings.sensorMode = var;
				break;
			}
		}
		Settings.saveSettings();
	}

	public void onMenuItemPress(int i)
	{
		switch(i)
		{
			case -1:
			{
				break;
			}
			case -2:
			{
				break;
			}
			case -3:
			{
				Settings.loadDefaultSettings();
				Settings.saveSettings();
				break;
			}
		}
	}

	public void onMenuItemOption(int i)
	{
		
	}

}
