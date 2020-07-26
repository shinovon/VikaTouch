package vikaTouch.canvas;

import java.util.Random;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.items.PostItem;

public class NewsCanvas
	extends MainCanvas
{
	
	public static PostItem[] postitems = new PostItem[10];
	public static JSONArray profiles;
	public static JSONArray groups;
	
	public NewsCanvas()
	{
		super();
		DisplayUtils.checkdisplay(this);
		
		if(VikaTouch.menu == null)
			VikaTouch.menu = new MenuCanvas();
		
		loadPosts();
		
		try
		{
			switch(DisplayUtils.idispi)
			{
				case DisplayUtils.DISPLAY_PORTRAIT:
				case DisplayUtils.DISPLAY_ALBUM:
				case DisplayUtils.DISPLAY_E6:
				{
					menuImg = Image.createImage("/menu.png");
					newsImg = Image.createImage("/lentao.png");
					break;
				}
				case DisplayUtils.DISPLAY_S40:
				case DisplayUtils.DISPLAY_ASHA311:
				case DisplayUtils.DISPLAY_EQWERTY:
				{
					menuImg = VikaUtils.resize(Image.createImage("/menu.png"), 10, 9);
					newsImg = VikaUtils.resize(Image.createImage("/lentao.png"), 11, 11);
					break;
				}
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	private void loadPosts()
	{
		try
		{
			int requestcount = 20;
			int startswith = 0;
			int postscount = 10;
			int len2 = postscount;
			final String s = VikaUtils.download(
					new URLBuilder("newsfeed.get")
					.addField("filters", "post,photo,photo_tag,wall_photo")
					.addField("count", requestcount)
					.addField("fields", "groups,profiles,items")
					);
			final JSONObject response = new JSONObject(s).getJSONObject("response");
			final JSONArray items = response.getJSONArray("items");
			profiles = response.getJSONArray("profiles");
			groups = response.getJSONArray("groups");
			System.out.println(s);
			itemsh = 0;
			int i2 = startswith;
			for(int i = 0; i < len2; i++)
			{
				if(i2 >= requestcount)
				{
					break;
				}
				final JSONObject item = items.getJSONObject(i2);
				JSONObject itemCopy;
				try
				{
					itemCopy = item.getJSONArray("copy_history").getJSONObject(0);
				}
				catch(Exception e)
				{
					itemCopy = item;
				}
				postitems[i] = new PostItem(itemCopy, item);
				postitems[i].parseJSON();
				if(postitems[i].text == "" && postitems[i].prevImage == null)
				{
					postitems[i] = null;
					i--;
				}
				else
					itemsh += postitems[i].itemDrawHeight;
				i2++;
			}
			this.itemsCount = postscount;
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Обработка объектов: Посты");
			e.printStackTrace();
		}
		
		System.gc();
	}

	protected final void callRefresh()
	{
		loadPosts();
	}

	public void paint(Graphics g)
	{
		double multiplier = (double)DisplayUtils.height / 640.0;
		double ww = 10.0 * multiplier;
		int w = (int)ww;
		try
		{
			DisplayUtils.checkdisplay(this);
			
			ColorUtils.setcolor(g, -1);
			g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
			ColorUtils.setcolor(g, 0);
			g.setFont(Font.getFont(0, 0, 8));
			//g.drawString(string, 8, 8, 0);
			
			update(g);
			int y = oneitemheight + w;
			try
			{
				for(int i = 0; i < itemsCount; i++)
				{
					if(postitems[i] != null)
					{
						postitems[i].paint(g, y, scrolled);
						y += postitems[i].itemDrawHeight + 8;
					}
				}
			}
			catch (Exception e)
			{
				VikaTouch.error(e, "Прорисовка объектов: Посты");
			}
			
			g.translate(0, -g.getTranslateY());
			
			drawHeaders(g, "Новости");
		} catch (Exception e) {
			VikaTouch.error(e, "Прорисовка: Лента");
			e.printStackTrace();
		}
	}
	
	protected final void pointerReleased(int x, int y)
	{
		if(!dragging)
		{
			switch(DisplayUtils.idispi)
			{
				case DisplayUtils.DISPLAY_ALBUM:
				case DisplayUtils.DISPLAY_PORTRAIT:
				{
					if(y > 58 && y < DisplayUtils.height - oneitemheight)
					{
						int yy = 0;
						for(int i = 0; i < itemsCount; i++)
						{
							int y1 = scrolled + 50 + yy;
							int y2 = y1 + postitems[i].itemDrawHeight;
							yy += postitems[i].itemDrawHeight;
							if(y > y1 && y < y2)
							{
								postitems[i].tap(x, y1 - y);
								itemsh = 0;
								for(int i2 = 0; i2 < itemsCount; i2++)
								{
									itemsh += postitems[i2].itemDrawHeight;
								}
								break;
							}
							
						}
					}
					break;
				}
				
			}
		}
		super.pointerReleased(x, y);
	}

	protected void scrollHorizontally(int deltaX)
	{
		
	}

}
