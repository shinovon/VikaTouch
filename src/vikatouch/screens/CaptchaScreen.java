package vikatouch.screens;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.VikaScreen;
import vikatouch.base.CaptchaObject;
import vikatouch.base.TextEditor;
import vikatouch.base.VikaTouch;
import vikatouch.base.local.TextLocal;

public class CaptchaScreen
	extends VikaScreen
{
	public Image image;
	private Thread thread;
	public static String input;
	public static boolean finished;
	public CaptchaObject obj;
	private int x;
	private int w;
	private String captchaRequiredStr;
	private String captchaStr;
	
	public CaptchaScreen()
	{
		super();
		captchaRequiredStr = TextLocal.inst.get("login.captcharequired");
		captchaStr = TextLocal.inst.get("login.captcha");
	}

	public void draw(Graphics g)
	{
		if(obj != null && image == null)
		{
			image = obj.getImage();
		}
		w = image.getWidth();
		ColorUtils.setcolor(g, -2);
		g.drawRect(0, 100, 240, 40);
		ColorUtils.setcolor(g, 2);
		if(input != null)
			g.drawString(input, 10, 110, 0);
		x = (DisplayUtils.width - w) / 2;
		ColorUtils.setcolor(g, -5);
		g.drawString(captchaRequiredStr/*"Требуется ввод капчи!"*/, DisplayUtils.width / 2, 0, Graphics.HCENTER);
		g.drawImage(image, x, 24, 0);
		ColorUtils.setcolor(g, 3);
		g.fillRect(x, 150, w, 36);
	}
	
	public final void press(short x, short y)
	{
		if(y > 100 && y < 140 && x < 240)
		{
			if(thread != null)
				thread.interrupt();
			thread = new Thread()
			{
				public void run()
				{
					input = TextEditor.inputString(captchaStr, "", 32, true);
					interrupt();
				}
			};
			thread.start();
		}
	}
	
	public final void release(short x, short y)
	{
		if(x > this.x && y > 150 && y < 186 && x < this.x + this.w)
		{
			finished = true;
			VikaTouch.canvas.showCaptcha = false;
		}
	}

}
