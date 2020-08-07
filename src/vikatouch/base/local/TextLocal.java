package vikatouch.base.local;

import java.util.Hashtable;

public class TextLocal
{
	public static TextLocal inst;
	private static Hashtable hashtable;

	public static void init()
	{
		hashtable = new Hashtable();
	}
	
	public static void loadLanguage(LangObject lang)
	{
		loadLanguage(lang);
	}
	
	public static void loadLanguage(String lang)
	{
		
	}
	
	public static String get(String key)
	{
		if(hashtable.containsKey(key))
		{
			return (String) hashtable.get(key);
		}
		else
		{
			return key;
		}
	}

}
