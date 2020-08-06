package vikatouch.base;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

import org.json.me.JSONObject;

import ru.nnproject.vikatouch.VikaTouchApp;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.UIThread;
import ru.nnproject.vikaui.VikaScreen;
import vikamobilebase.ImageStorage;
import vikamobilebase.VikaUtils;
import vikatouch.screens.AboutScreen;
import vikatouch.screens.CaptchaScreen;
import vikatouch.screens.ChatScreen;
import vikatouch.screens.DialogsScreen;
import vikatouch.screens.GroupPageScreen;
import vikatouch.screens.LoginScreen;
import vikatouch.screens.MainScreen;
import vikatouch.screens.NewsScreen;
import vikatouch.screens.ReturnableListScreen;
import vikatouch.screens.menu.DocsScreen;
import vikatouch.screens.menu.FriendsScreen;
import vikatouch.screens.menu.GroupsScreen;
import vikatouch.screens.menu.MenuScreen;
import vikatouch.screens.menu.PhotosScreen;
import vikatouch.screens.menu.VideosScreen;

public class VikaTouch
{

	public static final boolean DEMO_MODE = false;
	public static final String API_VERSION = "5.122";
	public static final String TOKEN_RMS = "vikatouchtoken";
	public static final int INDEX_FALSE = -1;
	public static String API = "http://vk-api-proxy.xtrafrancyz.net:80";
	public static String OAUTH = "https://oauth.vk.com:443";
	public static String accessToken;
	public static String mobilePlatform;
	public static LoginScreen login;
	public static MenuScreen menuCanv;
	public static DialogsScreen dialogsCanv;
	public static DocsScreen docsCanv;
	public static GroupsScreen grCanv;
	public static VikaScreen videosCanv;
	public static FriendsScreen friendsCanv;
	public static NewsScreen newsCanv;
	public static PhotosScreen photosCanv;
	public static CaptchaScreen captchaCanv;
	public static RecordStore tokenRMS;
	public static Image cameraImg;
	public static Thread mainThread;
	public static UIThread uiThread;
	public static String userId;
	public static short unreadCount = -1;
	public static boolean offlineMode;
	public static boolean loading;
	public static AboutScreen about;
	public static VikaCanvasInst canvas;
	public CommandsImpl cmdsInst;
	private String errReason;
	private String tokenUnswer;
	public static VikaTouch inst;
	public static VikaTouchApp appInst;
	
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
			String s = accessToken + ";" + userId + ";" + MenuScreen.name + " " + MenuScreen.lastname + ";" + MenuScreen.avaurl;
			tokenRMS.addRecord(s.getBytes("UTF-8"), 0, s.length());
		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.TOKENSAVE);
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
				MenuScreen.avaurl = s3.substring(s3.indexOf(";")+1, s3.length());
				MenuScreen.hasAva = true;
				String name = s3.substring(0, s3.indexOf(";"));
				MenuScreen.name = name.substring(0, name.indexOf(" "));
				MenuScreen.lastname = name.substring(name.indexOf(" ")+1, name.length());
				userId = s2.substring(0, s2.indexOf(";"));
				tokenRMS.closeRecordStore();
				return true;
			}
			tokenRMS.closeRecordStore();
		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.TOKENLOAD);
		}
		return false;
	}

	public static void setDisplay(VikaScreen s)
	{
		appInst.isPaused = false;
		if(s instanceof MenuScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_MENU;
			MainScreen.lastMenu = DisplayUtils.CANVAS_MENU; 
		}
		if(s instanceof NewsScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_NEWS;
		}
		if(s instanceof DialogsScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_CHATSLIST;
		}
		if(s instanceof AboutScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_ABOUT;
		}
		if(s instanceof LoginScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_LOGIN;
		}
		if(s instanceof ChatScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_CHAT;
		}
		if(s instanceof ReturnableListScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_TEMPLIST;
		}
		if(s instanceof GroupPageScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_TEMPLIST;
			canvas.lastTempScreen = s;
		}
		if(s instanceof DocsScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_DOCSLIST;
			MainScreen.lastMenu = DisplayUtils.CANVAS_DOCSLIST; 
		}
		if(s instanceof GroupsScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_GROUPSLIST;
			MainScreen.lastMenu = DisplayUtils.CANVAS_GROUPSLIST; 
		}
		if(s instanceof FriendsScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_FRIENDSLIST;
			MainScreen.lastMenu = DisplayUtils.CANVAS_FRIENDSLIST; 
		}
		if(s instanceof PhotosScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_PHOTOSLIST;
			MainScreen.lastMenu = DisplayUtils.CANVAS_PHOTOSLIST; 
		}
		if(s instanceof VideosScreen)
		{
			DisplayUtils.current = DisplayUtils.CANVAS_VIDEOSLIST;
			MainScreen.lastMenu = DisplayUtils.CANVAS_VIDEOSLIST; 
		}
		canvas.currentScreen = s;
		canvas.paint();
		DisplayUtils.checkdisplay();
		loading = false;
	}

	public static boolean isPaused()
	{
		return appInst.isPaused;
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
				if(tokenUnswer == null)
				{
					errReason = "Network error on token getting";
					return false;
				}
				System.out.println(tokenUnswer);
				errReason = tokenUnswer;
				if(tokenUnswer.indexOf("need_captcha") > 0)
				{
					captchaCanv = new CaptchaScreen();
					captchaCanv.obj = new CaptchaObject(new JSONObject(tokenUnswer));
					captchaCanv.obj.parseJSON();
					canvas.showCaptcha = true;
					while(appInst.started)
					{
						if(captchaCanv != null && CaptchaScreen.finished)
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
								.addField("key", CaptchaScreen.input)
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
								final VikaScreen canvas = menuCanv = new MenuScreen();
								setDisplay(canvas);
								saveToken();
								Dialogs.refreshDialogsList();
								CaptchaScreen.finished = false;
								return true;
							}
							else
							{
								errReason = "failed auth with captcha";
							}
							CaptchaScreen.finished = false;
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
						final VikaScreen canvas = menuCanv = new MenuScreen();
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
			catch (NullPointerException e)
			{
				errReason = "no internet: "+e.toString();
				e.printStackTrace();
				return false;
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

	public static void warn(String string)
	{
		final Alert alert = new Alert("Внимание!", string, null, AlertType.WARNING);
		alert.addCommand(Alert.DISMISS_COMMAND);
		setDisplay(alert);
	}

	public static Displayable getCurrentDisplay()
	{
		return Display.getDisplay(appInst).getCurrent();
	}

	public static String getVersion()
	{
		return appInst.getAppProperty("MIDlet-Version");
	}

	public static void setDisplay(Displayable d)
	{
		Display.getDisplay(appInst).setCurrent(d);
	}

	public static void error(int i, boolean fatal)
	{
		inst.errReason = "errorcode" + i;
		final Alert alert = new Alert("Ошибка!", "Код ошибки: " + i + "\nобратитесь к разработчику за помощью.", null, AlertType.ERROR);
		if(fatal)
		{
			alert.setCommandListener(inst.cmdsInst);
			alert.addCommand(CommandsImpl.close);
		}
		else
		{
			alert.addCommand(Alert.DISMISS_COMMAND);
		}
		setDisplay(alert);
	}

	public static void error(Throwable e, int i)
	{
		inst.errReason = e.toString();
		final Alert alert = new Alert("Ошибка!", "Исключение: \n" + e.toString() + "\nКод ошибки: " + i + "\nобратитесь к разработчику за помощью.", null, AlertType.ERROR);
		final boolean fatal = e instanceof IOException || e instanceof NullPointerException || e instanceof OutOfMemoryError;
		if(fatal)
		{
			alert.setCommandListener(inst.cmdsInst);
			alert.addCommand(CommandsImpl.close);
		}
		else
		{
			alert.addCommand(Alert.DISMISS_COMMAND);
		}
		setDisplay(alert);
	}

	public static void error(Throwable e, String s)
	{
		System.out.println(s);
		inst.errReason = e.toString();
		final Alert alert = new Alert("Ошибка!", "Необработанное исключение: \n" + e.toString() + "\nВозможное описание: " + s, null, AlertType.ERROR);
		final boolean fatal = e instanceof IOException || e instanceof NullPointerException || e instanceof OutOfMemoryError;
		if(fatal)
		{
			alert.setCommandListener(inst.cmdsInst);
			alert.addCommand(CommandsImpl.close);
		}
		else
		{
			alert.addCommand(Alert.DISMISS_COMMAND);
		}
		setDisplay(alert);
	}

	public static void error(String s, boolean fatal)
	{
		inst.errReason = s;
		final Alert alert = new Alert("Ошибка!", s, null, AlertType.ERROR);
		if(fatal)
		{
			alert.setCommandListener(inst.cmdsInst);
			alert.addCommand(CommandsImpl.close);
		}
		else
		{
			alert.addCommand(Alert.DISMISS_COMMAND);
		}
		setDisplay(alert);
	}

	public void start()
	{
		loading = true;
		mainThread = new Thread(appInst);
		mainThread.start();
		canvas = new VikaCanvasInst();
		DisplayUtils.checkdisplay();
		setDisplay(canvas);
		uiThread = new UIThread(canvas);
		uiThread.start();
		DisplayUtils.checkdisplay();
	}

	public void threadRun()
	{
		ImageStorage.init();
		try {
			IconsManager.Load();
		} catch (Exception e) { System.out.println("Сбой иконок"); e.printStackTrace(); }
		cmdsInst = new CommandsImpl();	

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
					OAUTH = Settings.proxyOAuth;
					API = Settings.proxyApi;
					Settings.proxy = true;
				}
				else
				{
					OAUTH = Settings.proxyOAuth;
					API = Settings.proxyApi;
					Settings.proxy = true;
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
			OAUTH = Settings.proxyOAuth;
			API = Settings.proxyApi;
			Settings.proxy = true;
		}
		
		try
		{
			cameraImg = ResizeUtils.resizeava(Image.createImage("/camera.png"));
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
				canvas = menuCanv = new MenuScreen();
			}
			else
			{
				canvas = login = new LoginScreen();
			}
			setDisplay(canvas);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Thread.yield();
		
	}
}
