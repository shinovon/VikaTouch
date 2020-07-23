package vikaTouch.newbase.items;

import org.json.me.JSONObject;

public class MessageItem
	extends Item
{
	public MessageItem(JSONObject json)
	{
		super(json);
	}

	public long mid;

	public void parseJSON()
	{
		super.parseJSON();
	}

}
