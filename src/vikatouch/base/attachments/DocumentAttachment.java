package vikatouch.base.attachments;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.json.me.JSONException;

import ru.nnproject.vikaui.utils.ColorUtils;
import vikatouch.base.IconsManager;
import vikatouch.base.VikaTouch;
import vikatouch.base.utils.ErrorCodes;

public class DocumentAttachment
	extends Attachment
{
	public DocumentAttachment() {
		this.type = "doc";
	}
	
	public String name;
	private String url;
	private int docType;
	public int size;
	private String ext;
	private String prevImgUrl;

	public void parseJSON() 
	{
		try
		{
			name = json.optString("title");
			url = fixJSONString(json.optString("url"));
			size = json.optInt("size");
			ext = json.optString("ext");
			docType = json.optInt("type");

			if(!json.isNull("preview"))
			{
				PhotoSize[] prevSizes = PhotoSize.parseSizes(json.getJSONObject("preview").getJSONObject("photo").getJSONArray("sizes"));

				PhotoSize prevPs = null;
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
					{ }
				}
				if(prevPs != null)
					prevImgUrl = fixJSONString(prevPs.url);
			}
		}
		catch (JSONException e)
		{ }
		catch (Exception e)
		{
			e.printStackTrace();
			VikaTouch.error(e, ErrorCodes.DOCPARSE);
		}

		System.gc();
	}
	public int getDrawHeight()
	{
		return 40;
	}
	public void draw(Graphics g, int x1, int y1, int w)
	{
		ColorUtils.setcolor(g, ColorUtils.BACKGROUND);
		g.fillRect(x1, y1, w, getDrawHeight());
		g.drawImage(IconsManager.ico[IconsManager.DOCS], x1+4, y1+8, 0);
		ColorUtils.setcolor(g, ColorUtils.COLOR1);
		Font f = Font.getFont(0, Font.STYLE_BOLD, Font.SIZE_SMALL);
		g.setFont(f);
		if(name!=null)
			g.drawString(name, x1+34, y1 + 10 - f.getHeight()/2, 0);
		ColorUtils.setcolor(g, ColorUtils.TEXT);
		f = Font.getFont(0, 0, Font.SIZE_SMALL);
		g.setFont(f);
		g.drawString(size / 1000 + "kb", x1+34, y1 + 30 - f.getHeight()/2, 0);
	}

}
