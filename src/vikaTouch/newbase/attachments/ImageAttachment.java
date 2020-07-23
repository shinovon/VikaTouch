package vikaTouch.newbase.attachments;

import javax.microedition.lcdui.Image;

public abstract class ImageAttachment 
	extends Attachment
{
	public abstract Image getPreviewImage();
	
	public abstract Image getFullImage();
	
	public abstract Image getImage(int height);

}
