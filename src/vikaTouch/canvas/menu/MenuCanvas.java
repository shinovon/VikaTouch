package vikaTouch.canvas.menu;

import java.io.IOException;
import java.util.Date;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.canvas.MainCanvas;
import vikaTouch.newbase.JSONBase;
import vikaTouch.newbase.URLBuilder;
import vikaUI.ColorUtils;
import vikaUI.DisplayUtils;
import vikaUI.VikaCanvas;

public class MenuCanvas extends MainCanvas {

	public static Image logoImg;
	public static Image menuImg;
	public static Image dialImg;
	private static Image profileimg;
	private static Image friendimg;
	private static Image groupimg;
	public static String name;
	public static boolean hasAva;
	public static String lastname;
	public static String avaurl;
	private static Image musicimg;
	private static Image videosimg;
	private static Image photosimg;
	private static int[] itemscmd = {4, 5, 6, 7, 8, 9, -1};
	public static Image dialImg2;
	public static Image docsimg;
	private Image exit;
	private Image settingsImg;
	private int selectedBtn;
	private int btnsLen = 8;

	public MenuCanvas()
	{
		super();
		itemsCount = 7;
		try {
			switch(DisplayUtils.idispi)
			{
				default:
				case DisplayUtils.DISPLAY_ALBUM:
				case DisplayUtils.DISPLAY_PORTRAIT:
				case DisplayUtils.DISPLAY_E6:
				{

					menuImg = Image.createImage("/menuo.png");
					if(ColorUtils.isTemnaya)
					{
						dialImg = Image.createImage("/msg1.png");
						newsImg = Image.createImage("/lenta1.png");
						logoImg = Image.createImage("/vikahead.png");
						friendimg = Image.createImage("/friend1.png");
					}
					else
					{
						dialImg = Image.createImage("/msg.png"); 
						newsImg = Image.createImage("/lenta.png");
						logoImg = Image.createImage("/vikahead.jpg");
						friendimg = Image.createImage("/friend.png");
					}
					profileimg = Image.createImage("/ava.png");
					groupimg = Image.createImage("/group.png");
					musicimg = Image.createImage("/music.png");
					videosimg = Image.createImage("/video.png");
					photosimg = Image.createImage("/fotki.png");
					docsimg = Image.createImage("/doc.png");
					dialImg2 = Image.createImage("/msgh.png");
					exit = Image.createImage("/exit.png");
					settingsImg = Image.createImage("/settings.png");
					break;
				}
				
				case DisplayUtils.DISPLAY_S40:
				case DisplayUtils.DISPLAY_ASHA311:
				case DisplayUtils.DISPLAY_EQWERTY:
				{

					profileimg = Image.createImage("/ava25.png");
					logoImg = Image.createImage("/vikaheadsmall.png");
					groupimg = Image.createImage("/group12.png");
					friendimg = Image.createImage("/friend25.png");
					exit = VikaUtils.resize(Image.createImage("/exit.png"), 12, 12);
					settingsImg = VikaUtils.resize(Image.createImage("/settings.png"), 12, 12);
					menuImg = VikaUtils.resize(Image.createImage("/menuo.png"), 10, 9);
					dialImg = VikaUtils.resize(Image.createImage("/msg.png"), 12, 12);
					newsImg = VikaUtils.resize(Image.createImage("/lenta.png"), 11, 12);
					dialImg2 = VikaUtils.resize(Image.createImage("/msgh.png"), 18, 14);
					break;
				}
			}
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Не удалось загрузить одну или несколько элементов меню!");
		}
		if(VikaTouch.DEMO_MODE)
		{
			name = "Арман";
			lastname = "Джусупгалиев";
			try
			{
				profileimg = DisplayUtils.resizeava(Image.createImage("/camera.png"));
				hasAva = true;
			}
			catch (Exception e)
			{
				
			}
		}
		if(VikaTouch.userId != null)
		{
			try
			{
				if((avaurl == null && hasAva && profileimg != null) || name == null || name == "null" || name == "" || avaurl == "" || !VikaTouch.offlineMode)
				{
					String var10 = VikaUtils.download(new URLBuilder("users.get")
						.addField("user_ids", VikaTouch.userId)
						.addField("fields", "photo_id,verified,sex,bdate,city,country,home_town,has_photo,photo_50"));
					final JSONObject profileobj = new JSONObject(var10).getJSONArray("response").getJSONObject(0);
					name = profileobj.optString("first_name");
					lastname = profileobj.optString("last_name");
					avaurl = JSONBase.fixJSONString(profileobj.optString("photo_50"));
					hasAva = profileobj.optInt("has_photo") == 1;
				}
				if(hasAva && avaurl != null && avaurl != "" && avaurl != "null")
				{
					try
					{
						profileimg = DisplayUtils.resizeava(VikaUtils.downloadImage(avaurl));
					}
					catch (Exception e)
					{
						if(!VikaTouch.offlineMode)
							VikaTouch.error(e, "Не удалось получить аватарку профиля!");
						hasAva = false;
					}
				}
			}
			catch (StringIndexOutOfBoundsException var19)
			{
				VikaTouch.error(var19, "Получение информации о профиле");
			}
			catch (NullPointerException var19)
			{
				VikaTouch.error(var19, "Получение информации о профиле");
			}
			catch (Exception var19)
			{
				VikaTouch.error(var19, "Получение информации о профиле");
			}
		}
		else
		{
			VikaTouch.error("Обратите внимание! USER_ID равен null!", false);
		}
	}

