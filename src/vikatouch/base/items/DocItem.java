package vikatouch.base.items;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.ScrollableCanvas;
import vikamobilebase.VikaUtils;
import vikatouch.base.ErrorCodes;
import vikatouch.base.JSONBase;
import vikatouch.base.ResizeUtils;
import vikatouch.base.VikaTouch;
import vikatouch.base.attachments.PhotoSize;
import vikatouch.screens.menu.DocsScreen;

public class DocItem
	extends JSONUIItem
{
	private String name;
	private String url;
	private String iconUrl;
	private String prevImgUrl;
	private int size;
	private PhotoSize[] prevSizes;
	private Image iconImg;
	private int documentType;
	private String ext;
	private int type;
	private String time;
	
	private static Image downloadBI = null;

	public static final int BORDER = 1;

	//типы вложения
	private static final int TYPE_TEXT = 1;
	private static final int TYPE_ARCHIVE = 2;
	private static final int TYPE_GIF = 3;
	private static final int TYPE_PHOTO = 4;
	private static final int TYPE_AUDIO = 5;
	private static final int TYPE_VIDEO = 6;
	private static final int TYPE_EBOOK = 7;
	private static final int TYPE_UNKNOWN = 8;
	private static final int TYPE_UNDEFINED = 0;

	public DocItem(JSONObject json)
	{
		super(json);
		itemDrawHeight = 50;
	}

	public void parseJSON()
	{
		//System.out.println(json.toString());

		try
		{
			date = json.optInt("date");
			name = json.optString("title");
			url = fixJSONString(json.optString("url"));
			size = json.optInt("size");
			ext = json.optString("ext");
			type = json.optInt("type");

			if(!json.isNull("preview"))
			{
				prevSizes = PhotoSize.parseSizes(json.getJSONObject("preview").getJSONObject("photo").getJSONArray("sizes"));

				PhotoSize iconPs = null;
				PhotoSize prevPs = null;

				try
				{
					iconPs = PhotoSize.getSize(prevSizes, "s");
					if(iconPs==null) throw new Exception();
				}
				catch (Exception e)
				{
					try
					{
						iconPs = PhotoSize.getSize(prevSizes, "d");
					}
					catch (Exception e3)
					{
						e3.printStackTrace();
					}
				}
				try
				{
					prevPs = PhotoSize.getSize(prevSizes, "x");
					if(prevPs==null) throw new Exception();
				}
				catch (Exception e1)
				{
					try
					{
						prevPs = PhotoSize.getSize(prevSizes, "o");
					}
					catch (Exception e2)
					{
						//не достучались до превьюхи..
					}
				}

				if(iconPs != null)
				{
					iconUrl = fixJSONString(iconPs.url);
				}

				if(prevPs != null)
				{
					prevImgUrl = fixJSONString(prevPs.url);
				}
			}
		}
		catch (JSONException e)
		{
			//e.printStackTrace();
			//Предпросмотр не завезли - видимо не картинка. Ну и ладно.
		}
		catch (Exception e)
		{
			e.printStackTrace();
			VikaTouch.error(e, ErrorCodes.DOCPARSE);
		}

		setDrawHeight();

		System.gc();
	}

	private void setDrawHeight()
	{
		itemDrawHeight = 48 + (BORDER * 2);
	}

	public void paint(Graphics g, int y, int scrolled)
	{
		if(ScrollableCanvas.keysMode && selected)
		{
			ColorUtils.setcolor(g, ColorUtils.BUTTONCOLOR);
			g.fillRect(0, y, DisplayUtils.width, itemDrawHeight);
		}
		
		if(iconImg == null)
			iconImg = getPreviewImage();

		if(time == null)
			time = getTime();
		ColorUtils.setcolor(g, 0);
		if(name != null)
			g.drawString(name, 73, y, 0);
		ColorUtils.setcolor(g, ColorUtils.OUTLINE);
		g.drawString(size / 1000 + "кб, " + time, 73, y + 24, 0);
		if(iconImg != null)
		{
			g.drawImage(iconImg, 14, y + BORDER, 0);
		}
		if(!ScrollableCanvas.keysMode)
		{
			try
			{
				if(downloadBI == null)
				{
					downloadBI = Image.createImage("/downloadBtn.png");
				}
				g.drawImage(downloadBI, DisplayUtils.width-downloadBI.getWidth(), y, 0);
			} 
			catch (Exception e)
			{
				
			}
		}
	}

	private Image getPreviewImage()
	{
		Image img = null;
		try
		{
			//System.out.println(iconUrl);
			img = ResizeUtils.resizeItemPreview(VikaUtils.downloadImage(iconUrl));
		}
		catch (Exception e)
		{
			try
			{
				switch(type)
				{
					case TYPE_PHOTO:
					case TYPE_GIF:
						return ResizeUtils.resizeItemPreview(VikaTouch.cameraImg);
					case TYPE_AUDIO:
						return ResizeUtils.resizeItemPreview(Image.createImage("/docmus.png"));
					case TYPE_VIDEO:
						return ResizeUtils.resizeItemPreview(Image.createImage("/docvid.png"));
					case TYPE_ARCHIVE:
						if(ext.toLowerCase().indexOf("sis") != VikaTouch.INDEX_FALSE)
						{
							return ResizeUtils.resizeItemPreview(Image.createImage("/docsis.png"));
						}
						else
							return ResizeUtils.resizeItemPreview(Image.createImage("/doczip.png"));
					case TYPE_TEXT:
					case TYPE_EBOOK:
						return ResizeUtils.resizeItemPreview(Image.createImage("/doctxt.png"));
					case TYPE_UNKNOWN:
					case TYPE_UNDEFINED:
					default:
						if(ext.toLowerCase().indexOf("jar") != VikaTouch.INDEX_FALSE || ext.toLowerCase().indexOf("jad") != VikaTouch.INDEX_FALSE)
						{
							return ResizeUtils.resizeItemPreview(Image.createImage("/docjar.png"));
						}
						else if(ext.toLowerCase().indexOf("sis") != VikaTouch.INDEX_FALSE)
						{
							return ResizeUtils.resizeItemPreview(Image.createImage("/docsis.png"));
						}
						else if(ext.toLowerCase().indexOf("rar") != VikaTouch.INDEX_FALSE || ext.toLowerCase().indexOf("zip") != VikaTouch.INDEX_FALSE || ext.toLowerCase().indexOf("tar") != VikaTouch.INDEX_FALSE || ext.toLowerCase().indexOf("7z") != VikaTouch.INDEX_FALSE)
						{
							return ResizeUtils.resizeItemPreview(Image.createImage("/doczip.png"));
						}
						/*else if(ext.toLowerCase().indexOf("torrent") != VikaTouch.INDEX_FALSE)
						{
							return ResizeUtils.resizeItemPreview(Image.createImage("/doctorr.png"));
						}*/
						else
						{
							return ResizeUtils.resizeItemPreview(Image.createImage("/docfile.png"));
						}
				}
			}
			catch (Exception e2)
			{

			}
		}
		return img;
	}
	
	public void StartPreview() {
		if(type == TYPE_PHOTO)
		{
			
			if(prevImgUrl==null) { return; }
			VikaTouch.docsScr.isPreviewShown = true;
			(new Thread()
			{
				public void run()
				{
					try
					{
						//System.out.println("Начато скачивание превью");
						Image img = VikaUtils.downloadImage(prevImgUrl);
						//System.out.println("Ресайз превью: исходное "+img.getWidth()+"х"+img.getHeight());
						
						double aspectR = (double)img.getWidth() / (double)img.getHeight();
						double SAR = (double)DisplayUtils.width / (double)DisplayUtils.height;
						boolean vertical = aspectR < SAR;
						int w = 0; int h = 0;
						if(vertical) {
							h = DisplayUtils.height;
							w = (int)(h*aspectR);
						}
						else
						{
							w = DisplayUtils.width;
							h = (int)(w/aspectR);
						}
						VikaTouch.docsScr.previewX = (DisplayUtils.width - w)/2;
						VikaTouch.docsScr.previewY = (DisplayUtils.height - h)/2;
						VikaTouch.docsScr.previewImage = VikaUtils.resize(img, w, h);
					}
					catch(Exception e)
					{
						VikaTouch.docsScr.isPreviewShown = false;
						VikaTouch.error(e, ErrorCodes.DOCPREVIEWLOAD);
					}
				}
			}).start();
		}
	}

	public void tap(int x, int y)
	{
		try
		{
			if(x<DisplayUtils.width - 50)
			{
				StartPreview();
			}
			else
			{
				VikaTouch.appInst.platformRequest(url);
			}
		}
		catch (Exception e)
		{

		}
	}
	
	public void keyPressed(int key)
	{
		if(DocsScreen.current.isPreviewShown)
		{
			DocsScreen.current.isPreviewShown = false;
			DocsScreen.current.previewImage = null;
			return;
		}
		if(type == TYPE_PHOTO)
		{
			if(key == KEY_FUNC)
			{
				try
				{
					VikaTouch.appInst.platformRequest(url);
				}
				catch (ConnectionNotFoundException e) 
				{
					
				}
			}
			if(key == KEY_OK)
			{
				StartPreview();
			}
		}
		else
		{
			if(key == KEY_OK || key == KEY_FUNC)
			{
				try
				{
					VikaTouch.appInst.platformRequest(url);
				}
				catch (ConnectionNotFoundException e) 
				{
					
				}
			}
		}
	}
}
