package vikamobilebase;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


import vikatouch.base.URLBuilder;
import vikatouch.base.VikaTouch;
import vikatouch.base.local.TextLocal;


public final class VikaUtils
{
	public static String parseShortTime(final long paramLong)
	{
		final Calendar cal = Calendar.getInstance();
		
		final Date date = new Date(paramLong * 1000L);
		final Date currentDate = new Date(System.currentTimeMillis());
		
		cal.setTime(date);
		final int day = cal.get(Calendar.DAY_OF_MONTH);
		final int year = cal.get(Calendar.YEAR);
		final int month = cal.get(Calendar.MONTH);
		
		cal.setTime(currentDate);
		final int currentYear = cal.get(Calendar.YEAR);
		
	    final String time = time(date);
	    
	    final long dayDelta = (paramLong / 60L / 60L / 24L) - (System.currentTimeMillis() / 1000L / 60L / 60L / 24L);
	    
	    String result = "Давно";
	    
	    parsing:
	    {
		    if(dayDelta == 0)
		    {
		    	result = time;
		    	break parsing;
		    }
		    else if(dayDelta == 1)
		    {
		    	result = TextLocal.inst.get("date.yesterday");
		    	break parsing;
		    }
		    else if(currentYear == year)
		    {
		    	result = TextLocal.inst.formatChatDate(day, month);
		    	break parsing;
		    }
		    else
		    {
		    	result = TextLocal.inst.formatChatDate(day, month, year);
		    	break parsing;
		    }
	    }
	    
	    return result;
	}
	
	public static String parseTime(final long paramLong)
	{
		final Calendar cal = Calendar.getInstance();
		
		final Date date = new Date(paramLong * 1000L);
		final Date currentDate = new Date(System.currentTimeMillis());
		
		cal.setTime(date);
		final int day = cal.get(Calendar.DAY_OF_MONTH);
		final int year = cal.get(Calendar.YEAR);
		final int month = cal.get(Calendar.MONTH);
		
		cal.setTime(currentDate);
		final int currentYear = cal.get(Calendar.YEAR);
		
	    final String time = time(date);
	    
	    final long dayDelta = (paramLong / 60L / 60L / 24L) - (System.currentTimeMillis() / 1000L / 60L / 60L / 24L);
	    
	    String result;
	    
	    parsing:
	    {
		    if(dayDelta == 0)
		    {
		    	result = TextLocal.inst.get("date.todayat");
		    	result += " " + time;
		    	break parsing;
		    }
		    else if(dayDelta == 1)
		    {
		    	result = TextLocal.inst.get("date.yesterday");
		    	break parsing;
		    }
		    else if(currentYear == year)
		    {
		    	result = TextLocal.inst.formatShortDate(day, month);
		    	break parsing;
		    }
		    else
		    {
		    	result = TextLocal.inst.formatDate(day, month, year);
		    	break parsing;
		    }
	    }
	    
	    return result;
	}

