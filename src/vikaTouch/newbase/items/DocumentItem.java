package vikaTouch.newbase.items;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.JSONBase;
import vikaTouch.newbase.attachments.PhotoSize;

public class DocumentItem
	extends Item
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
	
	public static final int BORDER = 2;
	
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

	public DocumentItem(JSONObject json)
	{
		super(json);
		itemDrawHeight = 50;
	}
	
	public void parseJSON()
	{
		prevSizes = new PhotoSize[10];
		
		try
		{
			name = json.optString("title");
			url = json.optString("url");
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
					iconPs = PhotoSize.getSize(prevSizes, 's');
				}
				catch (Exception e)
				{
				}

				try
				{
					prevPs = PhotoSize.getSize(prevSizes, 'o');
				}
				catch (Exception e1)
				{
					try
					{
						prevPs = PhotoSize.getSize(prevSizes, 'x');
					}
					catch (Exception e2)
					{
						//не достучались до превьюхи..
					}
				}
				
				if(iconPs != null)
				{
					iconUrl = iconPs.url;
				}
				
				if(prevPs != null)
				{
					prevImgUrl = prevPs.url;
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			//Предпросмотр не завезли - видимо не картинка. Ну и ладно.
		}
		catch (Exception e)
		{
			e.printStackTrace();
			VikaTouch.error(e, "Обработка объектов: Документ");
		}
		
		setDrawHeight();

		System.gc(); 
	}
	
	private void setDrawHeight()
	{
		switch(DisplayUtils.idispi)
		{
			case DisplayUtils.DISPLAY_PORTRAIT:
			case DisplayUtils.DISPLAY_ALBUM:
			case DisplayUtils.DISPLAY_E6:
			{
				itemDrawHeight = 48;
				break;
			}
			case DisplayUtils.DISPLAY_S40:
			case DisplayUtils.DISPLAY_ASHA311:
			case DisplayUtils.DISPLAY_EQWERTY:
			{
				itemDrawHeight = 24;
				break;
			}
			default:
			{
				itemDrawHeight = 48;
				break;
			}
		}
		itemDrawHeight += BORDER;
	}

	public void paint(Graphics g, int y, int scrolled)
	{
		if(iconImg == null)
			iconImg = getPreviewImage();
		ColorUtils.setcolor(g, 0);
		g.drawString(name, 73, y + 16, 0);
		ColorUtils.setcolor(g, 6);
		g.drawString(size / 1000 + "кб", 73, y + 40, 0);
		if(iconImg != null)
		{
			//System.out.println("Иконка "+i);
			g.drawImage(iconImg, 14, y + (BORDER / 2), 0);
		}
	}

	private Image getPreviewImage()
	{
		Image img = null;
		try
		{
			img = DisplayUtils.resizeItemPreview(VikaUtils.downloadImage(iconUrl));
		}
		catch (Exception e)
		{
			try
			{
				switch(type)
				{
					case TYPE_PHOTO:
					case TYPE_GIF:
						return DisplayUtils.resizeItemPreview(VikaTouch.camera);
					case TYPE_AUDIO:
					case TYPE_VIDEO:
						return DisplayUtils.resizeItemPreview(Image.createImage("/docmus.png"));
					case TYPE_TEXT:
					case TYPE_ARCHIVE:
					case TYPE_EBOOK:
					case TYPE_UNKNOWN:
						return DisplayUtils.resizeItemPreview(Image.createImage("/docfile.png"));
					default:
						return null;
				}
			}
			catch (Exception e2)
			{
				
			}
		}
		return img;
	}
	
	public void tap(int x, int y)
	{
		try
		{
			VikaTouch.inst.platformRequest(url);
		}
		catch (Exception e)
		{
			
		}
	}

}
