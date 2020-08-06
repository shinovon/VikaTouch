package vikatouch.base;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.VikaScreen;
import vikatouch.screens.AboutScreen;
import vikatouch.screens.ChatScreen;
import vikatouch.screens.DialogsScreen;
import vikatouch.screens.GroupPageScreen;
import vikatouch.screens.LoginScreen;
import vikatouch.screens.NewsScreen;
import vikatouch.screens.menu.DocsScreen;
import vikatouch.screens.menu.FriendsScreen;
import vikatouch.screens.menu.GroupsScreen;
import vikatouch.screens.menu.MenuScreen;
import vikatouch.screens.menu.PhotosScreen;
import vikatouch.screens.menu.VideosScreen;

public class CommandsImpl
	implements CommandListener
{

    public static final Command close = new Command("Закрыть приложение", 4, 0);

	public void commandAction(Command c, Displayable d)
	{
		if(c == close)
		{
			VikaTouch.appInst.destroyApp(false);
		}
	}
	
	public void command(final int i, final VikaScreen s)
	{
		final Thread t = new Thread()
		{
			public void run()
			{
				switch(i)
				{
					case -1:
					{
						//Выход
						VikaTouch.appInst.destroyApp(false);
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
						
						if(s instanceof LoginScreen)
						{
							VikaTouch.inst.login(LoginScreen.user, LoginScreen.pass);
						}

						VikaTouch.loading = false;
						break;
					}
					case 4:
					{
						//Друзья
						if(s instanceof MenuScreen)
						{
							if(VikaTouch.friendsCanv == null)
								VikaTouch.friendsCanv = new FriendsScreen();
							VikaTouch.friendsCanv.LoadFriends(0,0,null);
							VikaTouch.setDisplay(VikaTouch.friendsCanv);
						}
						break;
					}
					case 5:
					{
						//Группы
						if(s instanceof MenuScreen)
						{
							if(VikaTouch.grCanv == null)
								VikaTouch.grCanv = new GroupsScreen();
							VikaTouch.grCanv.LoadGroups(0, Integer.parseInt(VikaTouch.userId), null);
							VikaTouch.setDisplay(VikaTouch.grCanv);
						}
						break;
					}
					case 6:
					{
						//Музыка
						if(s instanceof MenuScreen)
						{
							VikaTouch.warn("Данная кнопка не фунцкионирует.");
						}
						break;
					}
					case 7:
					{
						//Видео
						if(s instanceof MenuScreen)
						{
							if(VikaTouch.videosCanv == null)
								VikaTouch.videosCanv = new VideosScreen();
							VikaTouch.setDisplay(VikaTouch.videosCanv);
						}
						break;
					}
					case 8:
					{
						//Фотки
						if(s instanceof MenuScreen)
						{
							if(VikaTouch.photosCanv == null)
								VikaTouch.photosCanv = new PhotosScreen();
							VikaTouch.setDisplay(VikaTouch.photosCanv);
						}
						break;
					}
					case 9:
					{
						//Документы
						if(s instanceof MenuScreen)
						{
							if(VikaTouch.docsCanv == null)
								VikaTouch.docsCanv = new DocsScreen();
							VikaTouch.docsCanv.loadDocs(0, 0, null);
							VikaTouch.setDisplay(VikaTouch.docsCanv);
						}
						break;
					}
					case 10:
					{
						//свайп влево
						if(s instanceof MenuScreen || s instanceof DocsScreen  || s instanceof GroupsScreen || s instanceof VideosScreen || s instanceof FriendsScreen || s instanceof PhotosScreen)
						{
							dialogs(s);
						}
						if(s instanceof DialogsScreen)
						{
							news(s);
						}
						break;
					}
					case 11:
					{
						
						//свайп вправо
						if(s instanceof DialogsScreen)
						{
							menu(s);
						}
		
						if(s instanceof NewsScreen)
						{
							dialogs(s);
						}
						break;
					}
					case 13:
					{
						//Настройки
						if(VikaTouch.about == null)
							VikaTouch.about = new AboutScreen();
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
		if(s instanceof DocsScreen || s instanceof AboutScreen || s instanceof GroupsScreen || s instanceof VideosScreen || s instanceof FriendsScreen || s instanceof PhotosScreen)
		{
			VikaTouch.setDisplay(VikaTouch.menuCanv);
		}
		if(s instanceof ChatScreen)
		{
			VikaTouch.setDisplay(VikaTouch.dialogsCanv);
		}
		if(s instanceof GroupPageScreen)
		{
			VikaTouch.setDisplay(VikaTouch.grCanv);
		}
	}

	protected void news(VikaScreen s)
	{
		if(!(s instanceof NewsScreen))
		{
			VikaTouch.loading = true;
			
			if(VikaTouch.newsCanv == null)
				VikaTouch.newsCanv = new NewsScreen();
			VikaTouch.setDisplay(VikaTouch.newsCanv);
		}
	}

	protected void dialogs(VikaScreen s)
	{
		if(!(s instanceof DialogsScreen))
		{
			VikaTouch.loading = true;

			if(VikaTouch.dialogsCanv == null)
				VikaTouch.dialogsCanv = new DialogsScreen();
			VikaTouch.setDisplay(VikaTouch.dialogsCanv);
		}
		else
		{
			Dialogs.refreshDialogsList();
		}
	}

	protected void menu(VikaScreen s)
	{
		if(MenuScreen.lastMenu == DisplayUtils.CANVAS_MENU || s instanceof DocsScreen  || s instanceof GroupsScreen || s instanceof VideosScreen || s instanceof FriendsScreen || s instanceof PhotosScreen)
		{
			if(!(s instanceof MenuScreen))
			{
				if(VikaTouch.menuCanv == null)
					VikaTouch.menuCanv = new MenuScreen();
				VikaTouch.setDisplay(VikaTouch.menuCanv);
			}
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_DOCSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.docsCanv);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_PHOTOSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.photosCanv);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_FRIENDSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.friendsCanv);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_GROUPSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.grCanv);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_VIDEOSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.videosCanv);
		}
	}

}
