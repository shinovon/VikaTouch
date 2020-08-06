package vikatouch.screens;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.ConfirmBox;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.IMenu;
import ru.nnproject.vikaui.TextBreaker;
import vikamobilebase.VikaUtils;
import vikatouch.base.ErrorCodes;
import vikatouch.base.IconsManager;
import vikatouch.base.JSONBase;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.OptionItem;
import vikatouch.screens.menu.DocsScreen;
import vikatouch.screens.menu.FriendsScreen;
import vikatouch.screens.menu.GroupsScreen;
import vikatouch.screens.menu.MenuScreen;

public class ProfilePageScreen extends MainScreen implements IMenu {

	public int id;
	public boolean closed;
	// friend fields
	public String name;
	public String link;
	public String status;
	public boolean canBeFriend;
	public byte friendState;
	public Image ava;
	public boolean canMsg;
	public boolean online;
	public int lastSeen;
	
	// counters
	public int docs;
	public int groups;
	public int photos;
	public int videos;
	public int music;
	public int friends;
	
	// system
	public static Thread downloaderThread;
	private boolean friendAdd; // если true, друг добавляется. Если 0, удаляется.
	private String visitStr;
	
	public ProfilePageScreen(int id)
	{
		hasBackButton = true;
		this.menuImg = MenuScreen.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
		this.id = id;
		Load();
	}
	
	public void Load()
	{
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();
		System.gc();
		final ProfilePageScreen thisC = this;
		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("users.get").addField("user_ids", id)
							.addField("fields", "photo_50,online,domain,status,last_seen,common_count,can_write_private_message,can_send_friend_request,is_friend,friend_status,counters"));
					try
					{
						VikaTouch.loading = true;
						JSONObject res = new JSONObject(x).getJSONArray("response").getJSONObject(0);
						
						closed = res.optInt("can_access_closed") == 1;
					
						name = res.optString("first_name") + " " + res.optString("last_name");
						link = res.optString("domain");
						status = res.optString("status");
						canBeFriend = res.optInt("can_send_friend_request") == 1;
						canMsg = res.optInt("can_write_private_message") == 1;
						friendState = (byte)res.optInt("friend_status");
						friendAdd = (friendState==0||friendState==2);
						
						try {
							lastSeen = res.getJSONObject("last_seen").optInt("time");
						}
						catch (Exception e) { }
						online = res.optInt("online") == 1;
						
						if(online)
						{
							visitStr = "Онлайн";
						}
						else
						{
							int now = (int)(System.currentTimeMillis()/1000);
							int r = now - lastSeen;
							if(r<90) 
							{
								visitStr = "Был в сети только что";
							}
							else if(r<90*60)
							{
								visitStr = "Был в сети "+(r/60)+" минут назад";
							}
							else
							{
								visitStr = "Был в сети "+(r/3600)+" часов назад";
							}
						}
						
						try 
						{
							JSONObject counters = res.getJSONObject("counters");
							docs = counters.optInt("docs");
							groups = counters.optInt("groups");
							music = counters.optInt("audios");
							videos = counters.optInt("videos");
							photos = counters.optInt("photos");
							friends = counters.optInt("friends");
						}
						catch (Exception e) {}
						
						try {
							ava = VikaUtils.downloadImage(JSONBase.fixJSONString(res.optString("photo_50")));
						} catch (Exception e) { }
						
						if(closed) 
						{
							itemsCount = 2;
							uiItems = new OptionItem[2];
							uiItems[0] = new OptionItem(thisC, "Это закрытый профиль.", IconsManager.INFO, 0, 50);
						}
						else
						{
							itemsCount = 9;
							uiItems = new OptionItem[9];
							uiItems[0] = new OptionItem(thisC, canMsg?"Написать сообщение":"[нельзя писать]", IconsManager.MSGS, 0, 50);
							
							uiItems[2] = new OptionItem(thisC, "Друзья ("+friends+")", IconsManager.FRIENDS, 2, 50);
							uiItems[3] = new OptionItem(thisC, "Стена", IconsManager.NEWS, 3, 50);
							uiItems[4] = new OptionItem(thisC, groups==0?"[группы скрыты]":"Группы ("+groups+")", IconsManager.GROUPS, 4, 50);
							uiItems[5] = new OptionItem(thisC, photos==0?"[нет фотографий]":"Фотографии ("+photos+")", IconsManager.PHOTOS, 5, 50);
							uiItems[6] = new OptionItem(thisC, music==0?"[нет музыки]":"Музыка ("+music+")", IconsManager.MUSIC, 6, 50);
							uiItems[7] = new OptionItem(thisC, videos==0?"[нет видео]":"Видео ("+videos+")", IconsManager.VIDEOS, 7, 50);
							uiItems[8] = new OptionItem(thisC, docs==0?"[нет документов]":"Документы ("+docs+")", IconsManager.DOCS, 8, 50);
						}
						uiItems[1] = new OptionItem(thisC, (new String[] {"Добавить в друзья","Заявка отправлена (отменить)","Принять заявку","Удалить из друзей"})[friendState],
								(friendState==3||friendState==1)?IconsManager.CLOSE:IconsManager.ADD, 1, 50);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						VikaTouch.error(e, ErrorCodes.GROUPPAGEPARSE);
					}

