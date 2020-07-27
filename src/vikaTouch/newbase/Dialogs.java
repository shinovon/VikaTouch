package vikaTouch.newbase;

import java.util.TimerTask;

import javax.microedition.lcdui.List;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.base.VikaUtils;
import vikaTouch.newbase.items.DialogItem;

public class Dialogs
	extends TimerTask
{
	
	private static final int dialogsCount = 15;
	
	public static DialogItem[] dialogs = new DialogItem[15];
	
	public static JSONArray profiles;
	
	public static JSONArray groups;

	public static int itemsCount;

	public static boolean selected;
	
	public static void refreshDialogsList()
	{
		try
		{
			String x = VikaUtils.download(new URLBuilder("messages.getConversations").addField("count", "0"));
			try
			{
				JSONObject response = new JSONObject(x).getJSONObject("response");
				int has = response.optInt("unread_count");
				if(VikaTouch.has != has || has > 0)
				{
					VikaTouch.has = has;
					x = VikaUtils.download(new URLBuilder("messages.getConversations").addField("filter", "all").addField("extended", "1").addField("count", dialogsCount));
					response = new JSONObject(x).getJSONObject("response");
					final JSONArray items = response.getJSONArray("items");
					profiles = response.getJSONArray("profiles");
					groups = response.optJSONArray("items");
					itemsCount = response.optInt("count");
					if(itemsCount > dialogsCount)
					{
						itemsCount = dialogsCount;
					}
					for(int i = 0; i < items.length(); i++)
					{
						final JSONObject item = items.getJSONObject(i);
						dialogs[i] = new DialogItem(item);
						dialogs[i].parseJSON();
					}
				}
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
		catch (Exception e)
		{
			VikaTouch.warn("Не удалось обновить диалоги!");
		}
		
	}
	
	public static void refreshDialog()
	{
		
	}

	public static void openDialog(DialogItem dialogItem)
	{
		
	}

	public void run()
	{
		if(!VikaTouch.offlineMode)
		{
			refreshDialogsList();
		}
	}

}
