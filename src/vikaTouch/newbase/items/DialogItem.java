package vikaTouch.newbase.items;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.JSONBase;

public class DialogItem
	extends JSONBase
{
	String text;
	String title;
	MessageItem lastmessage;
	long chatid;
	boolean ls;
	long date;
	boolean read;
	boolean isMuted;
	String avaurl;
	
	public DialogItem(JSONObject json)
	{
		
	}

	public Image getAva()
	{
		Image img = null;
		try
		{
			 img = Image.createImage("/camera.png");
		if(avaurl != null)	
		{	
			try
			{
				 img = VikaUtils.downloadImage(avaurl);
			}
			catch (Exception e)
			{
				
			}
		}
			return DisplayUtils.resizeava(img);
		}
		catch (Exception e)
		{
			
		}
		return null;
	}
	
	public String getTime()
	{
		return VikaUtils.millisecondsToTime(date);
	}
	
	public void paint(Graphics g, int x, int y)
	{
		
	}

	public void parseJSON()
	{
		try {
			if(!json.isNull("chat_settings"))
			{
				JSONObject jo = json.getJSONObject("chat_settings");
				avaurl = fixJSONString(jo.getJSONObject("photo").optString("photo_50"));
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
}
