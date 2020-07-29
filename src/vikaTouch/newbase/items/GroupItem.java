package vikaTouch.newbase.items;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.attachments.PhotoSize;

public class GroupItem
	extends Item
{

	private String name;
	private String link;
	private int id;
	private Image ava = null;
	private int members;
	private boolean isAdmin;

	public static final int BORDER = 1;

	public GroupItem(JSONObject json)
	{
		super(json);
		itemDrawHeight = 50;
	}

	public void parseJSON()
	{
		try
		{
			name = json.optString("name");
			link = json.optString("screen_name");
			id = json.optInt("id");
			isAdmin = json.optInt("is_admin") == 1;
			members = json.optInt("members_count");
			try {
				ava = DisplayUtils.resizeava(VikaUtils.downloadImage(fixJSONString(json.optString("photo_50"))));
			} catch (Exception e) {
				System.out.println("Группа "+link+": ошибка аватарки");
				//System.out.println(json.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			VikaTouch.error(e, "Обработка объектов: Группы");
		}

		setDrawHeight();

		System.gc();
	}

	private void setDrawHeight()
	{
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			case DisplayUtils.DISPLAY_EQWERTY:
			{
				itemDrawHeight = 25;
				break;
			}
			case DisplayUtils.DISPLAY_PORTRAIT:
			case DisplayUtils.DISPLAY_ALBUM:
			case DisplayUtils.DISPLAY_E6:
			default:
			{
				itemDrawHeight = 50;
				break;
			}
		}
		itemDrawHeight += BORDER * 2;
	}

	public void paint(Graphics g, int y, int scrolled)
	{
		ColorUtils.setcolor(g, 0);
		if(name != null)
			g.drawString(name, 73, y, 0);
		ColorUtils.setcolor(g, 6);
		String descrS = (isAdmin ? "Администрирование, ":"")+(members>9999?((members/1000)+"K участников"):(members+" участников"));
		g.drawString(descrS, 73, y + 24, 0);
		if(ava != null)
		{
			g.drawImage(ava, 14, y + BORDER, 0);
		}
	}

	public void tap(int x, int y)
	{
		try
		{
			VikaTouch.error("Функционал групп ещё не готов. Группа: "+link, false);
		}
		catch (Exception e)
		{

		}
	}

}
