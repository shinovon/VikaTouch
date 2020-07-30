package vikaTouch.newbase.items;

import org.json.me.JSONObject;

import vikaUI.PressableUIItem;

public abstract class JSONUIItem
	extends JSONItem
	implements PressableUIItem
{
	public int itemDrawHeight;
	public boolean selected;

	public JSONUIItem(JSONObject json)
	{
		super(json);
	}
	
	public int getDrawHeight()
	{
		return itemDrawHeight;
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
}
