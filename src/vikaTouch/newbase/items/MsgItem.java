package vikaTouch.newbase.items;

import java.util.Date;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.TextBreaker;

public class MsgItem
	extends ChatItem
{
	public MsgItem(JSONObject json)
	{
		super(json);
	}

	public long mid;
	private String[] drawText;
	public Image ava;

	public void parseJSON()
	{
		super.parseJSON();
		parseAttachments();
		itemDrawHeight = 32;
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		if(drawText == null)
		{
			drawText = TextBreaker.breakText(text, false, this, true, DisplayUtils.width - 104);
			if(itemDrawHeight < 56)
			{
				itemDrawHeight = 56;
			}
		}
		
		if(ava == null)
		{
			ava = getAva();
		}
		
		if(ava != null)
			g.drawImage(ava, 16, 0 + y, 0);
		else
			g.drawImage(VikaTouch.cameraImg, 16, 10 + y, 0);
	}
	
	private Image getAva()
	{
		return null;
	}

	public String getTime()
	{
		return VikaUtils.time(new Date(date * 1000L));
	}

}
