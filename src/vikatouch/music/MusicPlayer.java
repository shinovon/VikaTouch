package vikatouch.music;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;

import vikatouch.VikaTouch;
import vikatouch.items.menu.AudioTrackItem;
import vikatouch.screens.MainScreen;
import vikatouch.screens.menu.MusicScreen;
import vikatouch.settings.Settings;

// экранчик с названием песни, перемоткой ии... Всё.
public class MusicPlayer extends MainScreen
{
	
	public MusicScreen playlist;
	public int current;
	
	public MusicPlayer()
	{
		// подгрузка ресов
	}
	
	
	public void firstLoad()
	{
		// созданиие потоков и прочего говна
		loadTrack();
	}
	
	public void loadTrack()
	{
		Pause();
		// загрузка следующего
	}
	
	public void Pause()
	{
		
	}
	
	public void Play()
	{
		
	}
	
	public void Next()
	{
		current++;
		if(current>=playlist.uiItems.length) current = 0;
		loadTrack();
	}
	
	public void Prev()
	{
		current--;
		if(current<0) current = playlist.uiItems.length - 1;
		loadTrack();
	}
	
	public static void launch(MusicScreen list, int track)
	{
		try 
		{
			switch(Settings.audioMode)
			{
			case Settings.AUDIO_VLC:
				String mrl = ((AudioTrackItem) list.uiItems[track]).mp3;
				System.out.println(mrl);
				if(mrl.indexOf("xtrafrancyz")!=-1)
				{
					mrl = "https://"+mrl.substring("http://vk-api-proxy.xtrafrancyz.net/_/".length());
				}
				String cmd = "vlc "+mrl;
				System.out.println("RESULT="+VikaTouch.appInst.platformRequest(cmd));
				System.out.println(cmd);
				break;
			case Settings.AUDIO_DOWNLOAD:
				VikaTouch.appInst.platformRequest(((AudioTrackItem) list.uiItems[track]).mp3);
				break;
			default:
				MusicPlayer mp = new MusicPlayer();
				mp.playlist = list;
				mp.current = track;
				mp.firstLoad();
				VikaTouch.setDisplay(mp, 1);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
