package vikatouch.screens;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.game.GameCanvas;

import ru.nnproject.vikaui.popup.InfoPopup;
import ru.nnproject.vikaui.screen.VikaScreen;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikatouch.VikaTouch;
import vikatouch.local.TextLocal;
import vikatouch.screens.menu.MenuScreen;
import vikatouch.utils.TextEditor;

public class LoginScreen
	extends VikaScreen
{
	
	//private static Image vikaLogo;
	private static Image loginpressed;
	private static Image login;
	private static Image settingsImg;
	private static boolean pressed;
	public static boolean vse;
	public static String user = "";
	public static String pass = "";
	public static Thread thread;
	private int selectedBtn;
	private boolean keysMode;
	
	// tapping cache
	private int[] tapCoords;
	private String titleLoginStr;
	protected String passwordStr;
	protected String loginStr;
	private String warnStr;
	private String failedStr;

	public LoginScreen()
	{
		/*try
		{
			vikaLogo = Image.createImage("/vikab48.jpg");
		}
		catch (IOException e)
		{ }*/
		try
		{
			login = Image.createImage("/login.png");
			loginpressed = Image.createImage("/loginpressed.png");
		}
		catch (IOException e)
		{ }
		try
		{
			settingsImg = Image.createImage("/settings.png");
		}
		catch (IOException e)
		{ }
		pressed = false;
		titleLoginStr = TextLocal.inst.get("title.login");
		loginStr = TextLocal.inst.get("login.login");
		passwordStr = TextLocal.inst.get("login.password");
		warnStr = TextLocal.inst.get("login.warn");
		failedStr = TextLocal.inst.get("login.failed");
	}
	
	public final void press(int key)
	{
		keysMode = true;
		if(key == -5)
		{
			if(selectedBtn == 0)
			{
				if(thread != null)
					thread.interrupt();
				thread = new Thread()
				{

					public void run()
					{
						user = TextEditor.inputString(loginStr, user, 28, false);
						repaint();
						interrupt();
					}
				};
				thread.start();
			}
			if(selectedBtn == 1)
			{
				if(thread != null)
					thread.interrupt();
				thread = new Thread()
				{
					public void run()
					{
						pass = TextEditor.inputString(passwordStr, pass, 32, false);
						repaint();
						interrupt();
					}
				};
				thread.start();
			}
			if(selectedBtn == 2)
			{
				if(user != null && user.length() >= 5 && pass != null && pass.length() >= 6)
				{
					if(!vse)
					{
						new Thread(
								new Runnable()
								{
									public void run()
									{
										//логин
										if(VikaTouch.DEMO_MODE)
										{
											vse = true;
											VikaScreen canvas = new MenuScreen();
											VikaTouch.setDisplay(canvas, 1);
										}
										else
										{
											vse = VikaTouch.inst.login(user, pass);
										}
										String reason;
										if(!vse && (reason = VikaTouch.getReason()) != null)
										{
											//VikaTouch.popup(new InfoPopup(reason, null, failedStr, "OK"));
											VikaTouch.warn(reason, failedStr);
											VikaTouch.loading = false;
										}
									}
								}
							).start();
					}
				}
				else
				{
					VikaTouch.loading = false;
					VikaTouch.warn(warnStr);
				}
			}
		}
		else if(key == -2)
		{
			selectedBtn++;
			if(selectedBtn > 2)
			{
				selectedBtn = 2;
			}
			if(selectedBtn == 2)
			{
				pressed = true;
			}
		}
		else if(key == -1)
		{
			selectedBtn--;
			if(selectedBtn == 1)
			{
				pressed = false;
			}
			if(selectedBtn < 0)
				selectedBtn = 0;
		}
		else if(key == -7 || key == -8 || key == 8)
		{
			if(selectedBtn == 0)
			{
				user = user.substring(0, user.length() - 1);
			}
			else if(selectedBtn == 1)
			{
				pass = pass.substring(0, pass.length() - 1);
			}
		}
		else
		{
			String s = VikaTouch.canvas.getKeyName(key);
			if(s.length() == 1)
			{
				if(selectedBtn == 0)
				{
					if(s == "*")
						s = "+";
					user += s;
				}
				else if(selectedBtn == 1)
				{
					pass += s;
				}
			}
		}
	}

	public void draw(Graphics g)
	{
		short sh = DisplayUtils.height;
		short sw = DisplayUtils.width;
		short yCenter = (short) (sh/2);
		byte fH = 40; // field height
		boolean shortLayout = sh<250;
		byte fieldsMargin = (byte) (shortLayout?30:60);
		
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, 0, sw, sh);
		
		ColorUtils.setcolor(g, ColorUtils.COLOR1);
		g.fillRect(0, 0, DisplayUtils.width, shortLayout?24:48);
		if(!shortLayout) g.fillRect(0, DisplayUtils.height-50, DisplayUtils.width, 50);
		//if(vikaLogo != null && !shortLayout)
		//{
	//		g.drawImage(vikaLogo, 2, 0, 0);
		//}
		Font f;
		//Font f = Font.getFont(0, 0, Font.SIZE_LARGE);
		//g.setFont(f); 
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		//g.drawString("Vika Touch - "+titleLoginStr, 8, (shortLayout?12:24)-f.getHeight()/2, 0);
		
		tapCoords = new int[] { yCenter - fH/2 - 8 - fH, yCenter - fH/2 - 8, yCenter - fH/2, yCenter + fH/2, yCenter + fH/2 + 8, yCenter + fH/2 + 8 + 32 };
		
		ColorUtils.setcolor(g, ColorUtils.TEXTBOX_OUTLINE);
		if(selectedBtn == 0)
			ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawRect(fieldsMargin, tapCoords[0], sw-fieldsMargin*2, fH);
		ColorUtils.setcolor(g, ColorUtils.TEXTBOX_OUTLINE);
		if(selectedBtn == 1)
			ColorUtils.setcolor(g, ColorUtils.TEXT);
		g.drawRect(fieldsMargin, tapCoords[2], sw-fieldsMargin*2, fH);
		ColorUtils.setcolor(g, ColorUtils.TEXTBOX_OUTLINE);
		if(loginpressed != null && pressed) {
			g.drawImage(loginpressed, DisplayUtils.width/2-loginpressed.getWidth()/2, tapCoords[4], 0);
		} else if(login != null)
			g.drawImage(login, DisplayUtils.width/2-login.getWidth()/2, tapCoords[4], 0);
		
		f = Font.getFont(0, 0, Font.SIZE_SMALL);
		g.setFont(f);
		ColorUtils.setcolor(g, ColorUtils.TEXTCOLOR1);
		if(user != null)
			g.drawString(user, fieldsMargin+10, tapCoords[0]+fH/2-f.getHeight()/2, 0);
		if(pass != null)
		{
			String strpass = "";
			if(keysMode && selectedBtn == 1)
			{
				strpass = pass;
			}
			else
			{
				for(int i = 0; i < pass.length(); i++)
					strpass += "*";
			}
			g.drawString(strpass, fieldsMargin+10, tapCoords[2]+fH/2-f.getHeight()/2, 0);
		}
	}
	
	public final void press(int x, int y) {
		keysMode = false;
		if(y>tapCoords[4]&&y<tapCoords[5])
		{
			pressed = true;
			if(!vse)
				repaint();
		}
		else if(y>tapCoords[0]&&y<tapCoords[1])
		{
			if(thread != null)
				thread.interrupt();
			thread = new Thread()
			{
				public void run()
				{
					user = TextEditor.inputString(loginStr, user, 28, false);
					repaint();
					interrupt();
				}
			};
			thread.start();
		}
		else if(y>tapCoords[2]&&y<tapCoords[3])
		{
			if(thread != null)
				thread.interrupt();
			thread = new Thread()
			{
				public void run()
				{
					pass = TextEditor.inputString(passwordStr, pass, 32, true);
					repaint();
					interrupt();
				}
			};
			thread.start();
		}
	}

	public final void release(int x, int y) {
		if(pressed)
		{
			pressed = false;
			if(y>tapCoords[4]&&y<tapCoords[5])
			{
				if(user != null && user.length() >= 5 && pass != null && pass.length() >= 6)
				{
					if(!vse)
					{
						VikaTouch.loading = true;
						new Thread(
								new Runnable()
								{
									public void run()
									{
										//логин
										if(VikaTouch.DEMO_MODE)
										{
											vse = true;
											VikaScreen canvas = new MenuScreen();
											VikaTouch.setDisplay(canvas, 1);
										}
										else
										{
											vse = VikaTouch.inst.login(user, pass);
										}
										String reason;
										if(!vse && (reason = VikaTouch.getReason()) != null)
										{
											if(reason.indexOf("invalid_user") > 0)
												reason = warnStr;
											//VikaTouch.popup(new InfoPopup(reason, null, failedStr, "OK"));
											VikaTouch.warn(reason, failedStr);
											VikaTouch.loading = false;
										}
									}
								}
							).start();
					}
				}
				else
				{

					VikaTouch.loading = false;
					VikaTouch.warn(warnStr);
				}
			}
			else
			{
				if(!vse)
					repaint();
			}
		}
	}
}