package ru.nnproject.vikaui.utils;

import javax.microedition.lcdui.Font;

import ru.nnproject.vikaui.menu.items.UIItem;

public class TextBreaker
{
	
	public static String[] breakText(final String text, boolean shortText, UIItem item, final boolean full, final int width)
	{
		Font font = Font.getFont(0, 0, 8);
		String[] result;
		if(text.length() > 24 || text.indexOf("\n") != -1)
		{
			char[] chars = text.toCharArray();
			int lncount = 0;
			result = new String[10];
			if(full)
				result = new String[100];
			try
			{
				if(font.stringWidth(text) > width)
				{
					String x2 = "";
					for(int i2 = 0; i2 < text.length(); i2++)
					{
						if(chars[i2] == '\n')
						{
							result[lncount] = x2;
							x2 = "";
							lncount++;
							if(item != null)
								item.addDrawHeight(24);
						}
						else
						{
							x2 += ""+chars[i2];
							if(font.stringWidth(x2) > width)
							{
								result[lncount] = x2;
								x2 = "";
								lncount++;
								if(item != null)
									item.addDrawHeight(24);
							}
							else if(text.length() - i2 <= 1)
							{
								result[lncount] = x2;
								x2 = "";
								lncount++;
								if(item != null)
									item.addDrawHeight(24);
							}
						}
					}
				}
				else
				{
					result[lncount] = text;
					if(item != null)
						item.addDrawHeight(24);
				}
				
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				result[result.length-1] = "Показать полностью...";
			}
		}
		else
		{
			result = new String[2];
			result[0] = text; 
			shortText = true;
			if(text.length() > 1)
				if(item != null)
					item.addDrawHeight(24);
		}
		return result;
	}

}
