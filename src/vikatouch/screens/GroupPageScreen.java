package vikatouch.screens;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.ConfirmBox;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.IMenu;
import ru.nnproject.vikaui.PressableUIItem;
import ru.nnproject.vikaui.TextBreaker;
import vikamobilebase.VikaUtils;
import vikatouch.base.ErrorCodes;
import vikatouch.base.IconsManager;
import vikatouch.base.JSONBase;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.FriendItem;
import vikatouch.base.items.OptionItem;
import vikatouch.screens.menu.DocsScreen;
import vikatouch.screens.menu.FriendsScreen;
import vikatouch.screens.menu.MenuScreen;

public class GroupPageScreen extends MainScreen implements IMenu {
	
	public int id;
	
	// group fields
	public String name;
	public String link;
	public String status;
	public boolean isMember;
	public boolean isAdmin;
	public int membersCount;
	public String[] description;
	public String site;
	public Image ava;
	public boolean canMsg;
	// counters
	public int docs;
	public int topics;
	public int photos;
	public int videos;
	public int music;
	
	// system
	private boolean isInfoShown = false;
	public static Thread downloaderThread;
	
	public GroupPageScreen(int id)
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
		final GroupPageScreen thisC = this;
		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("groups.getById").addField("group_id", id)
							.addField("fields", "description,contacts,members_count,counters,status,links,fixed_post,site,ban_info"));
					try
					{
						VikaTouch.loading = true;
						JSONObject res = new JSONObject(x).getJSONArray("response").getJSONObject(0);
						name = res.optString("name");
						link = res.optString("screen_name");
						status = res.optString("status");
						isAdmin = res.optInt("is_admin") == 1;
						isMember = res.optInt("is_member") == 1;
						membersCount = res.optInt("members_count");
						try {
							description = TextBreaker.breakText(res.optString("description"), false, null, true, DisplayUtils.width-32);
						} catch (Exception e) { e.printStackTrace(); }
						site = res.optString("site");
						
						JSONObject counters = res.getJSONObject("counters");
						docs = counters.optInt("docs");
						topics = counters.optInt("topics");
						music = counters.optInt("audios");
						videos = counters.optInt("videos");
						photos = counters.optInt("photos");
						
						try {
							ava = VikaUtils.downloadImage(JSONBase.fixJSONString(res.optString("photo_50")));
						} catch (Exception e) { }
						itemsCount = 13;
						uiItems = new OptionItem[13];
						uiItems[0] = new OptionItem(thisC, "Участники ("+membersCount+")", IconsManager.GROUPS, 0, 50);
						uiItems[1] = new OptionItem(thisC, isMember?"Выйти из группы":"Вступить в группу", 
								isMember?IconsManager.CLOSE:IconsManager.ADD, 1, 50);
						uiItems[2] = new OptionItem(thisC, canMsg?"Написать сообщение":"[нельзя писать]", IconsManager.MSGS, 2, 50);
						uiItems[3] = new OptionItem(thisC, "Стена", IconsManager.NEWS, 3, 50);
						uiItems[4] = new OptionItem(thisC, "Информация", IconsManager.INFO, 4, 50);
						uiItems[5] = new OptionItem(thisC, photos==0?"[нет фотографий]":"Фотографии ("+photos+")", IconsManager.PHOTOS, 5, 50);
						uiItems[6] = new OptionItem(thisC, music==0?"[нет музыки]":"Музыка ("+music+")", IconsManager.MUSIC, 6, 50);
						uiItems[7] = new OptionItem(thisC, videos==0?"[нет видео]":"Видео ("+videos+")", IconsManager.VIDEOS, 7, 50);
						uiItems[8] = new OptionItem(thisC, docs==0?"[нет документов]":"Документы ("+docs+")", IconsManager.DOCS, 8, 50);
						uiItems[9] = new OptionItem(thisC, topics==0?"[нет обсуждений]":"Обсуждения ("+topics+")", IconsManager.COMMENTS, 9, 50);
						uiItems[10] = new OptionItem(thisC, (site==null||site.length()<5)?"[сайт не указан]":"Сайт: "+site, IconsManager.REPOST, 10, 50);
						uiItems[11] = new OptionItem(thisC, "Ссылки", IconsManager.REPOST, 11, 50);
						uiItems[12] = new OptionItem(thisC, "Контакты", IconsManager.GROUPS, 11, 50);
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
		}
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawString(name==null?"Загрузка...":name, 74, 74, 0);
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
		g.drawString(status==null?"":status, 74, 98, 0);
		
		ColorUtils.setcolor(g, -3);
		g.drawRect(0, 140, DisplayUtils.width, 50);
		if(isInfoShown)
		{
			if(description == null)
			{
				isInfoShown = false;
				((OptionItem)uiItems[4]).text = "[описание пусто]";
			}
			Font df = Font.getFont(0, 0, 8);
			g.setFont(df);
			ColorUtils.setcolor(g, 0);
			y+=16;
			for(int i=0; i<description.length; i++)
			{
				if(description[i]!=null) {
					g.drawString(description[i], 16, y, 0);
					y+=df.getHeight();
				}
			}
		}
		else
		{
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
		}
		g.translate(0, -g.getTranslateY());
		drawHeaders(g, link==null?"Группа":link);
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
		if(isInfoShown)
		{
			isInfoShown = false;
		}
		else
		{
			switch (i) 
			{
				case 0:
					FriendsScreen fs = new FriendsScreen();
					VikaTouch.setDisplay(fs);
					fs.LoadFriends(0, -id, name);
					break;
				case 1:
					VikaTouch.canvas.currentAlert = new ConfirmBox(isMember?"Выйти из группы?":"Вступить в группу?",null,
					new Thread()
					{
						public void run()
						{
							VikaTouch.loading = true;
							if(isMember)
							{
								VikaUtils.download(new URLBuilder("groups.leave").addField("group_id", id));
							}
							else
							{
								VikaUtils.download(new URLBuilder("groups.join").addField("group_id", id));
							}
							Load();
						}
					}, null);
					repaint();
					break;
				case 2:
					// сообщение
					break;
				case 3:
					// стена
					break;
				case 4:
					isInfoShown = true;
					break;
				case 8:
					if(docs>0) {
						DocsScreen dc = new DocsScreen();
						VikaTouch.setDisplay(dc);
						dc.loadDocs(0, -id, name);
					}
					break;
				case 10:
					try
					{
						VikaTouch.appInst.platformRequest(site);
					}
					catch(Exception e) { }
					break;
			}
		}
	}

	public void onItemOption(int i)
	{
		
	}

}
