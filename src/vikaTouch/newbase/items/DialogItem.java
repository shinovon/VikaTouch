package vikaTouch.newbase.items;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.Dialogs;
import vikaTouch.newbase.DisplayUtils;

public class DialogItem
	extends Item
{
	public String text;
	public String title;
	public MessageItem lastmessage;
	public long chatid;
	public boolean ls;
	public long date;
	public boolean unread;
	public boolean isMuted;
	public String avaurl;
	public boolean selected;
	private String time;
	private String type;
	private boolean isGroup;
	private int id;
	private static Image deleteImg;
	private static Image unreadImg;
	
	public DialogItem(JSONObject json)
	{
		super(json);
		itemDrawHeight = 63;

		try
		{
			if(deleteImg == null)
			{
				deleteImg = Image.createImage("/dialremove.png");
			}
			
			if(unreadImg == null)
			{
				unreadImg = Image.createImage("/unread.png");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Image getAva()
	{
		Image img = null;
		try
		{
			img = Image.createImage("/camera.png");
			if(avaurl != null)	
			{
				try
				{
					img = VikaUtils.downloadImage(avaurl);
				}
				catch (Exception e)
				{
					
				}
			}
			return DisplayUtils.resizeava(img);
		}
		catch (Exception e)
		{
			
		}
		return null;
	}
	
	public String getTime()
	{
		return VikaUtils.parseShortTime(date);
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		Font font = Font.getFont(0, 0, 8);
		
		if(time == null)
		{
			time = getTime();
		}
		
		ColorUtils.setcolor(g, 0);
		
		if(selected)
		{
			ColorUtils.setcolor(g, 3);
			g.fillRect(0, y - 1, DisplayUtils.width, itemDrawHeight + 1);
			ColorUtils.setcolor(g, -1);
			g.drawImage(deleteImg, DisplayUtils.width - 25, y + 17, 0);
		}
		if(title != null)
		{
			g.drawString(title, 73, y + 16, 0);
		}
		
		if(!selected)
		{
			ColorUtils.setcolor(g, 6);
		}
		
		g.drawString(text, 73, y + 40, 0);
		
		if(!selected && time != null)
		{
			ColorUtils.setcolor(g, 7);
			g.drawString(time, DisplayUtils.width - (16 + font.stringWidth(time)), y + 16, 0);
		}
		
		Image ava = getAva();
		
		if(ava != null)
		{
			g.drawImage(ava, 14, y + 8, 0);
		}
		
		if(!selected)
		{
			ColorUtils.setcolor(g, -5);
			g.fillRect(72, y + itemDrawHeight, 640, 1);
			
			if(unread)
			{
				g.drawImage(unreadImg, DisplayUtils.width - 24, y + 42, 0);
			}
			
		}
	}

	public void parseJSON()
	{
		try
		{
			final JSONObject conv = json.getJSONObject("conversation");

			try
			{
				JSONObject chatSettings = conv.getJSONObject("chat_settings");
				avaurl = fixJSONString(chatSettings.getJSONObject("photo").optString("photo_50"));
				title = fixJSONString(chatSettings.optString("title"));
				isGroup = chatSettings.optBoolean("is_group_channel");
			}
			catch (Exception e)
			{
				//chat_settings может не существовать, так-что это исключение игнорируется
			}
			
			unread = conv.optInt("unread_count") > 0;
				
			final JSONObject peer = conv.getJSONObject("peer");
			type = fixJSONString(peer.optString("type"));
			id = peer.optInt("local_id");
			
			if(type == "user")
			{
				for(int i = 0; i < Dialogs.profiles.length(); i++)
				{
					final JSONObject profile = Dialogs.profiles.getJSONObject(i);
					if(profile.getInt("id") == id)
					{
						title = fixJSONString(profile.optString("first_name") + " " + profile.optString("last_name"));
						avaurl = fixJSONString(profile.optString("photo_50"));
						break;
					}
				}
			}
			if(type == "group" && Dialogs.groups != null)
			{
				for(int i = 0; i < Dialogs.groups.length(); i++)
				{
					final JSONObject group = Dialogs.groups.getJSONObject(i);
					if(group.getInt("id") == lastmessage.fromid)
					{
						title = fixJSONString(group.optString("name"));
						avaurl = fixJSONString(group.optString("photo_50"));
						break;
					}
				}	
			}
		}
		catch (JSONException e)
		{
			VikaTouch.error(e, "Парсинг диалога");
			e.printStackTrace();
		}
		
		try
		{
			lastmessage = new MessageItem(json.getJSONObject("last_message"));
			lastmessage.parseJSON();

			date = lastmessage.date;
			
			text = lastmessage.text;
			
			
			String nameauthora = "";
			
			if(("" + lastmessage.fromid).equalsIgnoreCase(VikaTouch.userId))
			{
				nameauthora = "Вы";
			}
			else if(isGroup || type.equalsIgnoreCase("chat"))
			{
				for(int i = 0; i < Dialogs.profiles.length(); i++)
				{
					final JSONObject profile = Dialogs.profiles.getJSONObject(i);
					if(("" + lastmessage.fromid).equalsIgnoreCase("" + profile.optInt("id")))
					{
						nameauthora = profile.optString("first_name");
					}
				}
			}
			
			if(nameauthora != "")
			{
				text = nameauthora + ": " + text;
			}
			
			
			if(text.length() > 26)
			{
				text = text.substring(0, 26) + "...";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(title != null && title.length() > 22)
		{
			title = title.substring(0, 22) + "...";
		}
		
		System.gc();
	}
	
	public void tap(int x, int y)
	{
		if(selected)
		{
			if(x > DisplayUtils.width - 25 && y > 16 && y < 32)
			{
				remove();
			}
			else
			{
				Dialogs.openDialog(this);
			}
		}
	}
	
	private void remove()
	{
		
	}

	public void pressed()
	{
		selected = true;
	}
	
	public void released(boolean dragging)
	{
		if(dragging)
		{
			selected = false;
		}
	}
	
}
