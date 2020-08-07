package vikatouch.base;

import vikatouch.base.local.LangObject;

public class Settings
{

	public static boolean setted = false;

	public static boolean animateTransition;

	public static boolean proxy;

	public static boolean https;

	public final static String xtrafrancyzApi = "http://vk-api-proxy.xtrafrancyz.net:80";

	public final static String xtrafrancyzOAuth = "http://vk-oauth-proxy.xtrafrancyz.net:80";

	public final static String httpsApi = "https://api.vk.com:443";

	public final static String httpsOAuth = "https://oauth.vk.com:443";

	public static String proxyApi;

	public static String proxyOAuth;

	public static int sensorMode;

	public static boolean debugInfo;

	public static int simpleListsLength;

	public static int messagesPerLoad;

	public static String language;

	public static String platform;

	public static String videoResolution; // 240 360 480 720

	public static final int SENSOR_OK = 0;
	public static final int SENSOR_J2MELOADER = 1;
	public static final int SENSOR_RESISTIVE = 2;

	static
	{
		loadDefaultSettings();
	}

	public static void loadSettings()
	{

	}

	public static void saveSettings()
	{

	}

	public static void loadDefaultSettings()
	{
		setted = false;
		platform = System.getProperty("microedition.platform");
		animateTransition = false;
		proxy = false;
		https = false;
		debugInfo = false;
		proxyApi = xtrafrancyzApi;
		proxyOAuth = xtrafrancyzOAuth;
		sensorMode = SENSOR_OK;
		simpleListsLength = 30; // выбор из 10, 30, 50 и 80. Экран потом сделаю.
		messagesPerLoad = 60;
		videoResolution = "480";
		language = "ru_RU";
	}

}
