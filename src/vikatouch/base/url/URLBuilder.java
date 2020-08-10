package vikatouch.base.url;

import vikamobilebase.URLDecoder;
import vikatouch.base.VikaTouch;

public class URLBuilder
{
	
	private String urlString = "";
	private boolean state = false;
	
	public URLBuilder(String host, String req)
	{
		append(host);
		append("/");
		append(req);
	}
	
	public URLBuilder(String method)
	{
		append(VikaTouch.API);
		append("/method/");
		append(method);
		addField("access_token", VikaTouch.accessToken);
		addField("v", VikaTouch.API_VERSION);
	}
	
	public URLBuilder addField(String param, String value)
	{
		if(!state)
			append("?");
		else
			append("&");
		state = true;
		append(param);
		append("=");
		append(URLDecoder.encode(value));
		return this;
	}

	public URLBuilder append(String string)
	{
		urlString += string;
		return this;
	}
	
	public String toString()
	{
		return urlString;
	}
	
	public static final String makeSimpleURL(String method)
	{
		return new URLBuilder(method).toString();
	}

	public URLBuilder addField(String param, int value) {
		return addField(param, "" + value);
	}

}