	public static boolean check()
	{
		String url = new URLBuilder("execute").addField("code", "return \"ok\";").toString();
		HttpConnection httpconn = null;
		try
		{
			httpconn = (HttpConnection) Connector.open(url);
		
			if(httpconn.getResponseCode() == 200)
			{
				httpconn.close();
				return true;
			}
			httpconn.close();
			return false;
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	public static String download(URLBuilder url)
	{
		return download(url.toString());
	}
	
	public static String download(String url)
	{
		HttpConnection httpconn = null;
		InputStream is = null;
		InputStreamReader isr = null;
		String result = null;

		try
		{
			httpconn = (HttpConnection) Connector.open(url);
			httpconn.setRequestMethod("GET");
			httpconn.setRequestProperty("User-Agent", "KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
			is = httpconn.openInputStream();
			isr = new InputStreamReader(is, "UTF-8"); 
			StringBuffer sb = new StringBuffer();
			char[] chars;
			int i;
			if (httpconn.getResponseCode() != 200 && httpconn.getResponseCode() != 401)
			{
				if(httpconn.getHeaderField("Location") != null)
				{
					final String replacedURL = httpconn.getHeaderField("Location");
					httpconn.close();
					httpconn = (HttpConnection) Connector.open(replacedURL);
					httpconn.setRequestMethod("GET");
					httpconn.setRequestProperty("User-Agent", "KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
					is = httpconn.openInputStream();
					isr = new InputStreamReader(is, "UTF-16");
					sb = new StringBuffer();
					if (httpconn.getResponseCode() == 200 || httpconn.getResponseCode() == 401)
					{
						chars = new char[262144];
	
						while ((i = isr.read(chars, 0, 262144)) != -1)
						{
							sb.append(chars, 0, i);
						}
	
					}
				}
			}
			else
			{
				chars = new char[262144];
				
				while ((i = isr.read(chars, 0, 262144)) != -1)
				{
					sb.append(chars, 0, i);
				}

			}

			result = replace(sb.toString(), "<br>", " ");
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println("Failed to download " + url);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("Failed to download " + url);
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{
			System.out.println("Failed to download " + url);
			e.printStackTrace();
		}

		try
		{
			isr.close();
		}
		catch (Exception e)
		{
			System.out.println("Failed to close stream");
			e.printStackTrace();
		}

		try
		{
			is.close();
		}
		catch (Exception e)
		{
			System.out.println("Failed to close stream");
			e.printStackTrace();
		}

		try
		{
			httpconn.close();
		}
		catch (Exception e)
		{
			System.out.println("Failed to close stream");
			e.printStackTrace();
		}

		try
		{
			if (VikaTouch.API != "https://api.vk.com:443")
			{
				result = replace(replace(replace(replace(result, "http://cs", "https://cs"), "http:\\/\\/cs", "https://cs"), "https://vk-api", "http://vk-api"), "https:\\/\\/vk-api", "http://vk-api");
			}
		}
		catch (NullPointerException e)
		{
			System.out.println("WARNING!! Response from " + url + " is null!");
			e.printStackTrace();
		}

		return result;
	}
	
	public static String sendPostRequest(String url, String vars)
	{
		HttpConnection httpconn = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		String result = "";

		try {
			httpconn = (HttpConnection) Connector.open(url, 3);
			httpconn.setRequestMethod("POST");
			dos = httpconn.openDataOutputStream();
			byte[] bytes = vars.getBytes();

			int i;
			for (i = 0; i < bytes.length; ++i)
			{
				dos.writeByte(bytes[i]);
			}
			dis = new DataInputStream(httpconn.openInputStream());
			for (; (i = dis.read()) != -1; result += (char) i);
		}
		catch (IOException e)
		{
			result = "ERROR";
		}
		finally
		{
			try
			{
				if (httpconn != null)
				{
					httpconn.close();
				}
			}
			catch (IOException e)
			{
				
			}

			try
			{
				if (dis != null)
				{
					dis.close();
				}
			}
			catch (IOException e)
			{
				
			}

			try
			{
				if (dos != null)
				{
					dos.close();
				}
			}
			catch (IOException e)
			{
				
			}

		}

		return result;
	}

	public static String strToHex(String str)
	{
		char[] chars = str.toCharArray();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < chars.length; ++i)
		{
			char c = chars[i];
			sb.append(Integer.toHexString(c).toUpperCase());
		}

		return sb.toString();
	}
	
	public static String replace(String str, String from, String to)
	{
		final StringBuffer sb = new StringBuffer();
		int j = str.indexOf(from);
		int k = 0;

		for (int i = from.length(); j != -1; j = str.indexOf(from, k))
		{
			sb.append(str.substring(k, j)).append(to);
			k = j + i;
		}

		sb.append(str.substring(k, str.length()));
		return sb.toString();
	}
	
	
	public static boolean startsWith(String str, String need)
	{
		int l = need.length();
		return str.substring(0,l).equalsIgnoreCase(need);
	}
	
	public static Image resize(Image image, int width, int height)
	{
		int origWidth = image.getWidth();
		int origHeight = image.getHeight();
		if (height == -1) {
			height = width * origHeight / origWidth;
		}

		Image newImage;
		Graphics g = (newImage = Image.createImage(width, height)).getGraphics();

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				g.setClip(x, y, 1, 1);
				int xx = x * origWidth / width;
				int yy = y * origHeight / height;
				g.drawImage(image, x - xx, y - yy, 20);
			}
		}

		return Image.createImage(newImage);
	}

	public static Image downloadImage(String url) 
			throws IOException
	{
		// кеширование картинок включается если запрос http
		boolean caching = !startsWith(url, "file");
		String filename = null;
		if(caching)
		{
			filename = 
				replace(
					replace(
						replace(
							replace(
								replace(
									replace(
										replace(
											replace(
												replace(
													replace(
														replace(
															replace(
																replace(
																	replace(
																		replace(
									url
									, "vk-api-proxy.xtrafrancyz.net", "")
									, "?ava=1", "")
									, VikaTouch.API, "")
									, ".userapi.", "")
					, "http:", "")
					, "https:", "")
					, "=", "")
					, "?", "")
					, ":80", "")
					, "\\", "")
					, "/", "")
					, ":443", "")
					, "_", "")
					, "vk.comimages", "")
					, "com", "");
			
			
			
			try
			{
				final Image image = ImageStorage.get(filename);
				if(image != null)
				{
					return image;
				}
			}
			catch (Exception e)
			{
				
			}
		}

		ByteArrayOutputStream baos = null;
		final Connection con = Connector.open(url);
		if(con instanceof HttpConnection)
		{
			HttpConnection var2 = (HttpConnection) con; 
			var2.setRequestMethod("GET");
			var2.setRequestProperty("User-Agent", "KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
			if (var2.getResponseCode() != 200 && var2.getResponseCode() != 401) {
				url = var2.getHeaderField("Location");
			}
			
			var2.close();
		}
		else if(con instanceof FileConnection)
		{
			caching = false;
			con.close();

			FileConnection fileconn;
			DataInputStream dis = (fileconn = (FileConnection) Connector.open(url)).openDataInputStream();
			try
			{
				int length = (int) fileconn.fileSize();
				byte[] imgBytes = new byte[length];
				
				dis.readFully(imgBytes);
				
				try
				{
					return Image.createImage(imgBytes, 0, imgBytes.length);
				}
				catch (IllegalArgumentException e)
				{
					
				}
			}
			finally
			{
				if (dis != null)
				{
					dis.close();
				}

				if (fileconn != null)
				{
					fileconn.close();
				}

				if (baos != null)
				{
					baos.close();
				}

			}
		}
		ContentConnection contconn;
		DataInputStream cin = (contconn = (ContentConnection) Connector.open(url)).openDataInputStream();

		try
		{
			int length;
			byte[] imgBytes;
			if ((length = (int) contconn.getLength()) != -1)
			{
				imgBytes = new byte[length];
				cin.readFully(imgBytes);
			}
			else
			{
				baos = new ByteArrayOutputStream();
				
				int i;
				while ((i = cin.read()) != -1)
				{
					baos.write(i);
				}

				imgBytes = baos.toByteArray();
				baos.close();
			}
			try
			{
				Image image = Image.createImage(imgBytes, 0, imgBytes.length);
				if(image != null && caching)
				{
					ImageStorage.save(filename, image);
				}
				return image;
			}
			catch (IllegalArgumentException e)
			{
				
			}
		}
		finally
		{
			if (cin != null)
			{
				cin.close();
			}

			if (contconn != null)
			{
				contconn.close();
			}

			if (baos != null)
			{
				baos.close();
			}
		}

		return null;
	}

	public static String time(Date date)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hours = cal.get(11);
		int minutes = cal.get(12);
		String time = TextLocal.inst.formatTime(hours, minutes);
		return time;
	}

	public static String fullDate(Date date)
	{

		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int hour = cal.get(Calendar.HOUR);
		int minutes = cal.get(Calendar.MINUTE);
		return TextLocal.inst.formatFullDate(day, month, year, hour, minutes);
	}
}