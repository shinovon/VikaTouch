package vikaTouch.newbase;

import javax.microedition.lcdui.List;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.items.DialogItem;

public class Dialogs
{
	
	public static DialogItem[] dialogs = new DialogItem[15];
	
	public static void refreshDialogsList()
	{
		try
		{
			String x = VikaUtils.download(new URLBuilder("messages.getConversations").addField("filter", "all").addField("count", ""+15));
			try {
				JSONObject response = new JSONObject(x).getJSONObject("response");
				JSONArray array = response.getJSONArray("items");
				for(int i = 0; i < array.length(); i++)
				{
					JSONObject item = array.getJSONObject(i);
					dialogs[i] = new DialogItem(item);
				}
				VikaTouch.has = response.getInt("unread_count");
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		catch (NullPointerException e)
		{
			VikaTouch.warn("Переход в оффлайн режим!");
			VikaTouch.offlineMode = true;
		}
	}
	
	public static void refreshDialog()
	{
		
	}

}
