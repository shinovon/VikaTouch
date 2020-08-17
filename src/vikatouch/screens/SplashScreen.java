package vikatouch.screens;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import vikatouch.VikaTouch;

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
		"Loading localization",
		"Preparing assets",
		"Autorization",
		"Loading profile data",
		"Loading dialogs",
		"Ready."
	};
	public String tipStr = "Tip";
	public String[] tip;

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
					g.drawString(tipStr, hdw-f.stringWidth(tipStr)/2, sy+260+f.getHeight()+30+20+f.getHeight(), 0);
					for(int i=0;i<tip.length;i++)
					{
						g.drawString(tip[i], hdw-f.stringWidth(tip[i])/2, sy+260+f.getHeight()+30+20+f.getHeight()+f.getHeight()+f.getHeight()*i, 0);
					}
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		
		
	}
	
	
	public void drawHUD(Graphics g)
	{
	}
	
	public final void release(int x, int y)
	{
		
	}

}
