package vikaTouch.newbase.items;

import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.JSONBase;
import vikaTouch.newbase.attachments.Attachment;

public class Item
	extends JSONBase
{
	public Item(JSONObject json)
	{
		this.json = json;
	}
	
	public Attachment[] attachments = new Attachment[5];

	public int fromid;
	public String text;
	public long date;
	public int itemDrawHeight = 50;
	
	public void parseJSON()
	{
		text = fixJSONString(json.optString("text"));
		fromid = json.optInt("from_id");
		date = json.optLong("date");
		
		try
		{
			if(!json.isNull("attachments"))
			{
				final JSONArray attachments = json.getJSONArray("attachments");
				if(this.attachments.length > attachments.length())
				{
					this.attachments = new Attachment[attachments.length()];
				}
				for(int i = 0; i < attachments.length(); i++)
				{
					if(i >= this.attachments.length)
					{
						break;
					}
					this.attachments[i] = Attachment.parse(attachments.getJSONObject(i));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g, int x, int y)
	{
		
	}
	
	public String getTime()
	{
		return VikaUtils.millisecondsToTime(date);
	}
}
