package vikaUI;

import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.JSONBase;
import vikaTouch.newbase.attachments.Attachment;

public interface UIItem
{
	public void paint(Graphics g, int y, int scrolled);
	
	public int getDrawHeight();
}
