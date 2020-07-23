package vikaTouch.newbase.attachments;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import vikaTouch.newbase.JSONBase;

public class PhotoSize
{
	public int height;
	public String url;
	public String type;
	public int width;
	
	public static PhotoSize[] parseSizes(JSONArray jsonArray)
	{
		PhotoSize[] sizes = new PhotoSize[10];
		if(jsonArray == null)
			return sizes;
		try
		{
			int len = jsonArray.length();
			for(int i = 0; i < len; i++)
			{
				if(i >= 10)
				{
					break;
				}
				sizes[i] = parseSize(jsonArray.getJSONObject(i));
			}
		}
		catch (Exception e)
		{
			
		}
		return sizes;
	}
	
	public static PhotoSize parseSize(JSONObject jsonObject)
	{
		PhotoSize ps = new PhotoSize();
		ps.height = jsonObject.optInt("height");
		ps.url = JSONBase.fixJSONString(jsonObject.optString("url"));
		ps.type = jsonObject.optString("type");
		ps.width = jsonObject.optInt("width");
		return ps;
	}
}