package vikatouch.utils.emulatordetect;

import vikatouch.settings.Settings;

public class EmulatorDetector
{
	public static int emulatorType;
	public static boolean isEmulator;
	public static boolean emulatorNotSupported = false;
	
	public static final int EM_KEM = 1;
	
	public static final int EM_KEM_OR_J2L = 17;
	
	public static final int EM_J2L = 16;
	
	public static final int EM_SDK = 4;
	
	public static final int EM_S40_5_SDK = 5;
	
	public static final int EM_S40_6_SDK = 6;
	
	public static final int EM_EPOC_COMPATIBLE = 7;
	
	public static final int EM_MICROEMULATOR = 8;
	
	public static final int EM_MICROEMULATOR_V2 = 9;
	
	public static final int EM_PC = 10;
	
	public static final int EM_UNDEFINED = -1;
	
	public static final int EM_NOT_EMULATOR = 0;

	public static void checkForEmulator(String platform)
	{
		if(platform.indexOf("03.xx") >= 0)
		{
			isEmulator = true;
			emulatorType = EM_SDK;
		}
		else if(platform.equalsIgnoreCase("NOKIA_SERIES60"))
		{
			isEmulator = true;
			emulatorType = EM_KEM;
		}
		else if(platform.equalsIgnoreCase("NOKIA_SERIES40"))
		{
			isEmulator = true;
			emulatorType = EM_KEM;
		}
		else if(platform.equalsIgnoreCase("MicroEmulator-2.0"))
		{
			isEmulator = true;
			emulatorType = EM_MICROEMULATOR_V2;
			emulatorNotSupported = true;
		}
		else if(platform.equalsIgnoreCase("MicroEmulator"))
		{
			isEmulator = true;
			emulatorType = EM_MICROEMULATOR;
			emulatorNotSupported = true;
		}
		else if(platform.indexOf("Emulator") >= 0)
		{
			isEmulator = true;
			emulatorType = EM_UNDEFINED;
		}
		else if(platform.equalsIgnoreCase("j2me"))
		{
			isEmulator = true;
			emulatorType = EM_UNDEFINED;
		}
		else if(platform.indexOf(" ") >= 0)
		{
			isEmulator = true;
			emulatorType = EM_KEM_OR_J2L; 
			// кем так не палится. Вроде.
		}
		else
		{
			isEmulator = false;
			emulatorType = EM_NOT_EMULATOR;
		}
		Settings.setEmulatorSettings();
	}

}
