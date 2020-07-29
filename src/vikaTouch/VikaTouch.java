package vikaTouch;

import java.io.*;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import org.json.me.JSONObject;

import vikaTouch.base.ImageStorage;
import vikaTouch.base.VikaUtils;
import vikaTouch.canvas.*;
import javax.microedition.lcdui.game.GameCanvas;

import vikaTouch.newbase.CaptchaObject;
import vikaTouch.newbase.Commands;
import vikaTouch.newbase.Dialogs;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.UIThread;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.VikaCanvas;
import vikaTouch.newbase.VikaScreen;

public final class VikaTouch
	extends MIDlet
	implements Runnable
{
	public static final boolean DEMO_MODE = false;
	public static final String API_VERSION = "5.120";
	public static final String TOKEN_RMS = "vikatouchtoken";
	public static final int INDEX_FALSE = -1;
	public static String API = "http://vk-api-proxy.xtrafrancyz.net:80";
	public static String OAUTH = "https://oauth.vk.com:443";
	public static String accessToken;
	public static String mobilePlatform;
	public static VikaTouch inst;
	public static LoginCanvas login;
	public static MenuCanvas menuCanv;
	public static DialogsCanvas dialogsCanv;
	public static DocsCanvas docsCanv;
	public static NewsCanvas newsCanv;
	public static CaptchaCanvas captchaCanv;
	public static RecordStore tokenRMS;
	public static Image cameraImg;
	public static Thread mainThread;
	public static UIThread uiThread;
	public static String userId;
	public static int has = -1;
	public static boolean offlineMode;
	public static boolean loading;
	public static AboutCanvas about;
	public static VikaCanvas canvas;
	public boolean isPaused;
	public Commands cmdsInst;
	private String errReason;
	private String tokenUnswer;
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
		mobilePlatform = System.getProperty("microedition.platform");
		isPaused = false;
		
		if(!started)
		{
			started = true;
			inst = this;
			loading = true;
			mainThread = new Thread(this);
			mainThread.start();
			canvas = new VikaCanvas();
			setDisplay(canvas);
			uiThread = new UIThread();
			uiThread.start();
			DisplayUtils.checkdisplay();
		}
	}
	
	private void saveToken()
	{
		try
		{
			try
			{
				if(tokenRMS != null)
					tokenRMS.closeRecordStore();
				RecordStore.deleteRecordStore(TOKEN_RMS);
			}
			catch(Exception e)
			{
				
			}
			tokenRMS = RecordStore.openRecordStore(TOKEN_RMS, true);
			String s = accessToken + ";" + userId + ";" + MenuCanvas.name + " " + MenuCanvas.lastname + ";" + MenuCanvas.avaurl;
			tokenRMS.addRecord(s.getBytes("UTF-8"), 0, s.length());
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Сохранение токена");
		}
	}
	
	private boolean getToken()
	{
		try
		{
			tokenRMS = RecordStore.openRecordStore(TOKEN_RMS, true);
			if(tokenRMS.getNumRecords() > 0)
			{
				String s = new String(tokenRMS.getRecord(1), "UTF-8");
				accessToken = s.substring(0, s.indexOf(";"));
				
				//Вся эта хрень нужна для запуска в оффлайне
				String s2 = s.substring(s.indexOf(";")+1, s.length());
				String s3 = s2.substring(s2.indexOf(";")+1, s2.length());
				MenuCanvas.avaurl = s3.substring(s3.indexOf(";")+1, s3.length());
				MenuCanvas.hasAva = true;
				String name = s3.substring(0, s3.indexOf(";"));
				MenuCanvas.name = name.substring(0, name.indexOf(" "));
				MenuCanvas.lastname = name.substring(name.indexOf(" ")+1, name.length());
				userId = s2.substring(0, s2.indexOf(";"));
				tokenRMS.closeRecordStore();
				return true;
			}
			tokenRMS.closeRecordStore();
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Получение токена из сохранений");
		}
		return false;
	}

	public static void setDisplay(VikaScreen s)
	{
		inst.isPaused = false;
		if(s instanceof MenuCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_MENU;
			MainCanvas.lastMenu = DisplayUtils.CANVAS_MENU; 
		}
		if(s instanceof NewsCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_NEWS;
		}
		if(s instanceof DialogsCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_CHATSLIST;
		}
		if(s instanceof AboutCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_ABOUT;
		}
		if(s instanceof LoginCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_LOGIN;
		}
		if(s instanceof DialogCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_CHAT;
		}
		if(s instanceof ReturnableListCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_TEMPLIST;
		}
		if(s instanceof DocsCanvas)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_DOCSLIST;
			MainCanvas.lastMenu = DisplayUtils.CANVAS_DOCSLIST; 
		}
		canvas.currentScreen = s;
		canvas.paint();
		loading = false;
	}

	public static boolean isPaused()
	{
		return inst.isPaused;
	}

	public boolean login(final String user, final String pass)
	{
		String refreshToken;
		if (user.length() > 0)
		{
			try
			{
				tokenUnswer = VikaUtils.download(
					new URLBuilder(OAUTH, "token")
					.addField("grant_type", "password")
					.addField("client_id", "2685278")
					.addField("client_secret", "lxhD8OD7dMsqtXIm5IUY")
					.addField("username", user)
					.addField("password", pass)
					.addField("scope", "notify,friends,photos,audio,video,docs,notes,pages,status,offers,questions,wall,groups,messages,notifications,stats,ads,offline")
					.toString()
				);
				if(tokenUnswer == null) {
					errReason = "Network error on token getting";
					return false;
				}
				errReason = tokenUnswer;
				if(tokenUnswer.indexOf("need_captcha") > 0)
				{
					captchaCanv = new CaptchaCanvas();
					captchaCanv.obj = new CaptchaObject(new JSONObject(tokenUnswer));
					captchaCanv.obj.parseJSON();
					canvas.showCaptcha = true;
					while(started)
					{
						if(captchaCanv != null && CaptchaCanvas.finished)
						{
							tokenUnswer = VikaUtils.download(
							new URLBuilder(OAUTH, "token")
								.addField("grant_type", "password")
								.addField("client_id", "2685278")
								.addField("client_secret", "lxhD8OD7dMsqtXIm5IUY")
								.addField("username", user)
								.addField("password", pass)
								.addField("scope", "notify,friends,photos,audio,video,docs,notes,pages,status,offers,questions,wall,groups,messages,notifications,stats,ads,offline")
								.addField("captcha_sid", captchaCanv.obj.captchasid)
								.addField("key", CaptchaCanvas.input)
								.toString()
							);

							accessToken = tokenUnswer.substring(tokenUnswer.indexOf("access_token") + 15,
									tokenUnswer.indexOf("expires_in") - 3);
							userId = tokenUnswer.substring(tokenUnswer.indexOf("user_id") + 9,
									tokenUnswer.indexOf("}") - 0);
							VikaUtils.download(URLBuilder.makeSimpleURL("audio.get"));
							String var5 = ":APA91bFAM-gVwLCkCABy5DJPPRH5TNDHW9xcGu_OLhmdUSA8zuUsBiU_DexHrTLLZWtzWHZTT5QUaVkBk_GJVQyCE_yQj9UId3pU3vxvizffCPQISmh2k93Fs7XH1qPbDvezEiMyeuLDXb5ebOVGehtbdk_9u5pwUw";
							if ((refreshToken = VikaUtils.download(new URLBuilder("auth.refreshToken").addField("receipt", var5).toString())).indexOf("method") == INDEX_FALSE) {
								accessToken = refreshToken.substring(refreshToken.indexOf("access_token") + 23, refreshToken.length() - 3);
								tokenUnswer = "{\"access_token\":\"" + accessToken + "\",\"expires_in\":0,\"user_id\":"
										+ userId + "}";
								final VikaScreen canvas = menuCanv = new MenuCanvas();
								setDisplay(canvas);
								saveToken();
								Dialogs.refreshDialogsList();
								CaptchaCanvas.finished = false;
								return true;
							}
							else
							{
								errReason = "failed auth with captcha";
							}
							CaptchaCanvas.finished = false;
							break;
						}
					}
				}
				else
				{
					accessToken = tokenUnswer.substring(tokenUnswer.indexOf("access_token") + 15, tokenUnswer.indexOf("expires_in") - 3);
					userId = tokenUnswer.substring(tokenUnswer.indexOf("user_id") + 9, tokenUnswer.indexOf("}") - 0);
					VikaUtils.download(URLBuilder.makeSimpleURL("audio.get"));
					String var5 = ":APA91bFAM-gVwLCkCABy5DJPPRH5TNDHW9xcGu_OLhmdUSA8zuUsBiU_DexHrTLLZWtzWHZTT5QUaVkBk_GJVQyCE_yQj9UId3pU3vxvizffCPQISmh2k93Fs7XH1qPbDvezEiMyeuLDXb5ebOVGehtbdk_9u5pwUw";
					if ((refreshToken = VikaUtils.download(new URLBuilder("auth.refreshToken").addField("receipt", var5).toString())).indexOf("method") == INDEX_FALSE) {
						accessToken = refreshToken.substring(refreshToken.indexOf("access_token") + 23, refreshToken.length() - 3);
						tokenUnswer = "{\"access_token\":\"" + accessToken + "\",\"expires_in\":0,\"user_id\":"
								+ userId + "}";
						final VikaScreen canvas = menuCanv = new MenuCanvas();
						setDisplay(canvas);
						saveToken();
						Dialogs.refreshDialogsList();
						return true;
					}
					else
					{
						errReason = "failed auth";
					}
				}
			}
			catch (Exception e)
			{
				errReason = e.toString();
				return false;
			}
		}
		else
		{
			errReason = "login is invalid";
		}
		return false;
	}

	public static String getReason()
	{
		final String x = inst.errReason;
		inst.errReason = null;
		return x;
	}

	public static void error(String s, boolean fatal)
	{
		inst.errReason = s;
		final Alert alert = new Alert("Ошибка!", s, null, AlertType.ERROR);
		if(fatal)
		{
			alert.setCommandListener(inst.cmdsInst);
			alert.addCommand(Commands.close);
		}
		else
		{
			alert.addCommand(Alert.DISMISS_COMMAND);
		}
		setDisplay(alert);
	}

	public static void error(Throwable e, String s)
	{
		inst.errReason = e.toString();
		final Alert alert = new Alert("Ошибка!", "Необработанное исключение: \n" + e.toString() + "\nВозможное описание: " + s, null, AlertType.ERROR);
		final boolean fatal = e instanceof IOException || e instanceof NullPointerException;
		if(fatal)
		{
			alert.setCommandListener(inst.cmdsInst);
			alert.addCommand(Commands.close);
		}
		else
		{
			alert.addCommand(Alert.DISMISS_COMMAND);
		}
		setDisplay(alert);
	}

	public void run()
	{
		ImageStorage.init();
		
		cmdsInst = new Commands();	

		//Выбор сервера 
		if (mobilePlatform.indexOf("S60") > 0)
		{
			if (mobilePlatform.indexOf("5.3") == INDEX_FALSE && mobilePlatform.indexOf("5.2") == INDEX_FALSE && mobilePlatform.indexOf("5.1") == INDEX_FALSE && mobilePlatform.indexOf("5.0") == INDEX_FALSE)
			{
				if (mobilePlatform.indexOf("3.2") > 0)
				{
					OAUTH = "https://oauth.vk.com:443";
					API = "https://api.vk.com:443";
				}
				else if (mobilePlatform.indexOf("3.1") > 0)
				{
					OAUTH = "http://vk-oauth-proxy.xtrafrancyz.net:80";
					API = "http://vk-api-proxy.xtrafrancyz.net:80";
				}
				else
				{
					OAUTH = "http://vk-oauth-proxy.xtrafrancyz.net:80";
					API = "http://vk-api-proxy.xtrafrancyz.net:80";
				}
			}
			else
			{
				OAUTH = "https://oauth.vk.com:443";
				API = "https://api.vk.com:443";
			}
		
		}
		else
		{
			OAUTH = "http://vk-oauth-proxy.xtrafrancyz.net:80";
			API = "http://vk-api-proxy.xtrafrancyz.net:80";
		}
		
		try
		{
			cameraImg = DisplayUtils.resizeava(Image.createImage("/camera.png"));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		/*
		LoadingSplash splash = new LoadingSplash("/splash.png", "/indicator2.gif");
		splash.action = true;
		LoadingSplash.showloadingbar = true;
		setDisplay(splash);
		splash.startthread();
		*/
		try
		{
			
			final VikaScreen canvas;
			if(DEMO_MODE || getToken())
			{
				if(accessToken != "")
				{
					if(userId == null || userId == "")
					{
						final JSONObject jo = new JSONObject(VikaUtils.download(new URLBuilder("account.getProfileInfo"))).getJSONObject("response");
						userId = "" + jo.optInt("id");
					}
					if(!offlineMode)
						Dialogs.refreshDialogsList();
				}
				canvas = menuCanv = new MenuCanvas();
			}
			else
			{
				canvas = login = new LoginCanvas();
			}
			setDisplay(canvas);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Thread.yield();
	}

	public static void warn(String string)
	{
		final Alert alert = new Alert("Внимание!", string, null, AlertType.WARNING);
		alert.addCommand(Alert.DISMISS_COMMAND);
		setDisplay(alert);
	}

	public static Displayable getCurrentDisplay()
	{
		return Display.getDisplay(inst).getCurrent();
	}

	public static String getVersion()
	{
		return inst.getAppProperty("MIDlet-Version");
	}

	public static void setDisplay(Displayable d)
	{
		Display.getDisplay(inst).setCurrent(d);
	}
}