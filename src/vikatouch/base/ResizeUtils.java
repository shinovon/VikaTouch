package vikatouch.base;

import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.DisplayUtils;
import vikamobilebase.VikaUtils;

public class ResizeUtils
{


	public static Image resizeava(Image img)
	{
		short h = (short) img.getHeight();
		short need = h;
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_E6:
			case DisplayUtils.DISPLAY_PORTRAIT:
			case DisplayUtils.DISPLAY_ALBUM:
			{
				need = 50;
				break;
			}
			
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			case DisplayUtils.DISPLAY_EQWERTY:
			{
				need = 25;
				break;
			}
			
			default:
			{
				need = 50;
				break;
			}
		}
		if(h != need)
		{
			return VikaUtils.resize(img, need, -1);
		}
		return img;
	}

	public static Image resizeChatAva(Image img)
	{
		short h = (short) img.getHeight();
		short need = h;
		/*
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_E6:
			case DisplayUtils.DISPLAY_PORTRAIT:
			case DisplayUtils.DISPLAY_ALBUM:
			{
				need = 50;
				break;
			}
			
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			case DisplayUtils.DISPLAY_EQWERTY:
			{
				need = 50;
				break;
			}
			
			default:
			{
				need = 50;
				break;
			}
		}
		*/
		need = 50;
		if(h != need)
		{
			return VikaUtils.resize(img, need, -1);
		}
		return img;
	}


	public static Image resizeItemPreview(Image img)
	{
		short h = (short) img.getHeight();
		short need = h;
		/*
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_E6:
			case DisplayUtils.DISPLAY_PORTRAIT:
			case DisplayUtils.DISPLAY_ALBUM:
			{
				need = 48;
				break;
			}
			
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			case DisplayUtils.DISPLAY_EQWERTY:
			{
				need = 48;
				break;
			}

			case DisplayUtils.DISPLAY_UNDEFINED:
			default:
			{
				need = 48;
				break;
			}
		}
		*/
		need = 48;
		if(h != need)
		{
			return VikaUtils.resize(img, need, -1);
		}
		return img;
	}
}