					VikaTouch.loading = false;
				}
				catch (NullPointerException e)
				{
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					VikaTouch.error(e, ErrorCodes.GROUPPAGELOAD);
				}
				VikaTouch.loading = false;
				System.gc();
			}
		};

		downloaderThread.start();
	}
	
	public void draw(Graphics g)
	{
		int y = 140; // init offset
		itemsh = itemsCount * 50 + y;
		update(g);
		ColorUtils.setcolor(g, -2);
		g.fillRect(0, 132, DisplayUtils.width, 8);
		ColorUtils.setcolor(g, -10);
		g.fillRect(0, 133, DisplayUtils.width, 1);
		ColorUtils.setcolor(g, -11);
		g.fillRect(0, 134, DisplayUtils.width, 1);
		ColorUtils.setcolor(g, -7);
		g.fillRect(0, 139, DisplayUtils.width, 1);
		ColorUtils.setcolor(g, -12);
		g.fillRect(0, 140, DisplayUtils.width, 1);
		if(ava != null)
		{
			g.drawImage(ava, 16, 71, 0);
			g.drawImage(IconsManager.ac, 16, 71, 0);
			ColorUtils.setcolor(g, ColorUtils.ONLINE);
			g.fillArc(16+38, 71+38, 12, 12, 0, 360);
		}
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawString(name==null?"Загрузка...":name, 74, 74, 0);
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
		g.drawString(status==null?"":status, 74, 98, 0);
		
		ColorUtils.setcolor(g, -3);
		g.drawRect(0, 140, DisplayUtils.width, 50);
		if(uiItems!=null)
		{
			for (int i=0;i<uiItems.length;i++)
			{
				if(uiItems[i]!=null) {
					uiItems[i].paint(g, y, scrolled);
					y+=uiItems[i].getDrawHeight();
				}
			}
		}
		g.translate(0, -g.getTranslateY());
		drawHeaders(g, link==null?"Пользователь":link);
	}
	
	public final void release(int x, int y)
	{
		if(!dragging)
		{
			if(y > 58 && y < DisplayUtils.height-50)
			{
				for(int i = 0; i < itemsCount; i++)
				{
					int y1 = scrolled + 140 + (i * oneitemheight);
					int y2 = y1 + oneitemheight;
					if(y > y1 && y < y2)
					{
						onItemPress(i);
						break;
					}
					
				}
			}
		}
		super.release(x, y);
	}

	public void onItemPress(int i)
	{
		switch (i) 
		{
			case 0:
				if(closed) 
				{ } // юзается как алерт, ничего не делаем.
				break;
			case 1:
				VikaTouch.loading = true;
				(new Thread()
				{
					public void run()
					{
						if(friendAdd)
						{
							VikaUtils.download(new URLBuilder("friends.add").addField("user_id", id));
						}
						else
						{
							VikaUtils.download(new URLBuilder("friends.delete").addField("user_id", id));
						}
						Load();
					}
				}
				).start();
				break;
			case 2:
				FriendsScreen fs = new FriendsScreen();
				VikaTouch.setDisplay(fs);
				fs.LoadFriends(0, id, name);
				break;
			case 3:
				// ТЕСТ ОКНА
				activeDialog = new ConfirmBox("Тескт 1","Пояснение",null,null);
				repaint();
				break;
			case 4:
				GroupsScreen gs = new GroupsScreen();
				VikaTouch.setDisplay(gs);
				gs.LoadGroups(0, id, name);
				break;
			case 8:
				if(docs>0) {
					DocsScreen dc = new DocsScreen();
					VikaTouch.setDisplay(dc);
					dc.loadDocs(0, id, name);
				}
				break;
		}
		
	}

	public void onItemOption(int i)
	{
		
	}
}
