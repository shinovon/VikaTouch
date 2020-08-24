package vikatouch.settings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import vikamobilebase.VikaUtils;
import vikatouch.VikaTouch;
import vikatouch.local.LangObject;
import vikatouch.utils.ErrorCodes;

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

	public static String videoResolution; // 240 360 480 720

	public static boolean cacheImages;

	public static boolean dontLoadAvas;
	
	public static int refreshRate = 5;

	public static boolean sendErrors;
	
	public static int audioMode = 0; // добавь пж сохранение -ок
	
	public static int rtspMethod = 0;
	
	public static boolean symtube;
	
	public static boolean autoMarkAsRead = true;
	
	// На ЗБТ чтоб было включено!!11!1!!
	public static boolean telemetry = true;

	public static boolean alerts;

	public static boolean dontBack;

	public static boolean isLiteOrSomething;

	public static long memoryClearCache = 400;

	public static final int SENSOR_OK = 0;
	public static final int SENSOR_J2MELOADER = 1;
	public static final int SENSOR_RESISTIVE = 2;
	
	public static final int AUDIO_PLAYONLINE = 0;
	public static final int AUDIO_CACHEANDPLAY = 1;
	public static final int AUDIO_LOADANDPLAY = 2;
	public static final int AUDIO_SYSTEMPLAYER = 3;
	public static final int AUDIO_VLC = 4;
	public static final int AUDIO_DOWNLOAD = 5;
	public static final String[] supportedLanguages = {"en_US", "en_UK", "ru_RU"};

	public static final boolean slideAnim = true;

	static
	{
		loadDefaultSettings();
	}

	public static void loadSettings()
	{
		try
		{
			RecordStore rs = RecordStore.openRecordStore("vikatouchsettings", true);
			if(rs.getNumRecords() > 0)
			{
		        setted = true;
		        
		        final ByteArrayInputStream bais = new ByteArrayInputStream(rs.getRecord(1));
		        final DataInputStream is = new DataInputStream(bais);
		        
		        try
		        {
			        animateTransition = is.readBoolean();
			        proxy = is.readBoolean();
			        https = is.readBoolean();
			        debugInfo  = is.readBoolean();
			        proxyApi = is.readUTF();
			        proxyOAuth = is.readUTF();
			        sensorMode = is.readShort();
			        simpleListsLength = is.readShort();
			        messagesPerLoad = is.readShort();
			        videoResolution = is.readUTF();
			        language = is.readUTF();
		        	dontLoadAvas = is.readBoolean();
		        	audioMode = is.readShort();
		        	rtspMethod = is.readShort();
		        	symtube = is.readBoolean();
		        	refreshRate = is.readShort();
		        }
		        catch (Exception e)
		        {
		        	
		        }
		        is.close();
		        bais.close();
			}
			rs.closeRecordStore();
		}
		catch (Exception e)
		{
			
		}
	}

	public static void saveSettings()
	{
		if(setted)
		{
			try
			{
				try
				{
					RecordStore.deleteRecordStore("vikatouchsettings");
				}
				catch (Exception e)
				{
					
				}
				RecordStore rs = RecordStore.openRecordStore("vikatouchsettings", true);
		        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        final DataOutputStream os = new DataOutputStream(baos);
		        
		        os.writeBoolean(animateTransition);
		        os.writeBoolean(proxy);
		        os.writeBoolean(https);
		        os.writeBoolean(debugInfo);
		        os.writeUTF(proxyApi);
		        os.writeUTF(proxyOAuth);
		        os.writeShort(sensorMode);
		        os.writeShort(simpleListsLength);
		        os.writeShort(messagesPerLoad);
		        os.writeUTF(videoResolution);
		        os.writeUTF(language);
		        os.writeBoolean(dontLoadAvas);
		        os.writeShort(audioMode);
		        os.writeShort(rtspMethod);
		        os.writeBoolean(symtube);
		        os.writeShort(refreshRate);
		
		        final byte[] b = baos.toByteArray();
		        rs.addRecord(b, 0, b.length);
		        os.close();
		        baos.close();
		        rs.closeRecordStore();
			}
			catch (Exception e)
			{
				VikaTouch.error(e, ErrorCodes.SETSSAVE);
			}
		}
	}

	public static void loadDefaultSettings()
	{
		setted = false;
		animateTransition = false;
		proxy = false;
		https = false;
		debugInfo = false;
		proxyApi = xtrafrancyzApi;
		proxyOAuth = xtrafrancyzOAuth;
		sensorMode = SENSOR_OK;
		simpleListsLength = 30;
		messagesPerLoad = 30;
		videoResolution = "480";
		language = "ru_RU";
		cacheImages = true;
		dontLoadAvas = false;
		sendErrors = true;
		autoMarkAsRead = true;
		isLiteOrSomething = VikaTouch.isS40();
		dontBack = isLiteOrSomething;
		
		//язык соотвествующий настройкам устройства
		try
		{
			final String lang = Settings.hasLanguage(System.getProperty("microedition.locale"));
			if(lang != null)
			{
				Settings.language = lang;
			}
		}
		catch (Exception e)
		{
			
		}
		
		if(isLiteOrSomething)
		{
			alerts = true;
			videoResolution = "240";
			proxy = true;
		}
	}

	public static String hasLanguage(String l)
	{
		for(int i = 0; i < supportedLanguages.length; i++)
		{
			if(supportedLanguages[i].equalsIgnoreCase(VikaUtils.replace(l, "-", "_")))
			{
				return supportedLanguages[i];
			}
		}
		return "en_US";
	}

}
