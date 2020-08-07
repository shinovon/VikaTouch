package vikatouch.base.local;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import vikamobilebase.VikaUtils;
import vikatouch.base.ErrorCodes;
import vikatouch.base.Settings;
import vikatouch.base.VikaTouch;

public class TextLocal
{
	public static TextLocal inst;
	private Hashtable hashtable;

	/**
	 *  Вызывать только после загрузки настроек!!
	 */
	public static void init()
	{
		inst = new TextLocal();
	}
	
	private TextLocal()
	{
		hashtable = new Hashtable();
		loadLanguage(Settings.language);
	}
	
	public void loadLanguage(LangObject lang)
	{
		loadLanguage(lang.shortName);
	}
	
	public void loadLanguage(String lang)
	{
		try
		{
			char[] chars = new char[16000];
			InputStreamReader isr = new InputStreamReader(this.getClass().getResourceAsStream("/local/" + lang + ".txt"),"UTF-8");
			isr.read(chars);
			String x = "";
			boolean iscomment = false;
			for(int i = 0; i < chars.length; i++)
			{
				final char c = chars[i];
				
				if(c == '#')
				{
					iscomment = true;	
				}
				
				if(c == '\n')
				{
					if(!iscomment && x != null && x.length() > 2)
					{
						int splitLoc = x.indexOf("=");
						int len = x.length();
						String key = x.substring(0, splitLoc);
						String val = VikaUtils.replace(x.substring(splitLoc + 1, len), "|", "\n");
						hashtable.put(key, val);
						System.out.println(key + "=" + val);
						System.out.println();
					}
					iscomment = false;
					x = "";
				}
				else
					x += String.valueOf(c);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			VikaTouch.error(e, ErrorCodes.LANGLOAD);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			VikaTouch.error(e, ErrorCodes.LANGLOAD);
		}
	}
	
	public String get(String key)
	{
		try
		{
			if(hashtable.containsKey(key))
			{
				return (String) hashtable.get(key);
			}
		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.LANGGET);
		}
		return key;
	}
	
	public String formatTime(int H, int M)
	{
		String result = H + ":" + M;
		try
		{
			String format = get("format.time");
			
			String h = "" + H;
			
			String HH = "" + (H < 10 ? "0" + H : "" + H);
			
			String TT = "AM";
			
			String MM = "" + (M < 10 ? "0" + M : "" + M);
			
			if(H == 0)
			{
				h = "" + 12;
				TT = "PM";
			}
			else if(H == 13)
			{
				h = "" + 1;
				TT = "PM";
			}
			else if(H > 13)
			{
				h = "" + H % 12;
				TT = "PM";
			}
			
			String tt = TT.toLowerCase();
			
			result = format;
			result = replace(result, "tt", tt);
			result = replace(result, "TT", TT);
			result = replace(result, "h", h);
			result = replace(result, "H", H);
			result = replace(result, "HH", HH);
			result = replace(result, "M", M);
			result = replace(result, "MM", MM);
		}
		catch (Exception e)
		{
			
		}
		
		return result;
	}

	private String replace(String str, String from, int to)
	{
		return replace(str, from, "" + to);
	}

	private String replace(String str, String from, String to)
	{
		try
		{
			String result = str;
			int j = result.indexOf(from);
			int k = 0;
	
			for (int i = from.length(); j != -1; j = str.indexOf(from, k))
			{
				result += str.substring(k, j) + to;
				k = j + i;
			}
	
			result += str.substring(k, str.length());
			return result;
		}
		catch (Exception e)
		{
			return str;
		}
	}

}
