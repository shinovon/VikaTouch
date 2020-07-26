package vikaTouch.newbase;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.DialogsCanvas;
import vikaTouch.canvas.LoginCanvas;
import vikaTouch.canvas.MenuCanvas;
import vikaTouch.canvas.NewsCanvas;

public class Commands implements CommandListener {

    public static final Command close = new Command("Закрыть", 4, 0);

	public void commandAction(Command c, Displayable d)
	{
		if(c == close)
		{
			VikaTouch.inst.destroyApp(false);
		}
	}
	
	public void commandAction(int i, Displayable d) {
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
				
				VikaTouch.loadingAnimation = true;
				
				if(VikaTouch.news == null)
					VikaTouch.news = new NewsCanvas();
				VikaTouch.setDisplay(VikaTouch.news);
				
				break;
			}
			case 1:
			{
				//Сообщения
				
				VikaTouch.loadingAnimation = true;

				if(!(d instanceof DialogsCanvas))
				{
					if(VikaTouch.dialogs == null)
						VikaTouch.dialogs = new DialogsCanvas();
					VikaTouch.setDisplay(VikaTouch.dialogs);
				}
				break;
			}
			case 2:
			{
				//Меню
				
				if(!(d instanceof MenuCanvas))
				{
					if(VikaTouch.menu == null)
						VikaTouch.menu = new MenuCanvas();
					VikaTouch.setDisplay(VikaTouch.menu);
				}
				break;
			}
			case 3:
			{
				//Логин
				
				VikaTouch.loadingAnimation = true;
				
				if(d instanceof LoginCanvas)
				{
					VikaTouch.inst.login(LoginCanvas.user, LoginCanvas.pass);
				}
				break;
			}
			case 4:
			{
				//Друзья
				if(d instanceof MenuCanvas)
				{
					final Alert alert = new Alert("нажато","друзья", null, AlertType.INFO);
					VikaTouch.setDisplay(alert);
				}
				break;
			}
			case 5:
			{
				//Группы
				if(d instanceof MenuCanvas)
				{
					final Alert alert = new Alert("нажато","группы", null, AlertType.INFO);
					VikaTouch.setDisplay(alert);
				}
				break;
			}
			case 6:
			{
				//Музыка
				if(d instanceof MenuCanvas)
				{
					final Alert alert = new Alert("нажато","музыка", null, AlertType.INFO);
					VikaTouch.setDisplay(alert);
				}
				break;
			}
			case 7:
			{
				//Видео
				if(d instanceof MenuCanvas)
				{
					final Alert alert = new Alert("нажато","видео", null, AlertType.INFO);
					VikaTouch.setDisplay(alert);
				}
				break;
			}
			case 8:
			{
				//Фотки
				if(d instanceof MenuCanvas)
				{
					final Alert alert = new Alert("нажато","фотки", null, AlertType.INFO);
					VikaTouch.setDisplay(alert);
				}
				break;
			}
			case 9:
			{
				//Фотки
				if(d instanceof MenuCanvas)
				{
					final Alert alert = new Alert("нажато","доки", null, AlertType.INFO);
					VikaTouch.setDisplay(alert);
				}
				break;
			}
			case 10:
			{/*
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
			default:
			{
				break;
			}
		}
	}

}
