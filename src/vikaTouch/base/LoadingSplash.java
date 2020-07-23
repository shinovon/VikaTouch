package vikaTouch.base;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class LoadingSplash extends Canvas {
	public static boolean showloadingbar;
	private int width;
	private int height;
	private Image splashImage = null;
	private GifDecoder d;
	public Display display;
	boolean action;
	private Image gifImage;
	private Thread t;
	static final boolean nadpis = true;
	static final boolean debug = true;

	public LoadingSplash(String var1, String var2) {
		this.setFullScreenMode(true);
		this.width = this.getWidth();
		this.height = this.getHeight();

		if(var1 == null)
		{
			var1 = "/image.png";
		}
		try {
			{
				InputStream in = this.getClass().getResourceAsStream(var2);
				d = new GifDecoder();
				int err = d.read(in);
	            if (err == 0) {
	                gifImage = d.getImage();
	            }
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			
		}
		try {
			this.splashImage = Image.createImage(var1);
		} catch (Exception e) {
			Alert var3;
			(var3 = new Alert("������!", "Splash Screen ����������� �� �������!", (Image) null, AlertType.ERROR))
					.setTimeout(-2);
			display.setCurrent(var3);
			var1 = "/image.png";
			try {
				this.splashImage = Image.createImage(var1);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	protected final void paint(Graphics var1) {
		var1.setColor(0);
		final String var5 = System.getProperty("microedition.platform");
		//����� ��� ���������� ������ �� ���� ������� ����� ������ �� ��� ������
		if((var5.indexOf("Belle") > 0 || var5.indexOf("Anna") > 0) && (width == 640 || height == 640))
		{
			var1.fillRect(0, 0, 640, 640);
		}
		else
		{
			var1.fillRect(0, 0, this.width, this.height);
		}
		var1.drawImage(this.splashImage, this.width / 2 - 128, this.height / 2 - 128, 0);
		var1.setColor(0xffffff);
		if(gifImage !=null && showloadingbar)
		{
			if(var5.indexOf("S40") > 0)
				var1.drawImage(this.gifImage, this.width / 2 - 112, this.height / 2 + 80, 0);
			else
				var1.drawImage(this.gifImage, this.width / 2 - 112, this.height / 2 + 192, 0);
			if(nadpis)
				var1.drawString("загрузка", this.width / 2 - 48, this.height / 2 + 80, 0);
		}
		else
		{
			if(nadpis)
				var1.drawString("зогрузка", this.width / 2 - 48, this.height / 2 + 80, 0);
		}
	}
	public void startthread()
	{
		try {
			//if(width >= 240 && height >= 344)
			{
	            action = true;
	    		t = new Thread("l2") {
	    			public void run() {
	    				int t;
	    	            action = true;
	    		            while (action) { 
	    		                int n = d.getFrameCount();
	    		                for (int i = 0; i < n; i++) {
	    		                    gifImage = d.getFrame(i);
	    		                    t = d.getDelay(i);
	    		                    repaint();
	    		                    serviceRepaints();
	    		                    try {
	    		                    	Thread.sleep(t);
	    		                    } catch (Exception ex){
	    		                    	ex.printStackTrace();
	    		                    }
	    		                }
	    		            }
	    		        this.interrupt();
	    		    }
	    		};
	    		t.start();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			
		}
	}
}