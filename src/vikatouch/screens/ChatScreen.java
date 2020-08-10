package vikatouch.screens;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.VikaCanvas;
import ru.nnproject.vikaui.menu.items.PressableUIItem;
import ru.nnproject.vikaui.popup.InfoPopup;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import ru.nnproject.vikaui.utils.TextBreaker;
import vikamobilebase.VikaUtils;
import vikatouch.base.IconsManager;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.MsgItem;
import vikatouch.base.settings.Settings;
import vikatouch.base.utils.TextEditor;
import vikatouch.base.utils.url.URLBuilder;

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
	private boolean textboxSelected;
	private String[] inputedTextToDraw;
	private boolean inputChanged;
	private JSONObject json;
	private JSONObject chatSettings;
	
	private boolean scrolledDown = false;
	private int inputBoxH = 48;
	private int inputedLinesCount = 0;
	
	private int selectedMsg = 0;
	// 0 - сообщения, 1 - прикреп, 2 - поле, 3 - смайлы, 4 - отправка
	private byte buttonSelected = 0;
	
	
	
	public static Hashtable profileNames = new Hashtable();
	
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
			messagesDialog();
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

					messagesChat();
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
				messagesDialog();
			}
		}
		
		System.gc();
		scroll = -10000;
		dragging = true;
	}

	private void messagesChat()
	{
		try
		{
			// скачка сообщений
			uiItems = new PressableUIItem[Settings.messagesPerLoad];
			final String x = VikaUtils.download(new URLBuilder("messages.getHistory").addField("peer_id", peerId).addField("extended", 1).addField("count", Settings.messagesPerLoad).addField("offset", 0));
			final JSONArray profiles = new JSONObject(x).getJSONObject("response").getJSONArray("profiles");
			final JSONArray items = new JSONObject(x).getJSONObject("response").getJSONArray("items");
			
			
			for(int i = 0; i < profiles.length(); i++)
			{
				final JSONObject profile = profiles.getJSONObject(i);
				String firstname = profile.optString("first_name");
				String lastname = profile.optString("last_name");
				int id = profile.optInt("id");
				if(id > 0 && firstname != null && !profileNames.containsKey(new Integer(id)))
					profileNames.put(new Integer(id), firstname + " " + lastname);
			}
			for(int i = 0; i < items.length(); i++)
			{
				MsgItem m = new MsgItem(items.getJSONObject(i));
				m.parseJSON();
				int fromId = m.fromid; 

				String name = "user" + fromId;
				Integer ii = new Integer(fromId);
				
				if(profileNames.containsKey(ii))
				{
					name = (String) profileNames.get(ii);
				}
				
				boolean chain = false;
				if(i+1<items.length())
				{
					chain = fromId == items.getJSONObject(i+1).optInt("from_id");
				}
				if(!chain)
				{
					m.name = (m.foreign ? name :"Вы");
				}
				uiItems[uiItems.length-1-i] = m;
				itemsCount = (short) uiItems.length;
			}
		}
		catch (Exception e)
		{
			this.title2 = "Не удалось загрузить сообщения.";
			e.printStackTrace();
		}
	}

	private void messagesDialog()
	{
		try
		{
			// скачка сообщений
			uiItems = new PressableUIItem[Settings.messagesPerLoad];
			final String x = VikaUtils.download(new URLBuilder("messages.getHistory").addField("peer_id", peerId).addField("count", Settings.messagesPerLoad).addField("offset", 0));
			JSONArray json = new JSONObject(x).getJSONObject("response").getJSONArray("items");
			profileNames.put(new Integer(peerId), title);
			for(int i = 0; i<json.length();i++) 
			{
				MsgItem m = new MsgItem(json.getJSONObject(i));
				m.parseJSON();
				int fromId = m.fromid; 
				
				// EXPERIMENTAL
				{
					//if(i==0) { VikaTouch.popup(new InfoPopup(json.getJSONObject(i).toString(), null)); }
				}
				
				boolean chain = false;
				if(i+1<json.length())
				{
					chain = fromId == json.getJSONObject(i+1).optInt("from_id");
				}
				if(!chain)
				{
					m.name = (m.foreign?title:"Вы");
				}
				uiItems[uiItems.length-1-i] = m;
				itemsCount = (short) uiItems.length;
			}
		}
		catch (Exception e)
		{
			this.title2 = "Не удалось загрузить сообщения.";
			e.printStackTrace();
		}
		
	}

	public void draw(Graphics g)
	{
		update(g);
		try 
		{
			drawDialog(g);
			
			g.translate(0, -g.getTranslateY());
			
			drawHeader(g);
			drawTextbox(g);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
		System.out.println(x+" "+y);
		textboxSelected = false;
		if(!dragging)
		{
			if(y > DisplayUtils.height-50)
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
	
	public void press(int key)
	{
		keysMode = true;
		if(key == -1)
		{
			up();
		}
		else if(key == -2)
		{
			down();
		}
		else if(key == -3)
		{
			up();
		}
		else if(key == -4)
		{
			down();
		}
		else if(key == -5)
		{ // ok
			switch (buttonSelected)
			{
			case 0:
				uiItems[currentItem].keyPressed(key);
				break;
			case 1:
				// прикреп
				break;
			case 2:
				showTextBox();
				break;
			case 3:
				// смайлы
				break;
			case 4:
				send();
				buttonSelected = 2;
				break;
			}
		}
		else if(key == -6)
		{ // lsk
			if(buttonSelected==0)
			{
				buttonSelected = 2;
			}
			else
			{
				buttonSelected = 0;
			}
		}
		else if(key == -7)
		{ // rsk
			VikaTouch.inst.cmdsInst.command(14, this);
		}
		repaint();
	}
	
	protected void down()
	{
		if(buttonSelected == 0)
		{
			try
			{
				uiItems[currentItem].setSelected(false);
			}
			catch (Exception e)
			{ }
			currentItem++;
			if(currentItem >= itemsCount)
			{
				currentItem = 0;
				scrolled += 1900;
			}
			else
				scrolled -= uiItems[currentItem].getDrawHeight();
			uiItems[currentItem].setSelected(true);
		}
		else
		{
			buttonSelected++;
			if(buttonSelected>4) buttonSelected = 4;
		}
	}

	protected void up()
	{
		if(buttonSelected == 0)
		{
			try
			{
				uiItems[currentItem].setSelected(false);
			}
			catch (Exception e) { }
			currentItem--;
			if(currentItem < 0)
			{
				currentItem = (short) (itemsCount-1);
				scrolled -= 1900;
			}
			else
			{
				scrolled += uiItems[currentItem].getDrawHeight();
			}
			try 
			{
				uiItems[currentItem].setSelected(true);
			}
			catch (Exception e) { }
		}
		else
		{
			buttonSelected--;
		}
	}
	
	public void repeat(int key)
	{
		keysMode = true;
		if(key == -1)
		{
			up();
		}
		if(key == -2)
		{
			down();
		}
		repaint();
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
		if(!scrolledDown)
		{
			scrolledDown = true;
			scrolled = -(itemsh);
			currentItem = (short) (uiItems.length-1);
			uiItems[currentItem].setSelected(true);
		}
	}

	private void drawTextbox(Graphics g)
	{
		// DEBUG
		g.drawString("btn:"+buttonSelected+" msg"+currentItem, 100, 100, 0);
		
		// расчёты и обработка текста
		int m = 4; // margin
		int dw = DisplayUtils.width; int dh = DisplayUtils.height;
		Font font = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		g.setFont(font);
		
		if(inputChanged)
		{
			try
			{
				inputedTextToDraw = TextBreaker.breakText(inputText, false, null, true, DisplayUtils.width - 150);
				inputChanged = false;
				if(inputedTextToDraw != null)
				{
					for(inputedLinesCount = 0; inputedTextToDraw[inputedLinesCount]!=null; inputedLinesCount++) { }
				}
				else
				{
					inputedLinesCount = 0;
				}
			}
			catch (Exception e)
			{
				inputedLinesCount = 0;
			}
			inputBoxH = Math.max(48, font.getHeight()*inputedLinesCount+m*2);
		}
		
		//рендер бокса
		ColorUtils.setcolor(g, -8);
		g.fillRect(0, dh - inputBoxH - 1, dw, 1);
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, dh - inputBoxH, dw, inputBoxH);
		
		if(inputedLinesCount == 0)
		{
			if(buttonSelected == 2)
			{
				ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
				g.drawString("Нажмите ОК для ввода", 48, dh-24-font.getHeight()/2, 0);
			}
			else
			{
				ColorUtils.setcolor(g, ColorUtils.OUTLINE);
				g.drawString("Введите сообщение...", 48, dh-24-font.getHeight()/2, 0);
			}
		}
		else
		{
			ColorUtils.setcolor(g, ColorUtils.TEXT);
			int currY = dh - inputBoxH + m;
			
			for(int i = 0; i < inputedLinesCount; i++)
			{
				if(inputedTextToDraw[i] == null) continue;
				
				g.drawString(inputedTextToDraw[i], 48, currY, 0);
				currY += font.getHeight();
			}
			
		}
		
		g.drawImage((buttonSelected == 1?IconsManager.ico:IconsManager.selIco)[IconsManager.ATTACHMENT], 12, DisplayUtils.height - 36, 0);
		g.drawImage((buttonSelected == 3?IconsManager.ico:IconsManager.selIco)[IconsManager.STICKERS], DisplayUtils.width - 86, DisplayUtils.height - 36, 0);
		g.drawImage((buttonSelected == 4?IconsManager.ico:IconsManager.selIco)[inputedLinesCount==0?IconsManager.VOICE:IconsManager.SEND], DisplayUtils.width - 40, DisplayUtils.height - 36, 0);
		
		if(keysMode) drawKeysTips(g);
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

	private void drawKeysTips(Graphics g)
	{
		String left = buttonSelected==0?"Написать":"Наверх";
		String right = "Назад";
		String ok = (new String[] {"Действия", "Прикрепить", "Клавиатура", "Стикеры", "Отправить"})[buttonSelected];
		Font f = Font.getFont(0, 0, Font.SIZE_SMALL);
		int h = f.getHeight();
		int y = DisplayUtils.height-h;
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, y, DisplayUtils.width, h);
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.fillRect(0, y-1, DisplayUtils.width, 1);
		
		int o = 4;
		g.drawString(left, o, y, 0);
		g.drawString(right, DisplayUtils.width-(o+f.stringWidth(right)), y, 0);
		g.drawString(ok, DisplayUtils.width/2-(f.stringWidth(ok)/2), y, 0);
	}
}
