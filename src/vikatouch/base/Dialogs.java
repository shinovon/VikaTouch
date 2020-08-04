package vikatouch.base;

import java.util.TimerTask;

import javax.microedition.lcdui.List;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import vikamobilebase.VikaUtils;
import vikatouch.base.items.ConversationItem;
import vikatouch.screens.ChatScreen;

public class Dialogs
	extends TimerTask
{
	
	private static final int dialogsCount = 15;
	
	public static ConversationItem[] dialogs = new ConversationItem[15];
	
	public static JSONArray profiles;
	
	public static JSONArray groups;

	public static int itemsCount;

	public static boolean selected;
	
	public static Thread downloaderThread;
	
	public static void refreshDialogsList()
	{
		if(downloaderThread != null && downloaderThread.isAlive())
			downloaderThread.interrupt();
		
		downloaderThread = new Thread()
		{
			public void run()
			{
				try
				{
					VikaTouch.loading = true;
					String x = VikaUtils.download(new URLBuilder("messages.getConversations").addField("count", "1"));
					try
					{
						VikaTouch.loading = true;
						JSONObject response = new JSONObject(x).getJSONObject("response");
						JSONArray items = response.getJSONArray("items");
						JSONObject item = items.getJSONObject(0);
						boolean u = dialogs[0] == null;
						int has = 0;
						try
						{
							u = dialogs[0] == null || !item.getJSONObject("last_message").optString("text").substring(0, 7).equalsIgnoreCase(dialogs[0].lastmessage.text.substring(0, 7));
						}
						catch (Exception e)
						{
							
						}
						has = response.optInt("unread_count");
						itemsCount = response.optInt("count");
						if(itemsCount > dialogsCount)
						{
							itemsCount = dialogsCount;
						}
						if(VikaTouch.unreadCount != has || has > 0 || u)
						{
							VikaTouch.unreadCount = has;
							VikaTouch.loading = true;
							x = VikaUtils.download(new URLBuilder("messages.getConversations").addField("filter", "all").addField("extended", "1").addField("count", dialogsCount));
							VikaTouch.loading = true;
							response = new JSONObject(x).getJSONObject("response");
							items = response.getJSONArray("items");
							profiles = response.getJSONArray("profiles");
							groups = response.optJSONArray("groups");
							if(itemsCount > dialogsCount)
							{
								itemsCount = dialogsCount;
							}
							for(int i = 0; i < itemsCount; i++)
							{
								item = items.getJSONObject(i);
								dialogs[i] = new ConversationItem(item);
								dialogs[i].parseJSON();
							}
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}

					VikaTouch.loading = false;
				}
				catch (NullPointerException e)
				{
					VikaTouch.warn("Переход в оффлайн режим!");
					VikaTouch.offlineMode = true;
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				VikaTouch.loading = false;
			}
		};
		
		downloaderThread.start();
	}
	
	public static void refreshDialog()
	{
		
	}

	public static void openDialog(ConversationItem dialogItem)
	{
		openDialog(dialogItem.peerId);
	}

	public static void openDialog(int peerId)
	{
		VikaTouch.setDisplay(new ChatScreen(peerId));
	}

	public void run()
	{
		if(!VikaTouch.offlineMode)
		{
			refreshDialogsList();
		}
	}

}
