package vikatouch.screens;

import java.util.Random;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.PressableUIItem;
import ru.nnproject.vikaui.TextBreaker;
import vikamobilebase.VikaUtils;
import vikatouch.base.IconsManager;
import vikatouch.base.Settings;
import vikatouch.base.TextEditor;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.MsgItem;

public class ChatScreen
	extends ReturnableListScreen
{
	private static final int TYPE_USER = 1;
	private static final int TYPE_CHAT = 2;
	private static final int TYPE_GROUP = 3;
	public int peerId;
	public int localId;
	public int type;
	public static final int OFFSET_INT = 2000000000;
	public String title = "dialog";
	public String title2 = "оффлайн";
	public String inputText = "";
	private int textboxmodifier;
	private boolean textboxSelected;
	private String[] inputedTextToDraw;
	private boolean inputChanged;
	private JSONObject json;
	private JSONObject chatSettings;
	
	public ChatScreen(int peerId, String title)
	{
		title2 = "Загрузка...";
		this.title = title;
		this.peerId = peerId;
		parse();
	}
	
	public ChatScreen(int peerId)
	{
		title2 = "Загрузка...";
		this.peerId = peerId;
		parse();
	}

	private void parse()
	{
		if(peerId < 0)
		{
			this.localId = -peerId;
			type = TYPE_GROUP;
			//title2 = "group" + this.localId;
			this.title2 = "";
		}
		else if(peerId > 0)
		{
			if(peerId > OFFSET_INT)
			{
				this.localId = peerId - OFFSET_INT;
				this.type = TYPE_CHAT;
				//title2 = "chat" + this.localId;
				try
				{
					final String x = VikaUtils.download(new URLBuilder("messages.getConversationsById").addField("peer_ids", peerId));
					try
					{
						json = new JSONObject(x).getJSONObject("response").getJSONArray("items").getJSONObject(0);
						
						chatSettings = json.getJSONObject("chat_settings");
						
						this.title2 = "" + chatSettings.optInt("members_count") + " участников";
					}
					catch (JSONException e)
					{
						this.title2 = e.toString();
						//this.title2 = "Ошибка JSON";
					}
				}
				catch (Exception e)
				{
					this.title2 = "Не удалось загрузить информацию.";
				}
			}
			else
			{
				this.localId = peerId;
				this.type = TYPE_USER;
				//title2 = "dm" + this.localId;
				try
				{
					final String x = VikaUtils.download(new URLBuilder("users.get").addField("user_ids", peerId).addField("fields", "online").addField("name_case", "nom"));
					try
					{
						JSONObject json = new JSONObject(x).getJSONArray("response").getJSONObject(0);
						this.title2 = json.optInt("online") > 0 ? "онлайн" : "оффлайн";
					}
					catch (JSONException e)
					{
						this.title2 = "Ошибка JSON";
					}
				}
				catch (Exception e)
				{
					this.title2 = "Не удалось загрузить информацию.";
				}
				try
				{ // скачка сообщений
					uiItems = new PressableUIItem[Settings.messagesPerLoad];
					final String x = VikaUtils.download(new URLBuilder("messages.getHistory").addField("peer_id", peerId).addField("count", Settings.messagesPerLoad).addField("offset", 0));
					JSONArray json = new JSONObject(x).getJSONObject("response").getJSONArray("items");
					boolean lastF = false;
					for(int i = 0; i<json.length();i++) {
						MsgItem m = new MsgItem(json.getJSONObject(i));
						m.parseJSON();
						boolean chain = (lastF == m.foreign);
						if(!chain)
						{
							m.name = (m.foreign?title:"Вы");
							//TODO время отправки
						}
						uiItems[uiItems.length-1-i] = m;
						lastF = m.foreign;
					}
				}
				catch (Exception e)
				{
					this.title2 = "Не удалось загрузить сообщения.";
					e.printStackTrace();
				}
			}
		}
		
		System.gc();
	}

	public void draw(Graphics g)
	{
		update(g);
		if(textboxSelected)
		{
			g.translate(0, (oneitemheight * textboxmodifier));
		}
		
		drawDialog(g);
		
		g.translate(0, -g.getTranslateY());
		
		drawHeader(g);
		
		drawTextbox(g);
	}

	protected void scrollHorizontally(int deltaX)
	{
		
	}
	
	public final void press(int x, int y)
	{
		textboxSelected = false;
		if(!dragging)
		{
			if(y > 590)
			{
				//нижняя панель
				
				//текстбокс
				if(x > 50 && x < DisplayUtils.width - 98)
				{
					textboxSelected = true;
				}
			}
			else if(y < 50)
			{
				//верхняя панель
				if(x < 50)
				{
				}
			}
		}
		super.press(x, y);
	}
	
	public final void release(int x, int y)
	{
		textboxSelected = false;
		if(!dragging)
		{
			if(y > 590)
			{
				//нижняя панель
				
				//текстбокс
				if(x > 50 && x < DisplayUtils.width - 98)
				{
					showTextBox();
				}
				else if(x < 50)
				{
					//прикреп
				}
				else if(x > DisplayUtils.width - 40)
				{
					//отправить
					send();
				}
				else if(x > DisplayUtils.width - 90)
				{
					//емоци и стикеры
				}
			}
			else if(y < 50)
			{
				//верхняя панель
				if(x < 50)
				{
					VikaTouch.inst.cmdsInst.command(14, this);
				}
			}
		}
		super.release(x, y);
	}

	private void send()
	{
		new Thread()
		{
			public void run()
			{
				VikaUtils.download(new URLBuilder("messages.send").addField("random_id", new Random().nextInt(1000)).addField("peer_id", peerId).addField("message", inputText).addField("intent", "default"));
				inputText = "";
				inputChanged = true;
			}
		}.start();
	}

	private void showTextBox()
	{
		new Thread()
		{
			public void run()
			{
				if(inputText != null)
				{
					inputText = TextEditor.inputString("Сообщение", inputText, 0);
				}
				else
				{
					inputText = TextEditor.inputString("Сообщение", "", 0);
				}
				inputChanged = true;
				textboxSelected = true;
			}
		}.start();
	}

	private void drawDialog(Graphics g)
	{
		if(uiItems==null) return;
		
		int y = 0;
		final int space = 4;
		for(int i=0; i<uiItems.length; i++)
		{
			if(uiItems[i] == null) continue;
			
			y+=space;
			uiItems[i].paint(g, y, scrolled);
			y+=uiItems[i].getDrawHeight();
		}
		this.itemsh = y;
	}

	private void drawTextbox(Graphics g)
	{
		int h = 48;
		ColorUtils.setcolor(g, -8);
		g.fillRect(0, 591 - (h * textboxmodifier), 640, 1);
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, 592 - (h * textboxmodifier), 640, 50);

		Font font = Font.getDefaultFont();
		g.setFont(font);
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		
		if(textboxSelected && inputText != null && inputText.length() > 0)
		{
			if(inputChanged || inputedTextToDraw == null)
				inputedTextToDraw = TextBreaker.breakText(inputText, false, null, true, DisplayUtils.width - 150);
			if(inputedTextToDraw == null)
				return;
			textboxmodifier = inputedTextToDraw.length-1;
			int y = 596 - (h * textboxmodifier);
			
			for(int i = 0; i < inputedTextToDraw.length; i++)
			{
				g.drawString(inputedTextToDraw[i], 51, y, 0);
				y += h;
			}
			
			g.drawImage(IconsManager.ico[IconsManager.ATTACHMENT], 17, DisplayUtils.height - (36 + (oneitemheight * textboxmodifier)), 0);

			g.drawImage(IconsManager.ico[IconsManager.STICKERS], DisplayUtils.width - 86, DisplayUtils.height - (36 + (oneitemheight * textboxmodifier)), 0);

			g.drawImage(IconsManager.selIco[IconsManager.SEND], DisplayUtils.width - 40, DisplayUtils.height - (36 + (oneitemheight * textboxmodifier)), 0);
		}
		else if(inputText != null && inputText.length() > 0)
		{
			String s = inputText;
			
			if(font.stringWidth(s) > DisplayUtils.width - 150)
			{
				s = s.substring(0, 18) + "...";
			}
			
			g.drawString(s, 51, 596, 0);
			g.drawImage(IconsManager.ico[IconsManager.ATTACHMENT], 17, DisplayUtils.height - 36, 0);

			g.drawImage(IconsManager.ico[IconsManager.STICKERS], DisplayUtils.width - 86, DisplayUtils.height - 36, 0);
			
			g.drawImage(IconsManager.selIco[IconsManager.SEND], DisplayUtils.width - 40, DisplayUtils.height - 36, 0);
		}
		else
		{
			g.drawImage(IconsManager.ico[IconsManager.ATTACHMENT], 17, DisplayUtils.height - 36, 0);

			g.drawImage(IconsManager.ico[IconsManager.STICKERS], DisplayUtils.width - 86, DisplayUtils.height - 36, 0);
			
			g.drawImage(IconsManager.ico[IconsManager.VOICE], DisplayUtils.width - 40, DisplayUtils.height - 36, 0);
		}

	}

	private void drawHeader(Graphics g)
	{
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, 0, 640, 55);
		ColorUtils.setcolor(g, -12);
		g.fillRect(0, 56, 640, 1);
		
		Font font1 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
		g.setFont(font1);
		ColorUtils.setcolor(g, 0);
		g.drawString(title, 64, 10, 0);
		
		Font font2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
		g.setFont(font2);
		ColorUtils.setcolor(g, ColorUtils.TEXT2);
		g.drawString(title2, 64, 30, 0);
		
		g.drawImage(IconsManager.selIco[IconsManager.BACK], 16, 16, 0);
		g.drawImage(IconsManager.selIco[IconsManager.INFO], DisplayUtils.width - 38, 16, 0);
	}

}
