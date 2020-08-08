package vikatouch.base.items;

import java.util.Date;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import ru.nnproject.vikaui.*;
import vikamobilebase.VikaUtils;
import vikatouch.base.VikaTouch;
import vikatouch.screens.ChatScreen;

public class MsgItem
	extends ChatItem
{
	public MsgItem(JSONObject json)
	{
		super(json);
	}
	
	public long mid;
	private String[] drawText;
	public String name = null;
	public boolean foreign;
	public static int maxWidth = 300;
	public static int margin = 10;
	public int linesC;
	private String time;
	
	private boolean hasReply;
	public String replyName;
	public String replyText;

	public void parseJSON()
	{
		super.parseJSON();
		parseAttachments();
		// {"id":354329,"important":false,"date":1596389831,"attachments":[],"out":0,"is_hidden":false,"conversation_message_id":7560,"fwd_messages":[],"random_id":0,"text":"Будет срач с Лëней или он уже потерял интерес?","from_id":537403336,"peer_id":537403336}
		foreign = json.optInt("from_id")!=Integer.parseInt(VikaTouch.userId);
		int h1 = Font.getFont(0, 0, 8).getHeight();
		drawText = TextBreaker.breakText(text, false, null, true, maxWidth-h1);
		for (linesC=0; (linesC<drawText.length && drawText[linesC]!=null); linesC++) { }
		
		itemDrawHeight = h1*(linesC+1);
		
		
		JSONObject reply = json.optJSONObject("reply_message");
		if(reply!=null)
		{
			hasReply = true;
			replyText = reply.optString("text");
			if(replyText==null)
			{
				replyText = "Вложения";
			}
			else
			{
				replyText = TextBreaker.breakText(replyText, false, null, true, maxWidth-h1-h1)[0];
			}
			int fromId = reply.optInt("from_id");
			if(fromId==Integer.parseInt(VikaTouch.userId))
			{
				replyName = "Вы";
			}
			else
			{
				if(ChatScreen.profileNames.containsKey(new Integer(fromId)))
				{
					replyName = (String) ChatScreen.profileNames.get(new Integer(fromId));
				}
			}
		}
		
		// experimental
		{
			if(text.equals("Т")) VikaTouch.popup(new InfoPopup(json.toString(), null));
		}
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		Font font = Font.getFont(0, 0, 8);
		g.setFont(font);
		int h1 = font.getHeight();
		int th = h1*(linesC+1+(name==null?0:1)+(hasReply?2:0));
		itemDrawHeight = th;
		int textX = 0;
		final int radius = 16;
		if(foreign)
		{
			ColorUtils.setcolor(g, ColorUtils.FOREIGNMSG);
			g.fillRoundRect(margin, y, maxWidth, th, radius, radius);
			g.fillRect(margin, y+th-radius, radius, radius);
			textX = margin + h1/2;
		}
		else
		{
			ColorUtils.setcolor(g, ColorUtils.MYMSG);
			g.fillRoundRect(DisplayUtils.width-(margin+maxWidth), y, maxWidth, th, radius, radius);
			g.fillRect(DisplayUtils.width-(margin+radius), y+th-radius, radius, radius);
			textX = DisplayUtils.width-(margin+maxWidth) + h1/2;
		}
		if(name!=null)
		{
			ColorUtils.setcolor(g, ColorUtils.COLOR1);
			g.drawString(name, textX, y+h1/2, 0);
			ColorUtils.setcolor(g, ColorUtils.OUTLINE);
			if(time == null)
			{
				time = getTime();
			}
			g.drawString(time, textX-h1+maxWidth-font.stringWidth(time), y+h1/2, 0);
		}
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		for(int i = 0; i < linesC; i++)
		{
			g.drawString(drawText[i]==null?" ":drawText[i], textX, y+h1/2+h1*(i+(name==null?0:1)), 0);
		}
		
		if(hasReply)
		{
			g.drawString(replyText, textX+h1, y+h1/2+h1*(linesC+1+(name==null?0:1)), 0);
			ColorUtils.setcolor(g, ColorUtils.COLOR1);
			g.drawString(replyName, textX+h1, y+h1/2+h1*(linesC+(name==null?0:1)), 0);
			g.fillRect(textX+h1/2-1, y+h1/2+h1*(linesC+(name==null?0:1)), 2, h1*2);
		}
		
	}

	public String getTime()
	{
		return VikaUtils.parseMsgTime(date);
	}
	
	public int getDrawHeight()
	{
		return itemDrawHeight;
	}

	public void tap(int x, int y)
	{
		
	}

	public void keyPressed(int key)
	{
		
	}

}
