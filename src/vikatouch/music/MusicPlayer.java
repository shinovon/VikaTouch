package vikatouch.music;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.menu.IMenu;
import ru.nnproject.vikaui.popup.ContextMenu;
import ru.nnproject.vikaui.utils.ColorUtils;
import ru.nnproject.vikaui.utils.DisplayUtils;
import ru.nnproject.vikaui.utils.images.IconsManager;
import vikatouch.VikaTouch;
import vikatouch.items.menu.OptionItem;
import vikatouch.items.music.AudioTrackItem;
import vikatouch.screens.MainScreen;
import vikatouch.screens.music.MusicScreen;
import vikatouch.settings.Settings;

// экранчик с названием песни, перемоткой ии... Всё.
public class MusicPlayer extends MainScreen
	implements IMenu
{
	
	public MusicScreen playlist;
	public int current;
	public boolean isPlaying = false;
	
	// кэш для рисования
	private String title = "Track name";
	private String artist = "track artist";
	private String totalNumber;
	private String time = "00:00";
	private String totalTime = "99:59";
	private int x1 = 30, x2 = 330, currX = 100;
	public Image[] buttons;
	private Image coverOrig;
	private Image resizedCover;
	private int lastW;
	
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
	
	public void onRotate()
	{
		
	}
	
	public void options()
	{
		OptionItem[] opts = new OptionItem[]
		{
			new OptionItem(this,"Повторять",IconsManager.REFRESH,0,50),
			new OptionItem(this,"Случайно",IconsManager.PLAY,1,50),
			new OptionItem(this,"Скачать",IconsManager.DOWNLOAD,2,50),
			new OptionItem(this,"Проблемы с воспроизведением?",IconsManager.INFO,3,50),
		};
		VikaTouch.popup(new ContextMenu(opts));
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
				String cmd = "vlc.exe "+mrl;
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
		int dw = DisplayUtils.width;
		int dh = DisplayUtils.height;
		int hdw = dw/2;
		int textAnchor;
		int timeY;
		if(dw!=lastW)
		{
			onRotate();
			lastW = dw;
		}
		Font f = Font.getFont(0, 0, Font.SIZE_MEDIUM);
		g.setFont(f);
		// cover
		if(resizedCover!=null) g.drawImage(resizedCover, 0, 0, 0);
		g.setGrayScale(0);
		if(dw>dh)
		{
			// альбом
			textAnchor = (dw-50) * 3 / 4;
			timeY = dh-20;
			g.drawImage(buttons[5], textAnchor-125, dh-50, 0);
			g.drawImage(buttons[0], textAnchor-75, dh-50, 0);
			g.drawImage(buttons[isPlaying?1:3], textAnchor-25, dh-50, 0);
			g.drawImage(buttons[2], textAnchor+25, dh-50, 0);
			g.drawImage(buttons[4], textAnchor+75, dh-50, 0);
			
			g.drawString(artist, textAnchor, dh/2-f.getHeight(), Graphics.HCENTER | Graphics.TOP);
			g.drawString(title, textAnchor, dh/2, Graphics.HCENTER | Graphics.TOP);
		}
		else
		{
			// портрет, квадрат
			textAnchor = hdw;
			timeY = dh-70;
			g.drawImage(buttons[5], hdw-125, dh-50, 0);
			g.drawImage(buttons[0], hdw-75, dh-50, 0);
			g.drawImage(buttons[isPlaying?1:3], hdw-25, dh-50, 0);
			g.drawImage(buttons[2], hdw+25, dh-50, 0);
			g.drawImage(buttons[4], hdw+75, dh-50, 0);

			g.drawString(artist, textAnchor, timeY-f.getHeight()*3/2, Graphics.HCENTER | Graphics.TOP);
			g.drawString(title, textAnchor, timeY-f.getHeight()*5/2, Graphics.HCENTER | Graphics.TOP);
		}
		ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
		g.drawRect(x1, timeY, x2-x1, 10);
		g.fillRect(x1+2, timeY+2, currX-x1-4, 6);
	}
	
	public void drawHUD(Graphics g) 
	{
		
	}

	public void onMenuItemPress(int i) {
		
	}


	public void onMenuItemOption(int i) {
	}
}
