package vikatouch.base;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.InfoPopup;
import ru.nnproject.vikaui.VikaScreen;
import vikatouch.screens.AboutScreen;
import vikatouch.screens.ChatScreen;
import vikatouch.screens.DialogsScreen;
import vikatouch.screens.GroupPageScreen;
import vikatouch.screens.LoginScreen;
import vikatouch.screens.NewsScreen;
import vikatouch.screens.SettingsScreen;
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
							if(VikaTouch.friendsScr == null)
								VikaTouch.friendsScr = new FriendsScreen();
							VikaTouch.friendsScr.LoadFriends(0,0,null);
							VikaTouch.setDisplay(VikaTouch.friendsScr);
						}
						break;
					}
					case 5:
					{
						//Группы
						if(s instanceof MenuScreen)
						{
							if(VikaTouch.grScr == null)
								VikaTouch.grScr = new GroupsScreen();
							VikaTouch.grScr.loadGroups(0, Integer.parseInt(VikaTouch.userId), null);
							VikaTouch.setDisplay(VikaTouch.grScr);
						}
						break;
					}
					case 6:
					{
						//Музыка
						if(s instanceof MenuScreen)
						{
							VikaTouch.popup(new InfoPopup("Функционал музыки ещё не реализован. Следите за обновлениями.",null,"Музыка","Назад"));
						}
						break;
					}
					case 7:
					{
						//Видео
						if(s instanceof MenuScreen)
						{
							VikaTouch.popup(new InfoPopup("Функционал видео в разработке и может (будет) не работать. ",new Thread() {
								public void run() {
							
									if(VikaTouch.videosCanv == null)
										VikaTouch.videosCanv = new VideosScreen();
									VikaTouch.videosCanv.load(0,0,null);
									VikaTouch.setDisplay(VikaTouch.videosCanv);
								}
							},null,"Продолжить"));
							
						}
						break;
					}
					case 8:
					{
						//Фотки
						if(s instanceof MenuScreen)
						{
							VikaTouch.popup(new InfoPopup("Функционал фотографий ещё не реализован. Следите за обновлениями.",null,"Фото","Назад"));
							/*
							if(VikaTouch.photosCanv == null)
								VikaTouch.photosCanv = new PhotosScreen();
							VikaTouch.setDisplay(VikaTouch.photosCanv);*/
						}
						break;
					}
					case 9:
					{
						//Документы
						if(s instanceof MenuScreen)
						{
							if(VikaTouch.docsScr == null)
								VikaTouch.docsScr = new DocsScreen();
							VikaTouch.docsScr.loadDocs(0, 0, null);
							VikaTouch.setDisplay(VikaTouch.docsScr);
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
		if(s instanceof SettingsScreen)
		{
			if(VikaTouch.menuScr != null)
			{
				VikaTouch.setDisplay(VikaTouch.menuScr);
			}
			else
			{
				VikaTouch.setDisplay(VikaTouch.loginScr);
			}
		}
		if(s instanceof DocsScreen || s instanceof AboutScreen || s instanceof GroupsScreen || s instanceof VideosScreen || s instanceof FriendsScreen || s instanceof PhotosScreen)
		{
			VikaTouch.setDisplay(VikaTouch.menuScr);
		}
		if(s instanceof ChatScreen)
		{
			VikaTouch.setDisplay(VikaTouch.dialogsScr);
		}
		if(s instanceof GroupPageScreen)
		{
			VikaTouch.setDisplay(VikaTouch.grScr);
		}
	}

	protected void news(VikaScreen s)
	{
		if(!(s instanceof NewsScreen))
		{
			VikaTouch.loading = true;
			
			if(VikaTouch.newsScr == null)
				VikaTouch.newsScr = new NewsScreen();
			VikaTouch.setDisplay(VikaTouch.newsScr);
		}
	}

	protected void dialogs(VikaScreen s)
	{
		if(!(s instanceof DialogsScreen))
		{
			VikaTouch.loading = true;

			if(VikaTouch.dialogsScr == null)
				VikaTouch.dialogsScr = new DialogsScreen();
			VikaTouch.setDisplay(VikaTouch.dialogsScr);
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
				if(VikaTouch.menuScr == null)
					VikaTouch.menuScr = new MenuScreen();
				VikaTouch.setDisplay(VikaTouch.menuScr);
			}
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_DOCSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.docsScr);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_PHOTOSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.photosScr);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_FRIENDSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.friendsScr);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_GROUPSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.grScr);
		}
		else if(MenuScreen.lastMenu == DisplayUtils.CANVAS_VIDEOSLIST)
		{
			VikaTouch.setDisplay(VikaTouch.videosScr);
		}
	}

}
