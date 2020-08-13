package vikatouch.base.items;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.base.VikaTouch;
import vikatouch.base.attachments.PhotoSize;
import vikatouch.base.utils.ErrorCodes;
import vikatouch.base.utils.ResizeUtils;
import vikatouch.screens.menu.GroupsScreen;
import vikatouch.screens.page.GroupPageScreen;

public class GroupItem
	extends JSONUIItem
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
			VikaTouch.error(e, ErrorCodes.GROUPPARSE);
		}

		setDrawHeight();

		System.gc();
	}

	private void setDrawHeight()
	{
		/*
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
				itemDrawHeight = 48;
				break;
			}
		}*/
		itemDrawHeight = 48;
		itemDrawHeight += BORDER * 2;
	}

	public void getAva() {
		try {
			ava = ResizeUtils.resizeItemPreview(VikaUtils.downloadImage(fixJSONString(json.optString("photo_50"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		
		if(selected)
		{
			ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
			g.fillRect(0, y, DisplayUtils.width, itemDrawHeight);
		}
		ColorUtils.setcolor(g, 0);
		if(name != null)
			g.drawString(name, 73, y, 0);
		ColorUtils.setcolor(g, ColorUtils.OUTLINE);
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
			GroupsScreen.abortLoading();
			VikaTouch.setDisplay(new GroupPageScreen(id), 1);
		}
		catch (Exception e)
		{

		}
	}

	public void keyPressed(int key)
	{
		if(key == KEY_OK)
			tap(20,20);
	}

}
