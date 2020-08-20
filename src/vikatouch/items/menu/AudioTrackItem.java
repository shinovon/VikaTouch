package vikatouch.items.menu;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import ru.nnproject.vikaui.menu.items.UIItem;
import ru.nnproject.vikaui.screen.ScrollableCanvas;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.items.JSONUIItem;

public class AudioTrackItem 
	extends JSONUIItem implements UIItem 
{
	public int id;
	public int owner_id;
	public String name;
	public String artist; // исполнтель, ну, вторая строка
	private int length;
	
	public AudioTrackItem(JSONObject json)
	{
		super(json);
		setDrawHeight();
	}

	public void parseJSON()
	{
		System.out.println(json.toString());

		try
		{
			name = json.optString("title");
			artist = json.optString("artist");
			id = json.optInt("id");
			owner_id = json.optInt("owner_id");
			length = json.optInt("duration");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		setDrawHeight();
		System.gc();
	}

	private void setDrawHeight()
	{
		itemDrawHeight = 50;
	}

	public void paint(Graphics g, int y, int scrolled)
	{
		if(ScrollableCanvas.keysMode && selected)
		{
			ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
			g.fillRect(0, y, DisplayUtils.width, itemDrawHeight);
		}
		ColorUtils.setcolor(g, 0);
		if(name != null)
			g.drawString(name, 73, y, 0);
		ColorUtils.setcolor(g, ColorUtils.OUTLINE);
		g.drawString(artist, 73, y + 24, 0);
	}

	public void tap(int x, int y)
	{
		keyPressed(-5);
	}
	
	public void keyPressed(int key)
	{
		if(key == KEY_OK)
		{
			
		}
	}
}
