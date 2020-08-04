package vikatouch.base.items;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import ru.nnproject.vikaui.ColorUtils;
import ru.nnproject.vikaui.DisplayUtils;
import ru.nnproject.vikaui.TextBreaker;
import ru.nnproject.vikaui.UIItem;
import vikamobilebase.ErrorCodes;
import vikamobilebase.VikaUtils;
import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.attachments.Attachment;
import vikatouch.base.attachments.PhotoAttachment;
import vikatouch.screens.NewsScreen;

public class PostItem
	extends JSONUIItem
{
	
	private JSONObject json2;

	public PostItem(JSONObject json, JSONObject ob)
	{
		super(json);
		json2 = ob;
	}

	public int ownerid;
	public int id;
	public int views;
	public int reposts;
	public int likes;
	public boolean canlike;
	public String copyright;
	public int replyownerid;
	public int replypostid;
	public Image prevImage;
	private String avaurl;
	private String[] drawText;
	public String name = "";
	public Image ava;
	public boolean isreply;
	private boolean largefont;
	private int sourceid;
	private boolean full;
	private String reposterName;
	private String type;
	private String data;
	
	public void parseJSON()
	{
		super.parseJSON();
		super.parseAttachments();
		try
		{
			if(text == null || text == "")
			{
				text = fixJSONString(json2.optString("text"));
			}
		}
		catch (Exception e)
		{
			VikaTouch.error(e, ErrorCodes.POSTTEXT);
			e.printStackTrace();
			text = "";
		}
		try
		{
			likes = json2.optJSONObject("likes").optInt("count");
			reposts = json2.optJSONObject("reposts").optInt("count");
			views = json2.optJSONObject("views").optInt("count");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		

		try
		{
			final JSONObject postSource = json2.getJSONObject("post_source");
			data = postSource.optString("data");
		}
		catch (Exception e)
		{
			
		}
		
		type = json2.optString("type"); 
		
		copyright = json2.optString("copyright");
		ownerid = json2.optInt("owner_id");
		sourceid = json2.optInt("source_id");
		id = json2.optInt("id");
		replyownerid = json2.optInt("reply_owner_id");
		replypostid = json2.optInt("reply_post_id");
		if(id == 0)
		{
			copyright = json.optString("copyright");
			ownerid = json.optInt("owner_id");
			id = json.optInt("id");
			replyownerid = json.optInt("reply_owner_id");
			replypostid = json.optInt("reply_post_id");
		}
		//itemDrawHeight = 82;
		isreply = replypostid != 0;
		itemDrawHeight = 72;
		int xx = 0;
		xx = replyownerid;
		if(xx == 0)
			xx = fromid;
		if(xx == 0)
			xx = ownerid;
		if(xx == 0)
			xx = sourceid;
		labelgetnameandphoto:
		{
			if(xx < 0)
			{
				for(int i = 0; i < NewsScreen.groups.length(); i++)
				{
					try
					{
						JSONObject group = NewsScreen.groups.getJSONObject(i);
						final int gid = group.optInt("id");
						if(gid == -xx)
						{
							name = group.optString("name");
							avaurl = fixJSONString(group.optString("photo_50"));
							break labelgetnameandphoto;
						}
					}
					catch (Exception e)
					{
						VikaTouch.error(e, ErrorCodes.POSTAVAGROUPS);
						e.printStackTrace();
					}
				}
			}
		}
		
		boolean b1 = false;
		boolean b2 = false;
		for(int i = 0; i < NewsScreen.profiles.length(); i++)
		{
			try
			{
				JSONObject profile = NewsScreen.profiles.getJSONObject(i);
				int uid = profile.optInt("id");
				if(sourceid <= 0)
				{
					b2 = true;
				}
				if(!b2 && uid == sourceid)
				{
					reposterName = "" + profile.optString("first_name") + " " + profile.optString("last_name");
					b2 = true;
				}
				if(xx < 0)
				{
					b1 = true;
				}
				if(!b1 && uid == xx)
				{
					name = "" + profile.optString("first_name") + " " + profile.optString("last_name");
					b1 = true;
					JSONObject jo2 = new JSONObject(VikaUtils.download(new URLBuilder("users.get").addField("user_ids", ""+profile.optInt("id")).addField("fields", "photo_50"))).getJSONArray("response").getJSONObject(0);
					avaurl = fixJSONString(jo2.optString("photo_50"));
				}
				if(b1 && b2)
				{
					break;
				}
			}
			catch (Exception e)
			{
				VikaTouch.error(e, ErrorCodes.POSTAVAPROFILES);
				e.printStackTrace();
			}
		}
		
		try
		{
			if(avaurl != null)
			{
				ava = VikaUtils.downloadImage(avaurl);
			}
		}
		catch (Exception e)
		{
		}
		
		if(reposterName != null)
		{
			itemDrawHeight += 43;
		}

		try
		{
			if(attachments[0] != null)
			{
				switch(DisplayUtils.idispi)
				{
					case DisplayUtils.DISPLAY_PORTRAIT:
					case DisplayUtils.DISPLAY_ALBUM:
					{
						if(attachments[0] instanceof PhotoAttachment)
							prevImage = ((PhotoAttachment)attachments[0]).getImg(3);
						break;
					}
					case DisplayUtils.DISPLAY_S40:
					case DisplayUtils.DISPLAY_ASHA311:
					{
						if(attachments[0] instanceof PhotoAttachment)
							prevImage = ((PhotoAttachment)attachments[0]).getImg(2);
						break;
					}
					default:
					{
						if(attachments[0] instanceof PhotoAttachment)
							prevImage = ((PhotoAttachment)attachments[0]).getImg(3);
						break;
					}
				}
				if(prevImage != null)
					itemDrawHeight += prevImage.getHeight() + 16;
			}
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Получение фотографии поста");
			e.printStackTrace();
		}
		
		if(data != null && data.equalsIgnoreCase("profile_photo"))
		{
			text = "обновил фотографию на странице";
		}
		
		drawText = TextBreaker.breakText(text, largefont, this, full, DisplayUtils.width - 32);
		
		System.gc();
	}
	
	public void paint(Graphics g, int y, int scrolled)
	{
		int yy = 10 + y;
		
		if(ava != null)
		{
			g.drawImage(ava, 16, 10 + y, 0);
			yy += ava.getHeight() + 12;
		}
		else
		{
			g.drawImage(VikaTouch.cameraImg, 16, 10 + y, 0);
			yy += VikaTouch.cameraImg.getHeight() + 12;
		}
		
		ColorUtils.setcolor(g, 5);
		
		g.drawString("" + name, 76, 18 + y, 0);

		if(largefont)
			g.setFont(Font.getFont(0, 0, Font.SIZE_LARGE));
		else
			g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
		
		for(int i = 0; i < drawText.length; i++)
		{
			if(drawText[i] != null)
			{
				if(drawText[i].length() > 0)
				{
					if(i == 9 && drawText.length == 10)
						g.setColor(68, 104, 143);
					
					g.drawString(""+drawText[i], 16, yy, 0);
					
					ColorUtils.setcolor(g, 5);
				}
				yy += 24;
			}
			else
			{
				break;
			}
		}
		
		if(largefont)
			g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));

		if(prevImage != null)
		{
			int ix = (DisplayUtils.width - prevImage.getWidth()) / 2;
			g.drawImage(prevImage, ix, yy + 3, 0);
		}
	}

	public void tap(int x, int y)
	{
		full = true;
		parseJSON();
	}

	public void keyPressed(int key)
	{
		
	}
}
