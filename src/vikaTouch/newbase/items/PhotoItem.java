package vikaTouch.newbase.items;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

public class PhotoItem
	extends Item
{
	public int itemDrawWidth = 50;
	private String iconUrl;
	private Image iconImg;
	
	public PhotoItem(JSONObject json, boolean dontLoadImage)
	{
		super(json);
		
		if(!dontLoadImage)
		{
			
		}
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		
	}

}
