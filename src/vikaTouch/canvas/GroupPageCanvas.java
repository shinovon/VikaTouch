package vikaTouch.canvas;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.IconsManager;
import vikaTouch.base.VikaUtils;
import vikaTouch.canvas.menu.DocsCanvas;
import vikaTouch.canvas.menu.MenuCanvas;
import vikaTouch.newbase.JSONBase;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.items.FriendItem;
import vikaTouch.newbase.items.OptionItem;
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;
import vikaUI.PressableUIItem;

public class GroupPageCanvas extends MainCanvas implements IMenu {
	
	public int this_id;
	
	// group fields
	public String name;
	public String link;
	public String status;
	public boolean isMember;
	public boolean isAdmin;
	public int membersCount;
	public String description;
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
	private int selectedBtn;
	private int btnsLen = 8;
	public static Thread downloaderThread;
	
	public GroupPageCanvas(int id) {
		this.menuImg = MenuCanvas.menuImg;
		this.newsImg = VikaTouch.menuCanv.newsImg;
		this_id = id; Load();
	}
	
	public void Load() {
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();
		System.gc();
		final GroupPageCanvas thisC = this;
		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("groups.getById").addField("group_id", this_id)
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
						JSONObject counters = res.getJSONObject("counters");
						System.out.println(counters.toString());
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
						uiItems[5] = new OptionItem(thisC, "Фотографии ("+photos+")", IconsManager.PHOTOS, 5, 50);
						uiItems[6] = new OptionItem(thisC, "Музыка ("+music+")", IconsManager.MUSIC, 6, 50);
						uiItems[7] = new OptionItem(thisC, "Видео ("+videos+")", IconsManager.VIDEOS, 7, 50);
						uiItems[8] = new OptionItem(thisC, "Документы ("+docs+")", IconsManager.DOCS, 8, 50);
						uiItems[9] = new OptionItem(thisC, "Обсуждения ("+topics+")", IconsManager.COMMENTS, 9, 50);
						uiItems[10] = new OptionItem(thisC, "Сайт", IconsManager.REPOST, 10, 50);
						uiItems[11] = new OptionItem(thisC, "Ссылки", IconsManager.REPOST, 11, 50);
						uiItems[12] = new OptionItem(thisC, "Контакты", IconsManager.GROUPS, 11, 50);
					}
					catch (JSONException e)
					{
						e.printStackTrace();
						VikaTouch.error(e, "Парс группы");
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
					VikaTouch.error(e, "Загрузка группы");
				}
				VikaTouch.loading = false;
				System.gc();
			}
		};

		downloaderThread.start();
	}
	
	public void draw(Graphics g) {
		update(g);
		ColorUtils.setcolor(g, -2);
		g.fillRect(0, 132, 640, 8);
		ColorUtils.setcolor(g, -10);
		g.fillRect(0, 133, 640, 1);
		ColorUtils.setcolor(g, -11);
		g.fillRect(0, 134, 640, 1);
		ColorUtils.setcolor(g, -7);
		g.fillRect(0, 139, 640, 1);
		ColorUtils.setcolor(g, -12);
		g.fillRect(0, 140, 640, 1);
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
		g.drawRect(0, 140, 360, 50);
		//g.drawRect(0, 58, 20, items);
		for(int d = 0; d<(itemsh/oneitemheight); d++)
		{
			g.drawRect(0, 140+(d*oneitemheight), DisplayUtils.width, 50);
			//g.drawString(""+d/50, 20, 150+d, 0);
		}
		int y = 140; // init offset
		if(uiItems!=null) for (int i=0;i<uiItems.length;i++)
		{
			if(uiItems[i]!=null) {
				uiItems[i].paint(g, y, scrolled);
				y+=uiItems[i].getDrawHeight();
			}
		}
			
		drawHeaders(g, link==null?"Группа":link);
		
	
	}
	
	// Switch Text Color
	private void Stc(Graphics g, int n) {
		if(keysMode && selectedBtn == n)
			ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		else
			ColorUtils.setcolor(g, ColorUtils.TEXT);
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
						System.out.println();
						System.out.println(i);
						OnItemPress(i);
						break;
					}
					
				}
			}
			
		}
		super.release(x, y);
	}

	public void OnItemPress(int i) {
		switch (i) 
		{
			case 1:
				VikaTouch.loading = true;
				(new Thread() {
					public void run() {
				
						if(isMember)
						{
							VikaUtils.download(new URLBuilder("groups.leave").addField("group_id", this_id));
						}
						else
						{
							VikaUtils.download(new URLBuilder("groups.join").addField("group_id", this_id));
						}
						Load();
					}
				}).start();
				break;
			case 8:
				DocsCanvas dc = new DocsCanvas();
				VikaTouch.setDisplay(dc);
				dc.LoadDocs(0, -this_id, name);
				break;
		}
	}

	public void OnItemOption(int i) {
		// TODO Auto-generated method stub
		
	}

}
