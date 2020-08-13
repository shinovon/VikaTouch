package vikatouch.base.items;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONObject;

import ru.nnproject.vikaui.*;
import ru.nnproject.vikaui.menu.IMenu;
import ru.nnproject.vikaui.popup.*;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import ru.nnproject.vikaui.utils.TextBreaker;
import vikamobilebase.VikaUtils;
import vikatouch.base.IconsManager;
import vikatouch.base.VikaTouch;
import vikatouch.base.attachments.*;
import vikatouch.screens.ChatScreen;

public class MsgItem
	extends ChatItem implements IMenu
{
	public MsgItem(JSONObject json)
	{
		super(json);
	}
	
	public long mid;
	private String[] drawText;
	public String name = null;
	public boolean foreign;
	public static int msgWidth = 300;
	public static int margin = 10;
	public static int attMargin = 5;
	public int linesC;
	private String time;
	public boolean showName;
	
	private int attH = -1;
	
	private boolean hasReply;
	public String replyName;
	public String replyText;

	public void parseJSON()
	{
		super.parseJSON();
		parseAttachments();
		// {"id":354329,"important":false,"date":1596389831,"attachments":[],"out":0,"is_hidden":false,"conversation_message_id":7560,"fwd_messages":[],"random_id":0,"text":"Будет срач с Лëней или он уже потерял интерес?","from_id":537403336,"peer_id":537403336}
		foreign = json.optInt("from_id")!=Integer.parseInt(VikaTouch.userId);
		mid = json.optLong("id");
		int h1 = Font.getFont(0, 0, 8).getHeight();
		drawText = TextBreaker.breakText(text, false, null, true, msgWidth-h1);
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
				replyText = TextBreaker.breakText(replyText, false, null, true, msgWidth-h1-h1)[0];
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
			//if(text.equals("Т")) VikaTouch.popup(new InfoPopup(json.toString(), null));
		}
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		if(attH<0)
		{
			attH = 0;
			// prepairing attachments
			try {
				for(int i=0; i<attachments.length; i++)
				{
					Attachment at = attachments[i];
					if(at==null) continue;
					
					if(at instanceof PhotoAttachment)
					{
						((PhotoAttachment) at).load();
					}
					attH += at.getDrawHeight()+attMargin;
				}
				if(attH != 0) { attH += attMargin; }
			}
			catch (Exception e)
			{
				attH = 0;
				e.printStackTrace();
			}
		}
		// drawing
		Font font = Font.getFont(0, 0, 8);
		g.setFont(font);
		int h1 = font.getHeight();
		int attY = h1*(linesC+1+(name==null?0:1)+(hasReply?2:0));
		int th = attY + attH;
		itemDrawHeight = th;
		int textX = 0;
		final int radius = 16;
		if(foreign)
		{
			ColorUtils.setcolor(g, ColorUtils.FOREIGNMSG);
			g.fillRoundRect(margin, y, msgWidth, th, radius, radius);
			g.fillRect(margin, y+th-radius, radius, radius);
			textX = margin + h1/2;
			if(selected)
			{
				ColorUtils.setcolor(g, ColorUtils.TEXT);
				g.setStrokeStyle(Graphics.SOLID);
				g.drawRoundRect(margin, y, msgWidth, th, radius, radius);
			}
		}
		else
		{
			ColorUtils.setcolor(g, ColorUtils.MYMSG);
			g.fillRoundRect(DisplayUtils.width-(margin+msgWidth), y, msgWidth, th, radius, radius);
			g.fillRect(DisplayUtils.width-(margin+radius), y+th-radius, radius, radius);
			textX = DisplayUtils.width-(margin+msgWidth) + h1/2;
			if(selected)
			{
				ColorUtils.setcolor(g, ColorUtils.TEXT);
				g.setStrokeStyle(Graphics.SOLID);
				g.drawRoundRect(DisplayUtils.width-(margin+msgWidth), y, msgWidth, th, radius, radius);
			}
		}
		if(name!=null&&showName)
		{
			ColorUtils.setcolor(g, ColorUtils.COLOR1);
			g.drawString(name, textX, y+h1/2, 0);
			ColorUtils.setcolor(g, ColorUtils.OUTLINE);
			if(time == null)
			{
				time = getTime();
			}
			g.drawString(time, textX-h1+msgWidth-font.stringWidth(time), y+h1/2, 0);
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
		
		// рендер аттачей
		if(attH>0)
		{
			attY += attMargin;
			for(int i=0; i<attachments.length; i++)
			{
				Attachment at = attachments[i];
				if(at==null) continue;
				
				if(at instanceof PhotoAttachment)
				{
					PhotoAttachment pa = (PhotoAttachment) at;
					int rx = foreign ? (margin + attMargin) : (DisplayUtils.width - (margin + attMargin) - pa.renderW);
					if(pa.renderImg == null)
					{
						g.drawString("Не удалось загрузить изображение", textX, y+attY, 0);
					}
					else
					{
						g.drawImage(pa.renderImg, rx, y+attY, 0);
					}
				}
				else if(at instanceof DocumentAttachment)
				{
					int x1 = foreign ? (margin + attMargin) : (DisplayUtils.width - (margin + msgWidth) + attMargin);
					((DocumentAttachment) at).draw(g, x1, y+attY, msgWidth - attMargin*2);
				}
				else if(at instanceof WallAttachment)
				{
					int x1 = foreign ? (margin + attMargin) : (DisplayUtils.width - (margin + msgWidth) + attMargin);
					g.drawString("Запись на стене", x1, y+attY, 0);
				}
				
				attY += at.getDrawHeight()+attMargin;
			}
		}
	}
	
	public String[] searchLinks()
	{
		int lm = 8;
		String[] l = new String[lm];
		int fc=0;
		
		
		return l;
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
		keyPressed(-5);
	}

	public void keyPressed(int key)
	{
		if(key == -5) 
		{
			int h = DisplayUtils.height>240?36:30;
			OptionItem[] opts = new OptionItem[7];
			opts[0] = new OptionItem(this, "Ответить", IconsManager.ANSWER, -1, h);
			opts[1] = new OptionItem(this, "Удалить", IconsManager.CLOSE, -2, h);
			opts[2] = new OptionItem(this, "Редактировать", IconsManager.EDIT, -4, h);
			opts[3] = new OptionItem(this, "Копировать текст", IconsManager.ADD, -5, h);
			opts[4] = new OptionItem(this, "Переслать", IconsManager.SEND, -6, h);
			opts[5] = new OptionItem(this, "Ссылки...", IconsManager.LINK, -8, h);
			opts[6] = new OptionItem(this, "Вложения...", IconsManager.ATTACHMENT, -9, h);
			VikaTouch.popup(new ContextMenu(opts));
		}
	}

	public void onMenuItemPress(int i) {
		if(i<=-100)
		{
			// ссылки
			i = -i;
			i = i - 100;
			try
			{
				VikaTouch.appInst.platformRequest(searchLinks()[i]);
			}
			catch (ConnectionNotFoundException e) 
			{
				VikaTouch.popup(new InfoPopup("Не удалось открыть. Возможно, произошла ошибка при обработке адреса либо нет подключения к интернету.", null));
			}
			return;
		}
		if(i>=0)
		{ // прикрепы
			try
			{
				attachments[i].press();
			}
			catch (Exception e) { }
			return;
		}
		// основная менюшка
		// Да, такая лапша. Ты ещё покруче делаешь.
		switch(i)
		{
		case -1:
			ChatScreen.attachAnswer(mid, name, text);
			break; // БРЕАК НА МЕСТЕ!!11!!1!
		case -2:
			VikaTouch.popup(new InfoPopup("УДОЛИ", null));
			break;
		case -4:
			VikaTouch.popup(new InfoPopup("Редачить", null));
			break;
		case -5:
			VikaTouch.popup(new InfoPopup("Я хз как это реализовать на симбе.", null));
			break;
		case -6:
			VikaTouch.popup(new InfoPopup("Пересылку тоже не изобрели", null));
			break;
		case -8:
			VikaTouch.popup(new InfoPopup("Ссылки в сибирь", null));
			break;
		case -9:
			{
				int l = attachments.length;
				OptionItem[] opts = new OptionItem[l];
				int photoC = 1;
				int h = DisplayUtils.height>240?36:30;
				for(int j=0;j<l;j++)
				{
					Attachment a = attachments[j];
					if(a.type.equals("photo"))
					{
						opts[j] = new OptionItem(this, "Фотография "+photoC, IconsManager.PHOTOS, j, h);
						a.attNumber = photoC;
						photoC++;
					}
					else if(a.type.equals("doc"))
					{
						DocumentAttachment da = (DocumentAttachment) a;
						opts[j] = new OptionItem(this, da.name + " ("+(da.size/1000)+"kb)", IconsManager.DOCS, j, h);
					}
					else
					{
						opts[j] = new OptionItem(this, "Вложение", IconsManager.ATTACHMENT, j, h);
					}
				}
				if(opts != null && opts.length>0)
				{
					VikaTouch.popup(new ContextMenu(opts));
				}
				else
				{
					VikaTouch.popup(new InfoPopup("У этого сообщения нет вложений.", null));
				}
			}
			break;
		}
	}

	public void onMenuItemOption(int i) {
		
	}

}
