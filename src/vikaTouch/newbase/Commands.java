package vikaTouch.newbase;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.AboutCanvas;
import vikaTouch.canvas.DialogsCanvas;
import vikaTouch.canvas.DocsCanvas;
import vikaTouch.canvas.GroupsCanvas;
import vikaTouch.canvas.LoginCanvas;
import vikaTouch.canvas.MenuCanvas;
import vikaTouch.canvas.NewsCanvas;

public class Commands
	implements CommandListener
{

    public static final Command close = new Command("Закрыть", 4, 0);

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
		
						if(!(s instanceof NewsCanvas))
						{
							VikaTouch.loading = true;
							
							if(VikaTouch.newsCanv == null)
								VikaTouch.newsCanv = new NewsCanvas();
							VikaTouch.setDisplay(VikaTouch.newsCanv);
						}
						break;
					}
					case 1:
					{
						//Сообщения
						
						if(!(s instanceof DialogsCanvas))
						{
							VikaTouch.loading = true;
		
							if(VikaTouch.dialogsCanv == null)
								VikaTouch.dialogsCanv = new DialogsCanvas();
							VikaTouch.setDisplay(VikaTouch.dialogsCanv);
						}
						break;
					}
					case 2:
					{
						//Меню
						
						if(MenuCanvas.lastMenu == DisplayUtils.CANVAS_MENU || s instanceof DocsCanvas /* || s instanceof GroupsCanvas || s instanceof VideosCanvas || s instanceof FriendsCanvas */)
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
						break;
					}
					case 4:
					{
						//Друзья
						if(s instanceof MenuCanvas)
						{
							final Alert alert = new Alert("нажато","друзья", null, AlertType.INFO);
							VikaTouch.setDisplay(alert);
						}
						break;
					}
					case 5:
					{
						if(!(s instanceof GroupsCanvas))
						{
							VikaTouch.loading = true;
		
							if(VikaTouch.grCanv == null)
								VikaTouch.grCanv = new GroupsCanvas();
							if(!GroupsCanvas.isReady())
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
							final Alert alert = new Alert("нажато","музыка", null, AlertType.INFO);
							VikaTouch.setDisplay(alert);
						}
						break;
					}
					case 7:
					{
						//Видео
						if(s instanceof MenuCanvas)
						{
							final Alert alert = new Alert("нажато","видео", null, AlertType.INFO);
							VikaTouch.setDisplay(alert);
						}
						break;
					}
					case 8:
					{
						//Фотки
						if(s instanceof MenuCanvas)
						{
							final Alert alert = new Alert("нажато","фотки", null, AlertType.INFO);
							VikaTouch.setDisplay(alert);
						}
						break;
					}
					case 9:
					{
						if(!(s instanceof DocsCanvas))
						{
							VikaTouch.loading = true;
		
							if(VikaTouch.docsCanv == null)
								VikaTouch.docsCanv = new DocsCanvas();
							VikaTouch.docsCanv.LoadDocs(0,DocsCanvas.loadDocsCount);
							VikaTouch.setDisplay(VikaTouch.docsCanv);
						}
					}
					case 10:
					{
						/*
						//свайп влево
						if(d instanceof MenuCanvas)
						{
							//final Alert alert = new Alert("свайпнуто","влево, диалоги", null, AlertType.INFO);
							//VikaTouch.setDisplay(alert);
		
							if(VikaTouch.dialogs == null)
								VikaTouch.dialogs = new DialogsCanvas();
							VikaTouch.setDisplay(VikaTouch.dialogs);
						}
						if(d instanceof DialogsCanvas)
						{
							//final Alert alert = new Alert("свайпнуто","влево, лента", null, AlertType.INFO);
							//VikaTouch.setDisplay(alert);
		
							if(VikaTouch.news == null)
								VikaTouch.news = new NewsCanvas();
							VikaTouch.setDisplay(VikaTouch.news);
						}*/
						break;
					}
					case 11:
					{
						/*
						//свайп вправо
						if(d instanceof DialogsCanvas)
						{
							//final Alert alert = new Alert("свайпнуто","вправо, главная", null, AlertType.INFO);
							//VikaTouch.setDisplay(alert);if(VikaTouch.menu == null)
							if(VikaTouch.menu == null)
								VikaTouch.menu = new MenuCanvas();
							VikaTouch.setDisplay(VikaTouch.menu);
						}
		
						if(d instanceof NewsCanvas)
						{
							//final Alert alert = new Alert("свайпнуто","влево, диалоги", null, AlertType.INFO);
							//VikaTouch.setDisplay(alert);
		
							if(VikaTouch.dialogs == null)
								VikaTouch.dialogs = new DialogsCanvas();
							VikaTouch.setDisplay(VikaTouch.dialogs);
						}*/
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

}
