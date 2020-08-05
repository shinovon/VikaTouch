package vikatouch.base.items;

import java.util.Date;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.TextBreaker;
import vikamobilebase.VikaUtils;
import vikatouch.base.VikaTouch;

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

	public void tap(int x, int y)
	{
		
	}

	public void keyPressed(int key)
	{
		
	}

}