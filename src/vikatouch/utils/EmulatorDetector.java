package vikatouch.utils;

public class EmulatorDetector
{
	private static int emulatorType;
	private static boolean isEmulator;
	
	private static final int EM_KEM = 1;
	
	private static final int EM_KEM_OR_J2L = 17;
	
	private static final int EM_J2L = 16;
	
	private static final int EM_SDK = 4;
	
	private static final int EM_S40_5_SDK = 5;
	
	private static final int EM_S40_6_SDK = 6;
	
	private static final int EM_EPOC_COMPATIBLE = 7;

	public static void checkForEmulator(String platform)
	{
		if(platform.indexOf("03.xx") > 0)
		{
			isEmulator = true;
			emulatorType = EM_SDK;
		}
		else if(platform.indexOf(" ") > 0)
		{
			isEmulator = true;
			emulatorType = EM_KEM_OR_J2L;
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
		else if(platform.equalsIgnoreCase("NOKIA_SERIES40"))
		{
			isEmulator = true;
			emulatorType = EM_KEM;
		}
	}

}
