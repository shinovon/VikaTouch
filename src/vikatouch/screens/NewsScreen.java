package vikatouch.screens;


import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import vikamobilebase.ErrorCodes;
import vikamobilebase.VikaUtils;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.items.PostItem;
import vikatouch.screens.menu.MenuScreen;

public class NewsScreen
	extends MainScreen
{
	
	public static JSONArray profiles;
	public static JSONArray groups;
	
	public NewsScreen()
	{
		super();
		
		VikaTouch.loading = true;
		
		if(VikaTouch.menuCanv == null)
			VikaTouch.menuCanv = new MenuScreen();
		
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
		VikaTouch.loading = true;
		try
		{
			short requestcount = 10;
			short startswith = 0;
			short postscount = 10;
			uiItems = new PostItem[postscount];
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
				uiItems[i] = new PostItem(itemCopy, item);
				((PostItem) uiItems[i]).parseJSON();
				if(((PostItem) uiItems[i]).text == "" && ((PostItem) uiItems[i]).prevImage == null)
				{
					uiItems[i] = null;
					i--;
				}
				else
					itemsh += uiItems[i].getDrawHeight() + 8;
				i2++;
			}
			this.itemsCount = postscount;
		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.NEWSPARSE);
			e.printStackTrace();
		}
		
		System.gc();
	}

	protected final void callRefresh()
	{
		loadPosts();
	}

	public void draw(Graphics g)
	{
		double multiplier = (double)DisplayUtils.height / 640.0;
		double ww = 10.0 * multiplier;
		int w = (int)ww;
		try
		{
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
					if(uiItems[i] != null)
					{
						uiItems[i].paint(g, y, scrolled);
						y += uiItems[i].getDrawHeight() + 8;
					}
				}
			}
			catch (Exception e)
			{
				VikaTouch.error(e, ErrorCodes.NEWSPOSTSDRAW);
			}
			
			g.translate(0, -g.getTranslateY());
			
			drawHeaders(g, "Новости");
		} catch (Exception e) {
			VikaTouch.error(e, ErrorCodes.NEWSDRAW);
			e.printStackTrace();
		}
	}
	
	protected final void up()
	{
		scroll += 25;
		repaint();
	}
	
	protected final void down()
	{
		scroll -= 25;
		repaint();
	}
	
	public final void release(int x, int y)
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
							int y2 = y1 + uiItems[i].getDrawHeight();
							yy += uiItems[i].getDrawHeight();
							if(y > y1 && y < y2)
							{
								uiItems[i].tap(x, y1 - y);
								itemsh = 0;
								for(int i2 = 0; i2 < itemsCount; i2++)
								{
									itemsh += uiItems[i2].getDrawHeight();
								}
								break;
							}
							
						}
					}
					break;
				}
				
			}
		}
		super.release(x, y);
	}

	protected void scrollHorizontally(int deltaX)
	{
		
	}

}