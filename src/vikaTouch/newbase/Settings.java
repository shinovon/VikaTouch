package vikaTouch.newbase;

public class Settings
{
	
	public static boolean setted = false;
	
	public static boolean animateTransition;
	
	public static boolean proxy;
	
	public static boolean https;
	
	public static String proxyApi = "http://vk-api-proxy.xtrafrancyz.net:80";
	
	public static String proxyOAuth = "http://vk-oauth-proxy.xtrafrancyz.net:80";
	
	public static int sensorMode;
	
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
		proxyApi = "http://vk-api-proxy.xtrafrancyz.net:80";
		proxyOAuth = "http://vk-oauth-proxy.xtrafrancyz.net:80";
		sensorMode = SENSOR_OK;
	}

}
