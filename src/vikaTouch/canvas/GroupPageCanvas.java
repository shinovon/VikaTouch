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
		itemsCount = 20;
		this_id = id; Load();
	}
	
	public void Load() {
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();

		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("groups.getById").addField("group_id", this_id).addField("fields", "description,contacts,members_count,counters,status,links,fixed_post,site,ban_info"));
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
							ava = VikaUtils.downloadImage(JSONBase.fixJSONString(res.optString("photo_50")));
						} catch (Exception e) { }
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
			}
		};

		downloaderThread.start();
		
		uiItems = new OptionItem[12];
		uiItems[0] = new OptionItem(this, "Участники ("+membersCount+")", IconsManager.GROUPS, 0, 50);
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

		if(keysMode)
		{
			ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
			if(selectedBtn > 0)
			{
				g.fillRect(0, 140 + (oneitemheight * (selectedBtn - 1)), 650, oneitemheight);
			}
		}
		Stc(g, 1);
		g.drawImage(IconsManager.ico[IconsManager.GROUPS], 16, 154, 0);
		g.drawString("Участники", 56, 158, 0);
		Stc(g, 2);
		g.drawImage(IconsManager.ico[isMember?IconsManager.CLOSE:IconsManager.ADD], 16, 204, 0);
		g.drawString(isMember?"Выйти из группы":"Вступить в группу", 56, 208, 0);
		Stc(g, 3);
		g.drawString(canMsg?"Написать сообщение":"[вы не можете отправлять сообщения]", 56, 258, 0);
		Stc(g, 4);
		g.drawImage(IconsManager.ico[IconsManager.NEWS], 16, 304, 0);
		g.drawString("Стена", 56, 308, 0);
		Stc(g, 5);
		g.drawImage(IconsManager.ico[IconsManager.PHOTOS], 16, 354, 0);
		g.drawString("Фотографии", 56, 358, 0);
		Stc(g, 6);
		g.drawString("Музыка", 56, 408, 0);
		Stc(g, 7);
		g.drawString("Видео", 56, 458, 0);
		Stc(g, 8);
		g.drawString("Музыка", 56, 508, 0);
		Stc(g, 9);
		g.drawImage(IconsManager.ico[IconsManager.DOCS], 16, 554, 0);
		g.drawString("Документы", 56, 558, 0);
		Stc(g, 10);
		g.drawString("Обсуждения", 56, 608, 0);
		Stc(g, 11);
		g.drawString("Контакты", 56, 658, 0);
		Stc(g, 12);
		g.drawString("Ссылки", 56, 708, 0);
		Stc(g, 13);
		g.drawString("Сайт", 56, 758, 0);
			
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
