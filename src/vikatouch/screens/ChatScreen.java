package vikatouch.screens;

import javax.microedition.lcdui.Graphics;

import ru.nnproject.vikaui.ColorUtils;
import vikatouch.base.VikaTouch;

public class ChatScreen
	extends ReturnableListScreen
{
	private static final int TYPE_USER = 1;
	private static final int TYPE_CHAT = 2;
	private static final int TYPE_GROUP = 3;
	public int peerId;
	public int localId;
	public int type;
	public static final int OFFSET_INT = 2000000000;
	public String title = "dialog";
	public String title2 = "оффлайн";
	
	public ChatScreen(int peerId)
	{
		title2 = "Загрузка...";
		this.peerId = peerId;
		if(peerId < 0)
		{
			this.localId = -peerId;
			type = TYPE_GROUP;
			title2 = "group" + this.localId;
		}
		else if(peerId > 0)
		{
			if(peerId > OFFSET_INT)
			{
				this.localId = peerId - OFFSET_INT;
				title2 = "chat" + this.localId;
				this.type = TYPE_CHAT;
			}
			else
			{
				this.localId = peerId;
				this.type = TYPE_USER;
				title2 = "dm" + this.localId;
			}
		}
	}

	public void draw(Graphics g)
	{
		update(g);
		
		drawDialog(g);
		
		g.translate(0, -g.getTranslateY());
		
		drawHeader(g);
		
		drawTextbox(g);
	}

	protected void scrollHorizontally(int deltaX)
	{
		
	}
	
	public final void release(int x, int y)
	{
		if(!dragging)
		{
			if(y > 590)
			{
				//нижняя панель
			}
			else if(y < 50)
			{
				//верхняя панель
				if(x < 50)
				{
					VikaTouch.inst.cmdsInst.command(13, this);
				}
			}
		}
		super.release(x, y);
	}

	private void drawDialog(Graphics g)
	{
		
	}

	private void drawTextbox(Graphics g)
	{
		ColorUtils.setcolor(g, ColorUtils.OUTLINE);
		g.fillRect(0, 591, 640, 1);
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(0, 592, 640, 50);
	}

	private void drawHeader(Graphics g)
	{
		ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
		g.fillRect(0, 0, 640, 50);
		
	}

}
