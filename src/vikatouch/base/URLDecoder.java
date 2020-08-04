package vikatouch.base;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public final class URLDecoder
{
	
	private static Hashtable dict;

	static
	{
		dict = new Hashtable();
		dict.put(" ", "%20");
		dict.put("а", "%D0%B0");
		dict.put("б", "%D0%B1");
		dict.put("в", "%D0%B2");
		dict.put("г", "%D0%B3");
		dict.put("д", "%D0%B4");
		dict.put("е", "%D0%B5");
		dict.put("ж", "%D0%B6");
		dict.put("з", "%D0%B7");
		dict.put("и", "%D0%B8");
		dict.put("й", "%D0%B9");
		dict.put("к", "%D0%Ba");
		dict.put("л", "%D0%Bb");
		dict.put("м", "%D0%Bc");
		dict.put("н", "%D0%Bd");
		dict.put("о", "%D0%Be");
		dict.put("п", "%D0%Bf");
		dict.put("р", "%D1%80");
		dict.put("с", "%D1%81");
		dict.put("т", "%D1%82");
		dict.put("у", "%D1%83");
		dict.put("ф", "%D1%84");
		dict.put("х", "%D1%85");
		dict.put("ц", "%D1%86");
		dict.put("ч", "%D1%87");
		dict.put("ш", "%D1%88");
		dict.put("щ", "%D1%89");
		dict.put("ъ", "%D1%8a");
		dict.put("ы", "%D1%8b");
		dict.put("ь", "%D1%8c");
		dict.put("э", "%D1%8d");
		dict.put("ю", "%D1%8e");
		dict.put("я", "%D1%8f");
		dict.put("А", "%D0%90");
		dict.put("Б", "%D0%91");
		dict.put("В", "%D0%92");
		dict.put("Г", "%D0%93");
		dict.put("Д", "%D0%94");
		dict.put("Е", "%D0%95");
		dict.put("Ж", "%D0%96");
		dict.put("З", "%D0%97");
		dict.put("И", "%D0%98");
		dict.put("Й", "%D0%99");
		dict.put("К", "%D0%9a");
		dict.put("Л", "%D0%9b");
		dict.put("М", "%D0%9c");
		dict.put("Н", "%D0%9d");
		dict.put("О", "%D0%9e");
		dict.put("П", "%D0%9f");
		dict.put("Р", "%D0%a0");
		dict.put("С", "%D0%a1");
		dict.put("Т", "%D0%a2");
		dict.put("У", "%D0%a3");
		dict.put("Ф", "%D0%a4");
		dict.put("Х", "%D0%a5");
		dict.put("Ц", "%D0%a6");
		dict.put("Ч", "%D0%a7");
		dict.put("Ш", "%D0%a8");
		dict.put("Щ", "%D0%a9");
		dict.put("Ъ", "%D0%aa");
		dict.put("Ы", "%D0%ab");
		dict.put("Ь", "%D0%ac");
		dict.put("Э", "%D0%ad");
		dict.put("Ю", "%D0%ae");
		dict.put("Я", "%D0%af");
		dict.put("ё", "%D1%91");
		dict.put("Ё", "%D0%81");
		dict.put("«", "\"");
		dict.put("»", "\"");
		dict.put("-", "-");
		dict.put("_", "_");
		dict.put("—", "-");
		dict.put("−", "-");
		dict.put("‒", "-");
		dict.put("⁃", "-");
		dict.put("–", "-");
		dict.put("―", "-");
		dict.put("\n", "%0A");
		dict.put("0", "0");
		dict.put("1", "1");
		dict.put("2", "2");
		dict.put("3", "3");
		dict.put("4", "4");
		dict.put("5", "5");
		dict.put("6", "6");
		dict.put("7", "7");
		dict.put("8", "8");
		dict.put("9", "9");
		dict.put("a", "a");
		dict.put("b", "b");
		dict.put("c", "c");
		dict.put("d", "d");
		dict.put("e", "e");
		dict.put("f", "f");
		dict.put("g", "g");
		dict.put("h", "h");
		dict.put("i", "i");
		dict.put("j", "j");
		dict.put("k", "k");
		dict.put("l", "l");
		dict.put("m", "m");
		dict.put("n", "n");
		dict.put("o", "o");
		dict.put("p", "p");
		dict.put("q", "q");
		dict.put("r", "r");
		dict.put("s", "s");
		dict.put("t", "t");
		dict.put("u", "u");
		dict.put("v", "v");
		dict.put("w", "w");
		dict.put("x", "x");
		dict.put("y", "y");
		dict.put("z", "z");
		dict.put("A", "A");
		dict.put("B", "B");
		dict.put("C", "C");
		dict.put("D", "D");
		dict.put("E", "E");
		dict.put("F", "F");
		dict.put("G", "G");
		dict.put("H", "H");
		dict.put("I", "I");
		dict.put("G", "G");
		dict.put("K", "K");
		dict.put("L", "L");
		dict.put("M", "M");
		dict.put("N", "N");
		dict.put("O", "O");
		dict.put("P", "P");
		dict.put("Q", "Q");
		dict.put("R", "R");
		dict.put("S", "S");
		dict.put("T", "T");
		dict.put("U", "U");
		dict.put("V", "V");
		dict.put("W", "W");
		dict.put("X", "X");
		dict.put("Y", "Y");
		dict.put("Z", "Z");
		dict.put("?", "%3F");
		dict.put(".", ".");
		dict.put(",", "%2C");
		dict.put("!", "%21");
		dict.put("\"", "%22");
		dict.put("¿", "%C2%BF");
		dict.put("<", "%3C");
		dict.put(">", "%3E");
		dict.put("/", "%2F");
		dict.put("@", "%40");
		dict.put("/", "%2F");
		dict.put("#", "%23");
		dict.put("№", "%E2%84%96");
		dict.put("$", "%24");
		dict.put(";", "%3B");
		dict.put("^", "%5E");
		dict.put(":", "%3A");
		dict.put("&", "%26");
		dict.put("*", "%2A");
		dict.put("(", "%28");
		dict.put(")", "%29");
		dict.put("+", "%2B");
		dict.put("[", "%5B");
		dict.put("]", "%5D");
		dict.put("{", "%7B");
		dict.put("}", "%7D");
		dict.put("~", "~");
		dict.put("`", "%60");
		
		dict.put("¡", "%C2%A1");
		dict.put("Є", "%D0%84");
		dict.put("є", "%D1%94");
		dict.put("Ґ", "%D2%90");
		dict.put("ґ", "%D2%91");
		
		dict.put("Ў", "%D0%8E");
		dict.put("ў", "%D1%9E");
		
		dict.put("Ñ", "%C3%91");
		dict.put("ñ", "%C3%B1");
		
		dict.put("Ї", "%D0%87");
		dict.put("ї", "%D1%97");
		
		dict.put("ә", "%D3%99");
		dict.put("і", "%D1%96");
		dict.put("ң", "%D2%A3");
		dict.put("ғ", "%D2%93");
		dict.put("ү", "%D2%AF");
		dict.put("ұ", "%D2%B1");
		dict.put("қ", "%D2%9B");
		dict.put("ө", "%D3%A9");
		dict.put("һ", "%D2%BB");
		dict.put("Ә", "%D3%98");
		dict.put("І", "%D0%86");
		dict.put("Ң", "%D2%A2");
		dict.put("Ғ", "%D2%92");
		dict.put("Ү", "%D2%AE");
		dict.put("Ұ", "%D2%B0");
		dict.put("Қ", "%D2%9A");
		dict.put("Ө", "%D3%A8");
		dict.put("Һ", "%D2%BA");
		

		dict.put("á", "%C3%BC");
		dict.put("Á", "%C3%81");
		dict.put("ü", "%C3%BC");
		dict.put("ý", "%C3%BD");
	}
	

	public static String encode(String s) 
	{
		String result = "";

		for (int i = 0; i < s.length(); ++i)
		{
			char c = s.charAt(i);
			Object obj;
			if ((obj = dict.get(String.valueOf(c))) == null)
			{
				result += c;
			}
			else
			{
				result += String.valueOf(obj);
			}
		}

		return result;
	}
	
	public static String decode(String s)
			throws UnsupportedEncodingException
	{
		String enc = "UTF-8";
		int i = 0;
	    int j = s.length();
	    StringBuffer sb = new StringBuffer(j > 500 ? j / 2 : j);
	    int k = 0;
	    if (enc.length() == 0) {
	    	throw new UnsupportedEncodingException("URLDecoder: empty string enc parameter");
	    }
	    byte[] arrayOfByte = null;
	    while (k < j)
	    {
	    	char c = s.charAt(k);
	    	switch (c)
	    	{
	    		case '+': 
	    			sb.append(' ');
	    			k++;
	    			i = 1;
	    			break;
	    		case '%': 
	    			try
	    			{
	    				if (arrayOfByte == null)
	    				{
	    					arrayOfByte = new byte[(j - k) / 3];
	    				}
	    				int m = 0;
	    				while ((k + 2 < j) && (c == '%'))
	    				{
	    					arrayOfByte[(m++)] = ((byte)Integer.parseInt(s.substring(k + 1, k + 3), 16));
	    					k += 3;
	    					if (k < j) {
	    						c = s.charAt(k);
	    					}
	    				}	
	    				if ((k < j) && (c == '%'))
	    				{
	    					throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
	    				}
	    				sb.append(new String(arrayOfByte, 0, m, enc));
	    			}
	    			catch (NumberFormatException localNumberFormatException)
	    			{
	    				throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - " + localNumberFormatException.getMessage());
	    			}
	    			i = 1;
	    			break;
	    		default: 
	    			sb.append(c);
	    			k++;
	    	}
	    }
	    return i != 0 ? sb.toString() : s;
	  }
}