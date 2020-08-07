package vikatouch.base;

public class Settings
{
	
	public static boolean setted = false;
	
	public static boolean animateTransition;
	
	public static boolean proxy;
	
	public static boolean https;
	
	public static String proxyApi = "http://vk-api-proxy.xtrafrancyz.net:80";
	
	public static String proxyOAuth = "http://vk-oauth-proxy.xtrafrancyz.net:80";
	
	public static int sensorMode;

	public static boolean debugInfo;
	
	public static int simpleListsLength;
	
	public static int messagesPerLoad;
	
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
		animateTransition = false;
		proxy = false;
		https = false;
		debugInfo = false;
		proxyApi = "http://vk-api-proxy.xtrafrancyz.net:80";
		proxyOAuth = "http://vk-oauth-proxy.xtrafrancyz.net:80";
		sensorMode = SENSOR_OK;
		simpleListsLength = 30; // выбор из 10, 30, 50 и 80. Экран потом сделаю.
		messagesPerLoad = 60;
		videoResolution = "480";
	}

}
