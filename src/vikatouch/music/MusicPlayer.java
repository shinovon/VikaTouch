package vikatouch.music;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;

import vikatouch.VikaTouch;
import vikatouch.items.menu.AudioTrackItem;
import vikatouch.screens.MainScreen;
import vikatouch.screens.menu.MusicScreen;

// экранчик с названием песни, перемоткой ии... Всё.
public class MusicPlayer extends MainScreen
{
	
	public static void launch(MusicScreen list, int track)
	{
		try { // временно для теста. Потом тут будет свитч по той толпе вариантов из настроек.
			String mrl = ((AudioTrackItem) list.uiItems[track]).mp3;
			System.out.println(mrl);
			if(mrl.indexOf("xtrafrancyz")!=-1)
			{
				mrl = "https://"+mrl.substring("http://vk-api-proxy.xtrafrancyz.net/_/".length());
			}
			String cmd = "vlc "+mrl;
			System.out.println("RESULT="+VikaTouch.appInst.platformRequest(cmd));
			System.out.println(cmd);
		} catch (ConnectionNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
