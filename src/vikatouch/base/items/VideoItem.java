package vikatouch.base.items;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.ScrollableCanvas;
import ru.nnproject.vikaui.popup.ConfirmBox;
import ru.nnproject.vikaui.popup.InfoPopup;
import vikamobilebase.VikaUtils;
import vikatouch.base.IconsManager;
import vikatouch.base.VikaTouch;
import vikatouch.base.settings.Settings;
import vikatouch.base.utils.ResizeUtils;

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
	
	public String file;
	public String playerUrl;
	
	public VideoItem(JSONObject json)
	{
		super(json);
		id = json.optInt("id");
		owner = json.optInt("owner_id");
		title = json.optString("title");
		descr = json.optString("description");
		try {
			iconUrl = fixJSONString(json.getJSONArray("image").getJSONObject(0).optString("url"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		length = json.optInt("duration");
		playerUrl = json.optString("player");
		views = json.optInt("views");
		itemDrawHeight = 50;
		
		try {
			JSONObject files = json.getJSONObject("files");
			file = files.optString("mp4_"+Settings.videoResolution);
			if(file==null)
			{
				file = files.optString("mp4_360");
			}
			if(file==null)
			{
				file = files.optString("mp4_240");
			}
			if(file!=null) file = fixJSONString(file);
		} catch (Exception e) {
			e.printStackTrace();
		} 
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
				e.printStackTrace();
			}
		}
		else
		{
			if(file!=null)
				VikaTouch.popup(new ConfirmBox("Загрузить видео-файл?","Будет скачано "+Settings.videoResolution+"p.", new Thread() {
					public void run() 
					{
						try {
							VikaTouch.appInst.platformRequest(file);
						} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						}
					}
				}, null));
		}
	}

	public void keyPressed(int key)
	{
		if(key == KEY_FUNC)
		{
			if(file!=null)
				VikaTouch.popup(new ConfirmBox("Загрузить видео-файл?","Будет скачано "+Settings.videoResolution+"p.", new Thread() {
					public void run() 
					{
						try {
							VikaTouch.appInst.platformRequest(file);
						} catch (ConnectionNotFoundException e) {
							e.printStackTrace();
						}
					}
				}, null));
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
		String subStr = min+":"+(sec<10?"0":"")+sec+"    "+views+" ";
		g.drawString(subStr, 73, y + 24, 0);
		g.drawImage(IconsManager.ico[IconsManager.VIEWS], 73+g.getFont().stringWidth(subStr)-2, y+20, 0);
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
