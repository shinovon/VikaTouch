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
	
	private static final int dialogsCount = 15;
	
	public static DialogItem[] dialogs = new DialogItem[15];
	
	public static JSONArray profiles;
	
	public static JSONArray groups;
	
	public static void refreshDialogsList()
	{
		try
		{
			String x = VikaUtils.download(new URLBuilder("messages.getConversations").addField("filter", "all").addField("extended", "1").addField("count", dialogsCount));
			try {
				final JSONObject response = new JSONObject(x).getJSONObject("response");
				final JSONArray items = response.getJSONArray("items");
				profiles = response.getJSONArray("profiles");
				groups = response.optJSONArray("items");
				if(VikaTouch.menu != null)
				{
					VikaTouch.menu.itemsCount = 0;
				}
				for(int i = 0; i < items.length(); i++)
				{
					final JSONObject item = items.getJSONObject(i);
					dialogs[i] = new DialogItem(item);
					dialogs[i].parseJSON();
					if(VikaTouch.menu != null)
					{
						VikaTouch.menu.itemsCount++;
					}
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

	public static void openDialog(DialogItem dialogItem)
	{
		
	}

}
