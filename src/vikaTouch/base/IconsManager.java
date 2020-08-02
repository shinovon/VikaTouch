package vikaTouch.base;

import java.io.IOException;

import javax.microedition.lcdui.*;

public class IconsManager {
	
	public static Image[] ico;
	public static Image[] selIco;
	
	public static void Load() throws IOException {
		Image sheet = Image.createImage("/Icons.png");
		int c /*count*/ = sheet.getWidth() / 24;
		ico = new Image[c]; selIco = new Image[c];
		for(int i=0;i<c;i++) {
			ico[i] = Image.createImage(sheet, i*24, 0, 24, 24, 0);
			selIco[i] = Image.createImage(sheet, i*24, 24, 24, 24, 0);
		}
		
		ac = Image.createImage("/avaMask.png");
		acs = Image.createImage("/avaMaskSelected.png");
	}
	
	public static Image ac;
	public static Image acs;
	
	public static final int FRIENDS = 0;
	public static final int GROUPS = 1;
	public static final int PHOTOS = 2;
	public static final int VIDEOS = 3;
	public static final int MUSIC = 4;
	public static final int DOCS = 5;
	public static final int CLOSE = 6;
	public static final int NEWS = 7;
	public static final int MSGS = 8;
	public static final int MENU = 9;
	public static final int SETTINGS = 10;
	public static final int BACK = 11;
	public static final int SEARCH = 12;
	public static final int OPTIONS = 13;
	public static final int APPLY = 14;
	public static final int ADD = 15;
	public static final int VOICE = 16;
	public static final int ATTACHMENT = 17;
	public static final int SEND = 18;
	public static final int EDIT = 19;
	public static final int INFO = 20;
	public static final int STICKERS = 21;
	public static final int LIKE = 22;
	public static final int LIKE_F = 23;
	public static final int COMMENTS = 24;
	public static final int VIEWS = 25;
	public static final int REPOST = 26;
	public static final int PIN = 27;
	public static final int ANSWER = 28;
	public static final int FAV = 29;
	
}