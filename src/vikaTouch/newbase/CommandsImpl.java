package vikaTouch.newbase;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.AboutCanvas;
import vikaTouch.canvas.DialogsCanvas;
import vikaTouch.canvas.LoginCanvas;
import vikaTouch.canvas.NewsCanvas;
import vikaTouch.canvas.menu.DocsCanvas;
import vikaTouch.canvas.menu.FriendsCanvas;
import vikaTouch.canvas.menu.GroupsCanvas;
import vikaTouch.canvas.menu.MenuCanvas;
import vikaTouch.canvas.menu.PhotosCanvas;
import vikaTouch.canvas.menu.VideosCanvas;
import vikaUI.DisplayUtils;
import vikaUI.VikaScreen;

public class CommandsImpl
	implements CommandListener
{

    public static final Command close = new Command("Закрыть приложение", 4, 0);

	public void commandAction(Command c, Displayable d)
	{
		if(c == close)
		{
			VikaTouch.inst.destroyApp(false);
		}
	}
	
	public void commandAction(final int i, final VikaScreen s)
	{
		final Thread t = new Thread("Command Thread")
		{
			public void run()
			{
				switch(i)
				{
					case -1:
					{
						//Выход
						VikaTouch.inst.destroyApp(false);
						break;
					}
					case 0:
					{
						//Новости
						news(s);
						break;
					}
					case 1:
					{
						//Сообщения
						dialogs(s);
						break;
					}
					case 2:
					{
						//Меню
						menu(s);
						break;
					}
					case 3:
					{
						//Логин
						VikaTouch.loading = true;
						
						if(s instanceof LoginCanvas)
						{
							VikaTouch.inst.login(LoginCanvas.user, LoginCanvas.pass);
						}

						VikaTouch.loading = false;
						break;
					}
					case 4:
					{
						//Друзья
						if(s instanceof MenuCanvas)
						{
							if(VikaTouch.friendsCanv == null)
								VikaTouch.friendsCanv = new FriendsCanvas();
							if(!VikaTouch.friendsCanv.isReady())
								VikaTouch.friendsCanv.LoadFriends();
							VikaTouch.setDisplay(VikaTouch.friendsCanv);
						}
						break;
					}
					case 5:
					{
						//Группы
						if(s instanceof MenuCanvas)
						{
							if(VikaTouch.grCanv == null)
								VikaTouch.grCanv = new GroupsCanvas();
							if(!VikaTouch.grCanv.isReady())
								VikaTouch.grCanv.LoadGroups();
							VikaTouch.setDisplay(VikaTouch.grCanv);
						}
						break;
					}
					case 6:
					{
						//Музыка
						if(s instanceof MenuCanvas)
						{
							VikaTouch.warn("Данная кнопка не фунцкионирует.");
						}
						break;
					}
					case 7:
					{
						//Видео
						if(s instanceof MenuCanvas)
						{
							if(VikaTouch.videosCanv == null)
								VikaTouch.videosCanv = new VideosCanvas();
							VikaTouch.setDisplay(VikaTouch.videosCanv);
						}
						break;
					}
					case 8:
					{
						//Фотки
						if(s instanceof MenuCanvas)
						{
							if(VikaTouch.photosCanv == null)
								VikaTouch.photosCanv = new PhotosCanvas();
							VikaTouch.setDisplay(VikaTouch.photosCanv);
						}
						break;
					}
					case 9:
					{
						//Документы
						if(s instanceof MenuCanvas)
						{
							if(VikaTouch.docsCanv == null)
								VikaTouch.docsCanv = new DocsCanvas();
							VikaTouch.docsCanv.LoadDocs(0, DocsCanvas.loadDocsCount, 0);
							VikaTouch.setDisplay(VikaTouch.docsCanv);
						}
						break;
					}
					case 10:
					{
						//свайп влево
						if(s instanceof MenuCanvas || s instanceof DocsCanvas  || s instanceof GroupsCanvas || s instanceof VideosCanvas || s instanceof FriendsCanvas || s instanceof PhotosCanvas)
						{
							dialogs(s);
						}
						if(s instanceof DialogsCanvas)
						{
							news(s);
						}
						break;
					}
					case 11:
					{
						
						//свайп вправо
						if(s instanceof DialogsCanvas)
						{
							menu(s);
						}
		
						if(s instanceof NewsCanvas)
						{
							dialogs(s);
						}
						break;
					}
					case 13:
					{
						//Настройки
						if(VikaTouch.about == null)
							VikaTouch.about = new AboutCanvas();
						VikaTouch.setDisplay(VikaTouch.about);
						break;
					}
					case 14:
					{
						//Назад
						back(s);
						break;
					}
					default:
					{
						break;
					}
				}
				Thread.yield();
			}
		};
		t.start();
	}

	protected void back(VikaScreen s)
	{
		if(s instanceof AboutCanvas || s instanceof DocsCanvas  || s instanceof GroupsCanvas || s instanceof VideosCanvas || s instanceof FriendsCanvas || s instanceof PhotosCanvas)
		{
			if(VikaTouch.menuCanv == null)
				VikaTouch.menuCanv = new MenuCanvas();
			VikaTouch.setDisplay(VikaTouch.menuCanv);
		}
	}

	protected void news(VikaScreen s)
	{
		if(!(s instanceof NewsCanvas))
		{
			VikaTouch.loading = true;
			
			if(VikaTouch.newsCanv == null)
				VikaTouch.newsCanv = new NewsCanvas();
			VikaTouch.setDisplay(VikaTouch.newsCanv);
		}
	}

	protected void dialogs(VikaScreen s)
	{
		if(!(s instanceof DialogsCanvas))
		{
			VikaTouch.loading = true;

			if(VikaTouch.dialogsCanv == null)
				VikaTouch.dialogsCanv = new DialogsCanvas();
			VikaTouch.setDisplay(VikaTouch.dialogsCanv);
		}
	}

	protected void menu(VikaScreen s)
	{
		if(MenuCanvas.lastMenu == DisplayUtils.CANVAS_MENU || s instanceof DocsCanvas  || s instanceof GroupsCanvas || s instanceof VideosCanvas || s instanceof FriendsCanvas || s instanceof PhotosCanvas)
		{
			if(!(s instanceof MenuCanvas))
			{
				if(VikaTouch.menuCanv == null)
					VikaTouch.menuCanv = new MenuCanvas();
				VikaTouch.setDisplay(VikaTouch.menuCanv);
			}
		}
		else if(MenuCanvas.lastMenu == DisplayUtils.CANVAS_DOCSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.docsCanv);
		}
		else if(MenuCanvas.lastMenu == DisplayUtils.CANVAS_PHOTOSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.photosCanv);
		}
		else if(MenuCanvas.lastMenu == DisplayUtils.CANVAS_FRIENDSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.friendsCanv);
		}
		else if(MenuCanvas.lastMenu == DisplayUtils.CANVAS_GROUPSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.grCanv);
		}
		else if(MenuCanvas.lastMenu == DisplayUtils.CANVAS_VIDEOSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.videosCanv);
		}
	}

}
