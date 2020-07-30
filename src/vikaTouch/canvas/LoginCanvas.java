package vikaTouch.canvas;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.game.GameCanvas;

import vikaTouch.VikaTouch;
import vikaTouch.canvas.menu.MenuCanvas;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.TextEditor;
import vikaTouch.newbase.VikaScreen;

public class LoginCanvas
	extends VikaScreen
{
	
	private static Image diary;
	private static Image loginpressed;
	private static Image login;
	private static boolean pressed;
	public static boolean vse;
	public static String user = "";
	public static String pass;
	public static Thread thread;

	public LoginCanvas()
	{
		try
		{
			diary = Image.createImage("/vikab48.jpg");
		}
		catch (Exception e)
		{
			
		}
		try
		{
			login = Image.createImage("/login.png");
		}
		catch (Exception e)
		{
			
		}
		try
		{
			loginpressed = Image.createImage("/loginpressed.png");
		}
		catch (Exception e)
		{
			
		}
		pressed = false;
	}

	public void paint(Graphics g)
	{
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_PORTRAIT:
			{
				ColorUtils.setcolor(g, ColorUtils.COLOR1);
				g.fillRect(0, 0, 360, 50);
				if(diary != null)
				{
					g.drawImage(diary, 156, 2, 0);
				}
				if(loginpressed != null && pressed)
				{
					g.drawImage(loginpressed, 116, 300, 0);
				}
				else if(login != null)
				{
					g.drawImage(login, 116, 300, 0);
				}
				ColorUtils.setcolor(g, ColorUtils.COLOR1);
				g.fillRect(0, 590, 360, 50);
				
				ColorUtils.setcolor(g, ColorUtils.TEXTBOX_OUTLINE);
				g.drawRect(60, 200, 240, 40);
				g.drawRect(60, 248, 240, 40);
				
				ColorUtils.setcolor(g, ColorUtils.TEXTCOLOR1);
				if(user != null)
					g.drawString(user, 70, 210, 0);
				if(pass != null)
				{
					String strpass = "";
					for(int i = 0; i < pass.length(); i++)
						strpass += "*";
					g.drawString(strpass,70,258,0);
				}
				break;
			}

			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			case DisplayUtils.DISPLAY_EQWERTY:
			case DisplayUtils.DISPLAY_ALBUM:
			{

				ColorUtils.setcolor(g, ColorUtils.COLOR1);
				g.fillRect(0, 0, 640, 25);
				if(diary != null)
				{
					g.drawImage(diary, 156, 2, 0);
				}
				if(loginpressed != null && pressed)
				{
					g.drawImage(loginpressed, 0, 220, 0);
				}
				else if(login != null)
				{
					g.drawImage(login, 0, 220, 0);
				}
				
				ColorUtils.setcolor(g, ColorUtils.TEXTBOX_OUTLINE);
				g.drawRect(0, 100, 240, 40);
				g.drawRect(0, 148, 240, 40);
				
				ColorUtils.setcolor(g, ColorUtils.TEXTCOLOR1);
				if(user != null)
					g.drawString(user, 10, 110, 0);
				if(pass != null)
				{
					String strpass = "";
					for(int i = 0; i < pass.length(); i++)
						strpass += "*";
					g.drawString(strpass,10,158,0);
				}
				break;
			}
			default:
			{
				ColorUtils.setcolor(g, ColorUtils.TEXTCOLOR1);
				g.drawString("Ваше", 2, 0, 0);
				g.drawString("разрешение", 2, 15, 0);
				g.drawString("экрана", 2, 30, 0);
				g.drawString("не", 2, 45, 0);
				g.drawString("поддерживается", 2, 60, 0);
				break;
			}
		}
	}
	
	public final void pointerPressed(int x, int y) {
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_PORTRAIT:
			{
				if(x > 116 && y > 300 && x < 112+128 && y < 300+36)
				{
					pressed = true;
					if(!vse)
						repaint();
				}
				if(x > 60 && y > 200 && x < 260 && y < 200+40)
				{
					if(thread != null)
						thread.interrupt();
					thread = new Thread()
					{
						public void run()
						{
							user = TextEditor.inputString("Логин", "", 28, false);
							repaint();
							interrupt();
						}
					};
					thread.start();
				}
				if(x > 60 && y > 248 && x < 260 && y < 288)
				{
					if(thread != null)
						thread.interrupt();
					thread = new Thread()
					{
						public void run()
						{
							pass = TextEditor.inputString("Пароль", "", 32, true);
							repaint();
							interrupt();
						}
					};
					thread.start();
				}
				break;
			}
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			case DisplayUtils.DISPLAY_EQWERTY:
			case DisplayUtils.DISPLAY_ALBUM:
			{
				if(y > 220 && y < 256 && x < 128)
				{
					pressed = true;
					if(!vse)
						repaint();
				}
				if( y > 100 && y < 140)
				{
					if(thread != null)
						thread.interrupt();
					thread = new Thread()
					{
						public void run()
						{
							user = TextEditor.inputString("Логин", "", 28, false);
							repaint();
							interrupt();
						}
					};
					thread.start();
				}
				if(y > 148 && y < 188)
				{
					if(thread != null)
						thread.interrupt();
					thread = new Thread()
					{
						public void run()
						{
							pass = TextEditor.inputString("Пароль", "", 32, true);
							repaint();
							interrupt();
						}
					};
					thread.start();
				}
				break;
			}
		}
	}

	public final void pointerReleased(int x, int y) {
		if(pressed)
		{
			pressed = false;
			switch(DisplayUtils.idispi)
			{
				case DisplayUtils.DISPLAY_PORTRAIT:
				{
					if(x > 116 && y > 300 && x < 112+128 && y < 300+36)
					{
						if(user != null && user.length() >= 5 && pass != null && pass.length() >= 6)
						{
							if(!vse)
							{
								VikaTouch.loading = true;
								
								//логин
								if(VikaTouch.DEMO_MODE)
								{
									vse = true;
									VikaScreen canvas = new MenuCanvas();
									VikaTouch.setDisplay(canvas);
								}
								else
								{
									vse = VikaTouch.inst.login(user, pass);
								}
								String reason;
								if(!vse && (reason = VikaTouch.getReason()) != null)
								{
									VikaTouch.setDisplay(new Alert("Не удалось ввойти", reason, null, AlertType.ERROR));
								}
							}
						}
						else
						{
							VikaTouch.setDisplay(new Alert("","Не введен логин или пароль!", null, AlertType.INFO));
						}
						
					}
					else
					{
						if(!vse)
							repaint();
					}
					
					break;
				}

				case DisplayUtils.DISPLAY_S40:
				case DisplayUtils.DISPLAY_ASHA311:
				case DisplayUtils.DISPLAY_EQWERTY:
				case DisplayUtils.DISPLAY_ALBUM:
				{
					if(y > 220 && y < 256 && x < 128)
					{
						if(user != null && user.length() >= 5 && pass != null && pass.length() >= 6)
						{
							if(!vse)
							{
								//логин
								if(VikaTouch.DEMO_MODE)
								{
									vse = true;
									VikaScreen canvas = new MenuCanvas();
									VikaTouch.setDisplay(canvas);
								}
								else
								{
									vse = VikaTouch.inst.login(user, pass);
								}
								String reason;
								if(!vse && (reason = VikaTouch.getReason()) != null)
								{
									VikaTouch.setDisplay(new Alert("Не удалось ввойти", reason, null, AlertType.ERROR));
								}
							}
						}
						else
						{
							VikaTouch.setDisplay(new Alert("","Не введен логин или пароль!", null, AlertType.INFO));
						}
						
					}
					else
					{
						if(!vse)
							repaint();
					}
					break;
				}
			}
		}
	}

}
