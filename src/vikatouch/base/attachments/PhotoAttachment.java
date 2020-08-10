package vikatouch.base.attachments;

import java.io.IOException;

import javax.microedition.lcdui.Image;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.base.items.MsgItem;

public class PhotoAttachment
	extends ImageAttachment
{
	public int albumid;
	public long ownerid;
	public long userid = 100;
	public int origwidth;
	public int origheight;
	public PhotoSize[] sizes = new PhotoSize[10];
	
	// for msg
	public int renderH;
	public int renderW;
	public Image renderImg = null;

	public PhotoAttachment()
	{
		this.type = "photo";
	}
	
	public void parseJSON()
	{
		sizes = PhotoSize.parseSizes(json.optJSONArray("sizes"));
		origwidth = json.optInt("width");
		origheight = json.optInt("height");
		ownerid = json.optInt("owner_id");
		albumid = json.optInt("album_id");
		userid = json.optInt("user_id");
	}
	
	public Image getImg(int i)
	{
		try
		{
			return VikaUtils.downloadImage(sizes[i].url);
		}
		catch(Exception e)
		{
			try {
				return Image.createImage("/image.png");
			} catch (IOException e1)
			{
				return null;
			}
		}
	}

	public String getPreviewImageUrl()
	{
		PhotoSize ps = null;
		try
		{
			ps = PhotoSize.getSize(sizes, "x");
			if(ps==null) throw new Exception();
		}
		catch (Exception e1)
		{
			try
			{
				ps = PhotoSize.getSize(sizes, "o");
			}
			catch (Exception e2)
			{ }
		}
		if(ps==null) return null;
		return ps.url;
	}

	public String getFullImageUrl()
	{
		try
		{
			PhotoSize ps = PhotoSize.getSize(sizes, "o");
			return ps.url;
		}
		catch (Exception e2)
		{
			return null;
		}
	}
	
	// имеющиеся методы для идиотов. Точнее я не уверен что вот то будет работать, и мне проще написать это чем 2 часа ловить баги. Потом втюхаю в I.
	public PhotoSize getMessageImage()
	{
		PhotoSize ps = PhotoSize.getSize(sizes, "s");
		if(ps==null) PhotoSize.getSize(sizes, "d");
		if(ps==null) PhotoSize.getSize(sizes, "m");
		if(ps==null) PhotoSize.getSize(sizes, "o");
		return ps;
	}
	// Загружает картинку
	public void load ()
	{
		try 
		{
			PhotoSize ps = getMessageImage();
			Image i = VikaUtils.downloadImage(ps.url);
			int w = Math.min((int)(DisplayUtils.width*0.6), MsgItem.msgWidth - MsgItem.attMargin*2);
			if(ps.width>w)
			{
				i = VikaUtils.resize(i, w, -1);
			}
			
			renderH = i.getHeight();
			renderW = i.getWidth();
			renderImg = i;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public int getDrawHeight() { return renderH; }

	// Нестабильно! Нельзя такое по индексу получать.
	public Image getPreviewImage() {
		return getImg(0);
	}
	public Image getFullImage() {
		return getImg(6);
	}

	public Image getImage(int height) {
		// TODO Auto-generated method stub
		return null;
	}
}

