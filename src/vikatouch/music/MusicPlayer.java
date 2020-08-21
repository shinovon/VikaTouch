package vikatouch.music;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.menu.IMenu;
import vikatouch.VikaTouch;
import vikatouch.items.menu.AudioTrackItem;
import vikatouch.items.menu.OptionItem;
import vikatouch.screens.MainScreen;
import vikatouch.screens.menu.MusicScreen;
import vikatouch.settings.Settings;

// экранчик с названием песни, перемоткой ии... Всё.
public class MusicPlayer extends MainScreen
	implements IMenu
{
	
	public MusicScreen playlist;
	public int current;
	
	// кэш для рисования
	private String title;
	private String artist;
	private String playlistN;
	private String number;
	private String time;
	private String totalTime;
	private int x1, x2, currX;
	public Image[] buttons;
	
	public MusicPlayer()
	{
		try
		{
			Image sheet = Image.createImage("/playerbtns.png");
			buttons = new Image[6];
			for(int i = 0; i < 6; i++)
			{
				buttons[i] = Image.createImage(sheet, i*50, 0, 50, 50, 0);
			}
		}
		catch (Exception e)
		{
			
		}
		// подгрузка ресов
	}
	
	
	public void firstLoad()
	{
		// созданиие потоков и прочего говна
		loadTrack();
	}
	
	public void loadTrack()
	{
		pause();
		// загрузка следующего
	}
	
	public void pause()
	{
		
	}
	
	public void play()
	{
		
	}
	
	public void next()
	{
		current++;
		if(current>=playlist.uiItems.length) current = 0;
		loadTrack();
	}
	
	public void prev()
	{
		current--;
		if(current<0) current = playlist.uiItems.length - 1;
		loadTrack();
	}
	
	public void updateDrawData()
	{
		
	}
	
	public void options()
	{
		OptionItem[] opts;
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


	public void onMenuItemPress(int i) {
		
	}


	public void onMenuItemOption(int i) {
	}
}
