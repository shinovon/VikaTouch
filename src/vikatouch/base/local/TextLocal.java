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

	private String replace(String str, String from, int to)
	{
		if(to == -1)
		{
			return str;
		}
		return replace(str, from, "" + to);
	}

	private String replace(String str, String from, String to)
	{
		if(to == null)
		{
			return str;
		}
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
	
	public String formatTime(int hour, int minute)
	{
		return format("time", -1, -1, -1, hour, minute);
	}

	public String formatShortDate(int day, int month, int year)
	{
		return format("shortdate", day, month, year, -1, -1);
	}

	public String formatShortDate(int day, int month)
	{
		return format("shortdatenoyear", day, month, -1, -1, -1);
	}

	public String formatChatDate(int day, int month)
	{
		return format("chatdate", day, month, -1, -1, -1);
	}
	
	public String formatChatDate(int day, int month, int year)
	{
		return format("chatdatewyear", day, month, year, -1, -1);
	}

	public String formatFullDate(int day, int month, int year, int hour, int minute)
	{
		return format("date", day, month, year, hour, minute);
	}

	public String formatDate(int day, int month, int year)
	{
		return format("date", day, month, year, -1, -1);
	}

	public String format(String format, int day, int month, int year, int H, int M)
	{
		String result = get("format." + format);
		try
		{
			result = replace(result, "DD", day);
			
			result = replace(result, "SMN", getShortMonth(month));
			
			result = replace(result, "MONTH", getMonth(month));
			
			result = replace(result, "YEAR", year);
			
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
	
	private String getMonthS(int i)
	{
		switch(i)
		{
			case 0:
			{
				return "jan";
			}
			case 1:
			{
				return "feb";
			}
			case 2:
			{
				return "mar";
			}
			case 3:
			{
				return "apr";
			}
			case 4:
			{
				return "may";
			}
			case 5:
			{
				return "jun";
			}
			case 6:
			{
				return "jul";
			}
			case 7:
			{
				return "aug";
			}
			case 8:
			{
				return "sep";
			}
			case 9:
			{
				return "oct";
			}
			case 10:
			{
				return "nov";
			}
			case 11:
			{
				return "dec";
			}
			case -1:
			{
				return null;
			}
			default:
			{
				return "";
			}
		}
	}

	private String getMonth(int month)
	{
		if(month == -1)
		{
			return null;
		}
		return get("date.month." + getMonthS(month));
	}

	private String getShortMonth(int month)
	{
		if(month == -1)
		{
			return null;
		}
		return get("date.shortmonth." + getMonthS(month));
	}

}