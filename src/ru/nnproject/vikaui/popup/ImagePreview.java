package ru.nnproject.vikaui.popup;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import ru.nnproject.vikaui.utils.DisplayUtils;
import vikamobilebase.VikaUtils;
import vikatouch.base.IconsManager;
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
		socialActions = (ISocialable) doc;
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
		if(img == null) {
			g.drawImage(IconsManager.ico[IconsManager.CLOSE], currX, 0, 0);
			VikaTouch.loading = true;
		} else {
			VikaTouch.loading = false;
			g.setGrayScale(50);
			g.fillRect(0, 0, DisplayUtils.width, DisplayUtils.height);
			g.drawImage(img, drX, drY, 0);
			
			// drawing buttons
			{
				int currX = DisplayUtils.width;
				currX-=24;
				g.drawImage(IconsManager.ico[IconsManager.CLOSE], currX, 0, 0);
				currX-=24;
				if(downloadUrl!=null)
				{
					g.drawImage(IconsManager.ico[IconsManager.DOCS], currX, 0, 0);
					currX -= 24;
				}
				if(socialActions!=null) 
				{
					if(socialActions.canSave())
					{
						g.drawImage(IconsManager.ico[IconsManager.ADD], currX, 0, 0);
						currX -= 24;
					}
					g.drawImage(IconsManager.ico[IconsManager.SEND], currX, 0, 0);
					currX -= 24;
					if(socialActions.commentsAliveable())
					{
						g.drawImage(IconsManager.ico[IconsManager.COMMENTS], currX, 0, 0);
						currX -= 24;
					}
					if(socialActions.canLike())
					{
						g.drawImage(IconsManager.ico[socialActions.getLikeStatus()?IconsManager.LIKE_F:IconsManager.LIKE], currX, 0, 0);
						currX -= 24;
					}
				}
			}
		}
	}
	
	public void release(int x, int y)
	{
		if(y>24) return;
		
		int currX = DisplayUtils.width;
		currX-=24;
		// закрытие
		if(x>currX)
		{
			VikaTouch.canvas.currentAlert = null;
		}
		if(img == null) return;
		currX-=24;
		if(downloadUrl!=null)
		{
			// скачка
			if(x>currX)
			{
				try
				{
					VikaTouch.appInst.platformRequest(downloadUrl);
				}
				catch (ConnectionNotFoundException e) 
				{
					VikaTouch.popup(new InfoPopup("Не удалось открыть. Возможно, произошла ошибка при обработке адреса либо ваше устройство не может открыть этот документ.", null));
				}
			}
			currX -= 24;
		}
		if(socialActions!=null) 
		{
			if(socialActions.canSave())
			{
				// сохранение
				if(x>currX)
				{
					VikaTouch.popup(new InfoPopup("Сохранение пока не реализовано.", null));
				}
				currX -= 24;
			}
			// отправка
			if(x>currX)
			{
				VikaTouch.popup(new InfoPopup("Отправку ещё не завезли", null));
			}
			currX -= 24;
			if(socialActions.commentsAliveable())
			{
				// каменты
				if(x>currX)
				{
					VikaTouch.popup(new InfoPopup("Комменты тоже.", null));
				}
				currX -= 24;
			}
			if(socialActions.canLike())
			{
				// луцки
				if(x>currX)
				{
					VikaTouch.popup(new InfoPopup("Лайки сожрали неко", null));
				}
				currX -= 24;
			}
		}
		
	}

}
