package vikatouch.base.items;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.ScrollableCanvas;
import vikamobilebase.VikaUtils;
import vikatouch.base.ResizeUtils;
import vikatouch.base.VikaTouch;

public class VideoItem
	extends JSONUIItem
{
	public int id;
	public int owner;
	public String title;
	public int length;
	public String iconUrl;
	public Image iconImg;
	public String descr;
	public int views;
	
	private static Image downloadBI = null;
	
	/*public String res144;
	public String res240;
	public String res360;
	public String res480;
	public String res720;*/
	public String playerUrl;
	
	public VideoItem(JSONObject json)
	{
		super(json);
		id = json.optInt("id");
		owner = json.optInt("owner_id");
		title = json.optString("title");
		descr = json.optString("description");
		iconUrl = fixJSONString(json.optString("photo_130"));
		length = json.optInt("duration");
		playerUrl = json.optString("player");
		views = json.optInt("views");
		itemDrawHeight = 50;
	}

	public int getDrawHeight() { return itemDrawHeight; }
	
	public void loadIcon()
	{
		try
		{
			iconImg = ResizeUtils.resizeItemPreview(VikaUtils.downloadImage(iconUrl));
		}
		catch (Exception e) { }
	}
	
	public void tap(int x, int y)
	{
		if(x<DisplayUtils.width - 50)
		{
			try {
				VikaTouch.appInst.platformRequest(playerUrl);
			} catch (ConnectionNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			// скачка
		}
	}

	public void keyPressed(int key)
	{
		if(key == KEY_FUNC)
		{
			// скачка
		}
		if(key == KEY_OK)
		{
			try
			{
				VikaTouch.appInst.platformRequest(playerUrl);
			}
			catch (ConnectionNotFoundException e) 
			{
				
			}
		}
	}

	public void paint(Graphics g, int y, int scrolled)
	{
		if(ScrollableCanvas.keysMode && selected)
		{
			ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
			g.fillRect(0, y, DisplayUtils.width, itemDrawHeight);
		}
		ColorUtils.setcolor(g, 0);
		if(title != null)
			g.drawString(title, 73, y, 0);
		ColorUtils.setcolor(g, ColorUtils.OUTLINE);
		int sec = length%60;
		int min = length/60;
		String secStr = (sec<10?"0":"")+sec;
		g.drawString(min+":"+secStr+", "+views+" просмотров", 73, y + 24, 0);
		if(iconImg != null)
		{
			g.drawImage(iconImg, 14, y+1, 0);
		}
		if(!ScrollableCanvas.keysMode)
		{
			try
			{
				if(downloadBI == null)
				{
					downloadBI = Image.createImage("/downloadBtn.png");
				}
				g.drawImage(downloadBI, DisplayUtils.width-downloadBI.getWidth(), y, 0);
			} 
			catch (Exception e)
			{
				
			}
		}
	}

}
