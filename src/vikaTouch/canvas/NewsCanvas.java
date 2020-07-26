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
	private static Image menuImg;
	private static Image lentaImg;
	
	public NewsCanvas()
	{
		super();
		DisplayUtils.checkdisplay(this);
		
		if(VikaTouch.menu == null)
			VikaTouch.menu = new MenuCanvas();
		
		loadPosts();
		
		try
		{
			menuImg = Image.createImage("/menu.png");
			lentaImg = Image.createImage("/lentao.png");
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
					.addField("count", "" + requestcount)
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
				final JSONObject ob = items.getJSONObject(i2);
				JSONObject ob2;
				try
				{
					ob2 = ob.getJSONArray("copy_history").getJSONObject(0);
				}
				catch(Exception e)
				{
					ob2 = ob;
				}
				postitems[i] = new PostItem(ob2, ob);
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
			VikaTouch.error(e, "Обработка объектов: Посты (Parse)");
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
			g.translate(0,-g.getTranslateY());
			
			switch(DisplayUtils.idispi)
			{
				case 1:
				{

					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, 360, 58);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, 590, 360, 50);

					if(menuImg != null)
					{
						g.drawImage(menuImg, 304, 606, 0);
					}
					if(MenuCanvas.diary != null)
					{
						g.drawImage(MenuCanvas.diary, 2, 2, 0);
					}
					if(lentaImg != null)
					{
						g.drawImage(lentaImg, 37, 604, 0);
					}
					if(VikaTouch.has > 0)
					{

						if(MenuCanvas.dialhas != null)
						{
							g.drawImage(MenuCanvas.dialhas, 168, 599, 0);
							g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
							g.drawString(""+VikaTouch.has, 191, 598, 0);
						}
						else if(MenuCanvas.dial != null)
						{
							g.drawImage(MenuCanvas.dial, 168, 604, 0);
						}

					}
					else
					{
						if(MenuCanvas.dial != null)
						{
							g.drawImage(MenuCanvas.dial, 168, 604, 0);
						}
					}
					break;
				}
				case 2:
				{
					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, 240, 30);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, 295, 240, 25);
					
					if(MenuCanvas.diary != null)
					{
						g.drawImage(MenuCanvas.diary, 2, 2, 0);
					}
					break;
				}

				case 5:
				{
					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, 640, 58);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, 310, 640, 50);

					if(MenuCanvas.diary != null)
					{
						g.drawImage(MenuCanvas.diary, 2, 2, 0);
					}
					if(lentaImg != null)
					{
						g.drawImage(lentaImg, 36, 324, 0);
					}
					if(VikaTouch.has > 0)
					{

						if(MenuCanvas.dialhas != null)
						{
							g.drawImage(MenuCanvas.dialhas, 308, 319, 0);
							g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
							g.drawString(""+VikaTouch.has, 330, 318, 0);
						}
						else if(MenuCanvas.dial != null)
						{
							g.drawImage(MenuCanvas.dial, 308, 324, 0);
						}

					}
					else
					{
						if(MenuCanvas.dial != null)
						{
							g.drawImage(MenuCanvas.dial, 308, 324, 0);
						}
					}
					if(menuImg != null)
					{
						g.drawImage(menuImg, 584, 326, 0);
					}
					break;
				}
				default:
				{
					ColorUtils.setcolor(g, 3);
					g.fillRect(0, 0, DisplayUtils.width, oneitemheight+w);
					ColorUtils.setcolor(g, -3);
					g.fillRect(0, DisplayUtils.height - oneitemheight, DisplayUtils.width, oneitemheight);
					
					if(MenuCanvas.diary != null)
					{
						g.drawImage(MenuCanvas.diary, 2, 2, 0);
					}
				}
			}
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
				case 5:
				case 1:
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