	protected final void up()
	{
		selectedBtn--;
		if(selectedBtn < 0)
			selectedBtn = 0;
		scroll += oneitemheight;
		
	}
	
	protected final void down()
	{
		selectedBtn++;
		if(selectedBtn >= btnsLen)
			selectedBtn = btnsLen - 1;
		if(selectedBtn > 3)
			scroll -= oneitemheight;
	}
	
	public final void keyPressed(int key)
	{
		keysMode = true;
		if(key == -5)
		{
			if(selectedBtn == 0)
			{
				VikaTouch.inst.cmdsInst.commandAction(13, this);
			}
			else
			{
				VikaTouch.inst.cmdsInst.commandAction(itemscmd[selectedBtn - 1], this);
			}
		}
		else
			super.keyPressed(key);
		repaint();
	}

	public final void paint(Graphics g)
	{
		{
			switch(DisplayUtils.idispi)
			{
				case DisplayUtils.DISPLAY_ALBUM:
				case DisplayUtils.DISPLAY_PORTRAIT:
				{
					//Nokia N8, E7, C7, C6, N97, 5800, и т.д.. Портретная
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
					if(profileimg != null)
					{
						if(hasAva)
						{
							g.drawImage(profileimg, 16, 71, 0);
							
							//Обрезка
							
							ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
							
							g.drawRect(15, 120, 51, 2);
							g.drawRect(15, 70, 51, 1);
							
							g.drawRect(16, 71, 1, 14);
							g.drawRect(16, 71, 14, 1);
							g.drawRect(17, 72, 11, 1);
							g.drawRect(17, 82, 1, 1);
							g.fillTriangle(17, 73, 17, 82, 28, 73);
							
							g.drawRect(65, 72, 1, 14);
							g.drawRect(52, 72, 14, 1);
							g.drawRect(64, 83, 1, 1);
							g.drawRect(54, 73, 1, 1);
							g.fillTriangle(64, 73, 64, 82, 55, 73);
							
							g.drawRect(65, 72, 1, 14);
							g.drawRect(52, 72, 14, 1);
							g.drawRect(64, 83, 1, 1);
							g.drawRect(54, 73, 1, 1);
							g.drawRect(64, 73, 1, 10);
							g.fillTriangle(64, 73, 64, 82, 55, 73);
							
							g.drawRect(16, 106, 1, 14);
							g.drawRect(16, 119, 14, 1);
							g.drawRect(17, 118, 11, 1);
							g.drawRect(17, 108, 1, 1);
							g.fillTriangle(17, 118, 17, 108, 28, 118);
							
							g.drawArc(15, 70, 51, 51, 0, 360);
							
							g.drawRect(65, 106, 1, 14);
							g.drawRect(54, 119, 12, 1);
							g.drawLine(52, 112, 54, 110);
							g.drawLine(61, 110, 64, 113);
							g.drawRect(64, 108, 1, 12);
							g.drawRect(63, 118, 1, 1);
							g.drawRect(61, 108, 2, 1);
							
							g.drawArc(51, 108, 14, 14, 0, 360);
							g.drawArc(52, 109, 13, 13, 0, 360);
							
							//Онлайн
							ColorUtils.setcolor(g, ColorUtils.ONLINE);
							g.fillArc(53, 110, 11, 11, 0, 360);
						}
						else
						{
							g.drawImage(profileimg, 15, 70, 0);
						}
					}
					/*if(online != null)
					{
						g.drawImage(online, 54, 108, 0);
					}*/

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
					if(friendimg != null)
					{
						g.drawImage(friendimg, 16, 162, 0);
					}

					if(groupimg != null)
					{
						g.drawImage(groupimg, 17, 216, 0);
					}

					if(musicimg != null)
					{
						g.drawImage(musicimg, 18, 264, 0);
					}

					if(videosimg != null)
					{
						g.drawImage(videosimg, 18, 314, 0);
					}

					if(photosimg != null)
					{
						g.drawImage(photosimg, 17, 364, 0);
					}

					if(docsimg != null)
					{
						g.drawImage(docsimg, 18, 414, 0);
					}

					if(exit != null)
					{
						g.drawImage(exit, 20, 466, 0);
					}
					
					ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString(name+" "+lastname, 82, 80, 0);
					if(keysMode && selectedBtn == 1)
						ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
					else
						ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString("Друзья", 56, 158, 0);
					if(keysMode && selectedBtn == 2)
						ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
					else
						ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString("Сообщества", 56, 208, 0);
					if(keysMode && selectedBtn == 3)
						ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
					else
						ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString("Музыка", 56, 258, 0);
					if(keysMode && selectedBtn == 4)
						ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
					else
						ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString("Видео", 56, 308, 0);
					if(keysMode && selectedBtn == 5)
						ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
					else
						ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString("Фотографии", 56, 358, 0);
					if(keysMode && selectedBtn == 6)
						ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
					else
						ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString("Документы", 56, 408, 0);
					if(keysMode && selectedBtn == 7)
						ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
					else
						ColorUtils.setcolor(g, ColorUtils.TEXT);
					g.drawString("Выход", 56, 458, 0);
					//g.drawString("", 56, 408, 0);
						
					g.translate(0,-g.getTranslateY());
						
					if(DisplayUtils.idispi == DisplayUtils.DISPLAY_PORTRAIT)
					{
						ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
						g.fillRect(0, 0, 360, 58);
						ColorUtils.setcolor(g, -3);
						g.fillRect(0, 590, 360, 50);
	
						if(logoImg != null)
						{
							g.drawImage(logoImg, 2, 2, 0);
						}
						
						if(menuImg != null)
						{
							g.drawImage(menuImg, 304, 606, 0);
						}
						
						if(newsImg != null)
						{
							g.drawImage(newsImg, 37, 603, 0);
						}
						
						if(settingsImg != null)
						{
							g.drawImage(settingsImg, 325, 18, 0);
						}
						
						if(keysMode && selectedBtn == 0)
						{
							ColorUtils.setcolor(g, ColorUtils.TEXTCOLOR1);
							g.drawRect(325, 18, 24, 24);
						}
						
						if(VikaTouch.unreadCount > 0)
						{
	
							if(dialImg2 != null)
							{
								g.drawImage(dialImg2, 168, 599, 0);
								g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
								g.drawString("" + VikaTouch.unreadCount, 191, 598, 0);
							}
							else if(dialImg != null)
							{
								g.drawImage(dialImg, 168, 604, 0);
							}
						}
						else
						{
							if(dialImg != null)
							{
								g.drawImage(dialImg, 168, 604, 0);
							}
						}
					}
					else if(DisplayUtils.idispi == DisplayUtils.DISPLAY_ALBUM)
					{
						ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
						g.fillRect(0, 0, 640, 58);
						ColorUtils.setcolor(g, -3);
						g.fillRect(0, 310, 640, 50);

						if(logoImg != null)
						{
							g.drawImage(MenuCanvas.logoImg, 2, 2, 0);
						}
						
						if(newsImg != null)
						{
							g.drawImage(newsImg, 36, 323, 0);
						}
						
						if(settingsImg != null)
						{
							g.drawImage(settingsImg, 605, 18, 0);
						}
						
						if(keysMode && selectedBtn == 0)
						{
							ColorUtils.setcolor(g, ColorUtils.TEXTCOLOR1);
							g.drawRect(605, 18, 24, 24);
						}
						
						if(VikaTouch.unreadCount > 0)
						{

							if(dialImg2 != null)
							{
								g.drawImage(MenuCanvas.dialImg2, 308, 319, 0);
								g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
								g.drawString("" + VikaTouch.unreadCount, 330, 318, 0);
							}
							else if(dialImg != null)
							{
								g.drawImage(dialImg, 308, 324, 0);
							}
						}
						else
						{
							if(dialImg != null)
							{
								g.drawImage(dialImg, 308, 324, 0);
							}
						}

						if(menuImg != null)
						{
							g.drawImage(menuImg, 584, 326, 0);
						}
						break;
					}
					break;
				}

				case DisplayUtils.DISPLAY_EQWERTY:
				case DisplayUtils.DISPLAY_S40:
				case DisplayUtils.DISPLAY_ASHA311:
				{
					//S40, bada, asha 311
					
					update(g);
					
					ColorUtils.setcolor(g, -7);
					g.fillRect(0, 66, 240, 4);
					ColorUtils.setcolor(g, -8);
					g.fillRect(0, 68, 240, 2);
					ColorUtils.setcolor(g, -4);
					g.fillRect(0, 29, 240, 1);
					ColorUtils.setcolor(g, -5);
					g.fillRect(0, 30, 240, 1);
					ColorUtils.setcolor(g, -6);
					g.fillRect(0, 31, 240, 1);
					ColorUtils.setcolor(g, -9);
					g.fillRect(0, 294, 240, 1);
					g.fillRect(0, 70, 240, 1);
					

					if(profileimg != null)
					{
						if(hasAva)
						{
							g.drawImage(profileimg, 8, 35, 0);
							ColorUtils.setcolor(g, ColorUtils.ONLINE);
							g.fillArc(27, 54, 6, 6, 0, 360);
						}
						else
						{
							g.drawImage(profileimg, 8, 35, 0);
						}
					}
					
					if(keysMode)
					{
						ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
						if(selectedBtn > 0)
						{
							g.fillRect(0, 70 + (oneitemheight * (selectedBtn - 1)), 240, oneitemheight);
						}
					}

					if(friendimg != null)
					{
						g.drawImage(friendimg, 10, 83, 0);
					}

					if(groupimg != null)
					{
						g.drawImage(groupimg, 9, 108, 0);
					}
					
					ColorUtils.setcolor(g, 5);
					g.setFont(Font.getFont(0, 0, 8));
					
					g.drawString(name + " " + lastname, 41, 40-8,0);
					
					g.drawString("Друзья", 28, 72, 0);
					g.drawString("Сообщества", 28, 96, 0);
					g.drawString("Музыка", 29, 120, 0);
					g.drawString("Видео", 29, 144, 0);
					g.drawString("Фотографии", 29, 168, 0);
					g.drawString("Документы", 29, 192, 0);
					g.drawString("Выход", 29, 216, 0);
					
					g.translate(0,-g.getTranslateY());

					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, 240, 30);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, DisplayUtils.height - 25, 240, 25);

					if(logoImg != null)
					{
						g.drawImage(logoImg, 1, 1, 0);
					}
					
					if(settingsImg != null)
					{
						g.drawImage(settingsImg, 228, 9, 0);
					}

					if(exit != null)
					{
						g.drawImage(exit, 9, 209, 0);
					}
					
					if(menuImg != null)
					{
						g.drawImage(menuImg, 212, 303, 0);
					}
					
					if(newsImg != null)
					{
						g.drawImage(newsImg, 18, 301, 0);
					}
					
					if(VikaTouch.unreadCount > 0)
					{
						if(dialImg2 != null)
						{
							g.drawImage(dialImg2, 114, 299, 0);
							g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
							g.drawString(""+VikaTouch.unreadCount, 126, 300, 0);
						}
						else if(dialImg != null)
						{
							g.drawImage(dialImg, 114, 302, 0);
						}

					}
					else
					{
						if(dialImg != null)
						{
							g.drawImage(dialImg, 114, 302, 0);
						}
					}
					
					break;
				}
				
				case DisplayUtils.DISPLAY_UNDEFINED:
					
				default:
				{

					ColorUtils.setcolor(g, 2);
					g.drawString("Ваше "+DisplayUtils.idispi, 2, 0, 0);
					g.drawString("разрешение", 2, 15, 0);
					g.drawString("экрана", 2, 30, 0);
					g.drawString("не", 2, 45, 0);
					g.drawString("поддерживается", 2, 60, 0);
				}
			}
		}
	}

	public final void pointerReleased(int x, int y)
	{
		if(!dragging)
		{
			switch(DisplayUtils.idispi)
			{
				case DisplayUtils.DISPLAY_PORTRAIT:
				{
					if(y > 58 && y < 590)
					{
						for(int i = 0; i < itemsCount; i++)
						{
							int y1 = scrolled + 140 + (i * oneitemheight);
							int y2 = y1 + oneitemheight;
							if(y > y1 && y < y2)
							{
								VikaTouch.inst.cmdsInst.commandAction(itemscmd[i], this);
								break;
							}
							
						}
					}
					break;
				}

				case DisplayUtils.DISPLAY_S40:
				case DisplayUtils.DISPLAY_ASHA311:
				case DisplayUtils.DISPLAY_EQWERTY:
				{
					if(y > 30 && y < DisplayUtils.height - 25)
					{
						for(int i = 0; i < itemsCount; i++)
						{
							int y1 = scrolled + 70 + (i * oneitemheight);
							int y2 = y1 + oneitemheight;
							if(y > y1 && y < y2)
							{
								VikaTouch.inst.cmdsInst.commandAction(itemscmd[i], this);
								break;
							}
							
						}
					}
					break;
				}
				case DisplayUtils.DISPLAY_ALBUM:
				{
					if(y > 58 && y < 310)
					{
						for(int i = 0; i < itemsCount; i++)
						{
							int y1 = scrolled + 140 + (i * oneitemheight);
							int y2 = y1 + oneitemheight;
							if(y > y1 && y < y2)
							{
								VikaTouch.inst.cmdsInst.commandAction(itemscmd[i], this);
								break;
							}
							
						}
					}
					break;
				}
				
				default:
				{
					
					break;
				}
			}
		}
		super.pointerReleased(x, y);
	}

}
