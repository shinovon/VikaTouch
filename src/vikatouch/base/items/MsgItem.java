package vikatouch.base.items;

import java.util.Date;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.TextBreaker;
import vikamobilebase.VikaUtils;
import vikatouch.base.VikaTouch;

public class MsgItem
	extends ChatItem
{
	public MsgItem(JSONObject json)
	{
		super(json);
	}
	
	public long mid;
	private String[] drawText;
	public String name;
	public boolean foreign;
	public static int maxWidth = 300;
	public static int margin = 10;
	public int linesC;
	

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
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		Font font = Font.getFont(0, 0, 8);
		g.setFont(font);
		int h1 = font.getHeight();
		int th = h1*(linesC+1);
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
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		
		for(int i = 0; i < linesC; i++)
		{
			//g.drawString(drawText[i]==null?" ":drawText[i], textX, y+h1/2+h1*i, 0);
			g.drawString(text, textX, y+h1/2+h1*i, 0); // TEST
		}
	}

	public String getTime()
	{
		return VikaUtils.time(new Date(date * 1000L));
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
