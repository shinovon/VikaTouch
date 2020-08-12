package vikatouch.base.settings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

import vikatouch.base.VikaTouch;
import vikatouch.base.local.LangObject;
import vikatouch.base.utils.ErrorCodes;

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

	public static boolean cacheImages;

	public static boolean dontLoadAvas;
	
	public static int refreshRate = 5;

	public static boolean sendErrors;
	
	// На ЗБТ чтоб было включено!!11!1!!
	public static boolean telemetry = false;

	public static final int SENSOR_OK = 0;
	public static final int SENSOR_J2MELOADER = 1;
	public static final int SENSOR_RESISTIVE = 2;

	public static final boolean slideAnim = true;

	static
	{
		loadDefaultSettings();
	}

	public static void loadSettings()
	{
		try
		{
			RecordStore rs = RecordStore.openRecordStore("setts", false);
			if(rs.getNumRecords() > 0)
			{
		        setted = true;
		        
		        final ByteArrayInputStream bais = new ByteArrayInputStream(rs.getRecord(1));
		        final DataInputStream is = new DataInputStream(bais);
		        
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
					RecordStore.deleteRecordStore("setts");
				}
				catch (Exception e)
				{
					
				}
				RecordStore rs = RecordStore.openRecordStore("setts", true);
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
		
		        final byte[] b = baos.toByteArray();
		        rs.addRecord(b, 0, b.length);
		        os.close();
		        baos.close();
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
		platform = System.getProperty("microedition.platform");
		animateTransition = true;
		proxy = false;
		https = false;
		debugInfo = false;
		proxyApi = xtrafrancyzApi;
		proxyOAuth = xtrafrancyzOAuth;
		sensorMode = SENSOR_OK;
		simpleListsLength = 30; // выбор из 10, 30, 50 и 80. Экран потом сделаю.
		messagesPerLoad = 30;
		videoResolution = "480";
		language = "ru_RU";
		cacheImages = true;
		dontLoadAvas = true; // мне тестить надо, а не ждать по 2 минуты пока скачается. И нет, нихера оно не кэшируется. Ещё и скачивается 2 раза.
		sendErrors = true;
	}

}
