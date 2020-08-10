package ru.nnproject.vikaui.popup;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.base.VikaTouch;
import vikatouch.base.attachments.ISocialable;
import vikatouch.base.attachments.PhotoAttachment;
import vikatouch.base.items.DocItem;
import vikatouch.base.utils.ErrorCodes;

public class ImagePreview extends VikaNotice {

	public ImagePreview (String url)
	{
		
	}
	public ImagePreview (PhotoAttachment photo)
	{
		imgUrl = photo.getPreviewImageUrl();
		downloadUrl = photo.getFullImageUrl();
		socialActions = null;
		Load();
	}
	public ImagePreview (DocItem doc)
	{
		imgUrl = doc.prevImgUrl;
		downloadUrl = doc.url;
		socialActions = doc;
		Load();
	}
	
	public Image img;
	public String imgUrl;
	public String downloadUrl;
	public ISocialable socialActions;
	
	public int drX; public int drY;
	
	private void Load()
	{
		VikaTouch.loading = true;
		(new Thread()
		{
			public void run()
			{
				try
				{
					//System.out.println("Начато скачивание превью");
					Image dimg = VikaUtils.downloadImage(imgUrl);
					//System.out.println("Ресайз превью: исходное "+img.getWidth()+"х"+img.getHeight());
					
					double aspectR = (double)dimg.getWidth() / (double)dimg.getHeight();
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
					drX = (DisplayUtils.width - w)/2;
					drY = (DisplayUtils.height - h)/2;
					img = VikaUtils.resize(dimg, w, h);
				}
				catch(Exception e)
				{
					VikaTouch.error(e, ErrorCodes.DOCPREVIEWLOAD);
				}
			}
		}).start();
	}
	
	public void draw(Graphics g) {
		
	}

}
