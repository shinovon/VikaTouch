package vikaTouch.newbase.items;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.canvas.NewsCanvas;
import vikaTouch.newbase.ColorUtils;
import vikaTouch.newbase.DisplayUtils;
import vikaTouch.newbase.URLBuilder;
import vikaTouch.newbase.attachments.Attachment;
import vikaTouch.newbase.attachments.PhotoAttachment;

public class PostItem
	extends Item
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
	public String predrawText;
	public String avaurl;
	public String[] drawText;
	public String name = "";
	public Image ava;
	public boolean isreply;
	private char[] chars;
	private boolean largefont;
	private int sourceid;
	private boolean full;
	
	public void parseJSON()
	{
		super.parseJSON();
		try
		{
			if(text == null || text == "")
			{
				text = fixJSONString(json2.optString("text"));
			}
		}
		catch (Exception e)
		{
			VikaTouch.error(e, "Получение текста поста");
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
		copyright = json2.optString("copyright");
		ownerid = json2.optInt("owner_id");
		sourceid = json2.optInt("source_id");
		id = json2.optInt("id");
		replyownerid = json2.optInt("reply_owner_id");
		replypostid = json2.optInt("reply_post_id");
		if(ownerid == 0)
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
				for(int i = 0; i < NewsCanvas.arraygroups.length(); i++)
				{
					try
					{
						JSONObject joo = NewsCanvas.arraygroups.getJSONObject(i);
						final int gid = joo.optInt("id");
						if(gid == -xx)
						{
							name = joo.optString("name");
							avaurl = fixJSONString(joo.optString("photo_50"));
							break labelgetnameandphoto;
						}
					}
					catch (Exception e)
					{
						VikaTouch.error(e, "Получение аватарки и автора поста: Группы");
						e.printStackTrace();
					}
				}
			}
			else if(xx > 0)
			{
				for(int i = 0; i < NewsCanvas.arrayprofiles.length(); i++)
				{
					System.out.println(i);
					try
					{
						JSONObject joo = NewsCanvas.arrayprofiles.getJSONObject(i);
						int uid = joo.optInt("id");
						if(uid == xx)
						{
							name = "" + joo.optString("first_name") + " " + joo.optString("last_name");
							JSONObject jo2 = new JSONObject(VikaUtils.download(new URLBuilder("users.get").addField("user_ids", ""+joo.optInt("id")).addField("fields", "photo_50"))).getJSONArray("response").getJSONObject(0);
							avaurl = fixJSONString(jo2.optString("photo_50"));
							break labelgetnameandphoto;
						}
					}
					catch (Exception e)
					{
						VikaTouch.error(e, "Получение аватарки и автора поста: Пользователи");
						e.printStackTrace();
					}
				}
			}
		}
		
		if(name == null)
		{
			VikaTouch.error("Обратите внимание! Текст поста равен null!", false);
			name = "null";
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
		boolean b = false;

		try
		{
			if(attachments[0] != null)
			{
				switch(DisplayUtils.idispi)
				{
					case 1:
					{
						if(attachments[0] instanceof PhotoAttachment)
							prevImage = ((PhotoAttachment)attachments[0]).getImg(3);
						break;
					}
					case 2:
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
		//text = "== ТЕСТ VIKA TOUCH ==\n" + 
		//		"1 2 3 4 5 6 7 8 9 0\n" + 
		//		"1234567890 123456789012345678901234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 vv 1234567890 1234567890\n" + 
		//		"Сплошник\n" + 
		//		"123456789012345678901234567890123456789012345678901234567890а12345678901234567890123456789012345678901234567890123456789012а345678901234567890123456789012345678901234567890123456789012а3456789012345678901234567890123456789012345678901234567890123а456789012345678901234567890";
		if(text.length() > 24 || text.indexOf("\n") != -1)
		{
			
			if(text.length() >= 360)
			{
				//itemDrawHeight = 300;
				//predrawText = text.substring(0, 360) + "\n";
				//b = true;
				predrawText = text;
			}
			else
			{
				predrawText = text;
			}
			

			chars = predrawText.toCharArray();
			int lncount = 0;
			drawText = new String[10];
			if(full)
				drawText = new String[100];
			try
			{
				if(Font.getDefaultFont().stringWidth(predrawText) > DisplayUtils.width - 32)
				{
					String x2 = "";
					for(int i2 = 0; i2 < predrawText.length(); i2++)
					{
						if(chars[i2] == '\n')
						{
							drawText[lncount] = x2;
							x2 = "";
							lncount++;
							if(!b)
								itemDrawHeight += 24;
						}
						else
						{
							x2 += ""+chars[i2];
							if(Font.getDefaultFont().stringWidth(x2) > DisplayUtils.width - 32)
							{
								drawText[lncount] = x2;
								x2 = "";
								lncount++;
								if(!b)
									itemDrawHeight += 24;
							}
							else if(predrawText.length() - i2 <= 1)
							{
								drawText[lncount] = x2;
								x2 = "";
								lncount++;
								if(!b)
									itemDrawHeight += 24;
							}
						}
					}
				}
				else
				{
					drawText[lncount] = predrawText;
					if(!b)
						itemDrawHeight += 24;
				}
				
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				drawText[9] = "Показать полностью...";
			}
		}
		else
		{
			drawText = new String[2];
			predrawText = text;
			drawText[0] = text; 
			largefont = true;
			if(text.length() > 1)
				itemDrawHeight += 32;
		}
		System.gc();
	}
	
	public void paint(Graphics g, int x, int y)
	{
		int yy = 72 + y;
		if(ava != null)
			g.drawImage(ava, 16, 10 + y, 0);
		else
			g.drawImage(VikaTouch.camera, 16, 10 + y, 0);
		ColorUtils.setcolor(g, 5);
		g.drawString(""+name, 76, 18 + y, 0);
		

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

	public void tap()
	{
		full = true;
		parseJSON();
	}
}
