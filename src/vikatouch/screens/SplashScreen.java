package vikatouch.screens;

import java.util.Random;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import ru.nnproject.vikaui.utils.TextBreaker;
import vikatouch.IconsManager;
import vikatouch.VikaTouch;
import vikatouch.local.TextLocal;

public class SplashScreen
	extends MainScreen 
{
	public Image logo;
	public String header;
	
	public static int currState = 0; // до 7
	
	public int[] statesProgress = new int[] { 
		0, 5, 10, 20, 40, 60, 80, 100
	};
	public String[] statesNames = new String[]
	{ 
		"Starting application",
		"Loading settings",
		"Loading text tables",
		"Preparing assets",
		"Autorization",
		"Loading profile data",
		"Pre-loading conversations",
		"Ready."
	};
	public String tipStr = "Tip";
	public String[] tip;
	public int tipL;

	public SplashScreen()
	{
		super();
		
		try {
			logo = Image.createImage("/vika256.png");
		}
		catch (Exception e)
		{
			logo = Image.createImage(1, 1);
		}
		
		header = "ViKa Touch "+VikaTouch.getRelease()+" v"+VikaTouch.getVersion();
	}
		
	public void draw(Graphics g)
	{
		int dw = DisplayUtils.width; int dh = DisplayUtils.height;
		int hdw = dw/2;
		Font f = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		g.setFont(f);
		int sy = (dh-(310+f.getHeight()*2))/2;
		if(dh>300)
		{
			try
			{
				g.drawImage(logo, hdw-128, sy, 0);
				g.setColor(0,0,0);
				g.drawString(header, hdw-f.stringWidth(header)/2, sy+260, 0);
				ColorUtils.setcolor(g, ColorUtils.COLOR1);
				g.fillRect(40, sy+260+f.getHeight()+30, dw-80, 16);
				g.setGrayScale(255);
				g.fillRect(42, sy+260+f.getHeight()+30+2, dw-84, 12);
				ColorUtils.setcolor(g, ColorUtils.COLOR1);
				g.fillRect(43, sy+260+f.getHeight()+30+3, (dw-86)*statesProgress[currState]/100, 10);
				g.setColor(0,0,0);
				g.drawString(statesNames[currState], hdw-f.stringWidth(statesNames[currState])/2, sy+260+f.getHeight()+30+20, 0);
				
				// Подсказка
				if(tip!=null)
				{
					g.drawString(tipStr, hdw-f.stringWidth(tipStr)/2, sy+260+f.getHeight()*3+50, 0);
					f = Font.getFont(0, 0, Font.SIZE_SMALL);
					g.setFont(f);
					for(int i=0;i<tipL;i++)
					{
						int sw = f.stringWidth(tip[i]); // иначе почему-то не работает.
						g.drawString(tip[i], hdw-sw/2, sy+260+50+f.getHeight()*(i+5), 0);
						// Я СКАЗАЛ, ОНО ПО ДРУГОМУ НЕ РАБОТАЕТ. Если пашет на н8 (или что там), не обязано на КЕме / других телах.
					}
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		else
		{
			g.drawImage(logo, hdw-128, dh/2-128, 0);
			ColorUtils.setcolor(g, ColorUtils.COLOR1);
			g.fillRect(40, dh-18, dw-80, 16);
			g.setGrayScale(255);
			g.fillRect(42, dh-16, dw-84, 12);
			ColorUtils.setcolor(g, ColorUtils.COLOR1);
			g.fillRect(43, dh-15, (dw-86)*statesProgress[currState]/100, 10);
		}
		/*
		g.setColor(0xffff00);
		g.fillRect(0,0,90,90);
		try
		{
			
			if(IconsManager.sheet != null)
				g.drawImage(IconsManager.sheet, 0, 0, 0);
		g.drawImage(IconsManager.ico[IconsManager.INFO],0,64,0);
		}
		catch(Exception e)
		{
			
		}
		*/
		ColorUtils.setcolor(g, 0);
		
		g.drawString("DO NOT DISTRIBUTE",dw/2,0,Graphics.TOP|Graphics.HCENTER);
		g.drawString("НЕ РАСПРОСТРАНЯТЬ",dw/2,f.getHeight()+2,Graphics.TOP|Graphics.HCENTER);
	}
	
	public void setText()
	{
		for(int i=3;i<=7;i++)
		{
			statesNames[i] = TextLocal.inst.get("splash.title."+i);
		}
		tipStr = TextLocal.inst.get("splash.tip");
		int tipsC = 16;
		Random r = new Random();
		int i = r.nextInt(tipsC);
		tip = TextBreaker.breakText(TextLocal.inst.get("splash.tip."+i), false, null, true, DisplayUtils.width-40);
		for(tipL = 0; (tipL<tip.length && tip[tipL]!=null); tipL++) { }
	}
	
	
	public void drawHUD(Graphics g)
	{
	}
	
	public final void release(int x, int y)
	{
		
	}

}
