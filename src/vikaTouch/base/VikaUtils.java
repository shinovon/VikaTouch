package vikaTouch.base;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connection;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.rms.RecordStore;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import vikaTouch.VikaTouch;
import vikaTouch.newbase.URLBuilder;


public final class VikaUtils {
	
	public static String millisecondsToTime(final long paramLong)
	{
	    final long l1 = paramLong / 1000000L / 60L;
	    final long l2 = paramLong / 1000000L % 60L;
	    final String str1 = Long.toString(l2);
	    final String str2;
	    if (str1.length() >= 2) {
	      str2 = str1.substring(0, 2);
	    } else {
	      str2 = "0" + str1;
	    }
	    return l1 + ":" + str2;
	}

	public static boolean check()
	{
		String url = new URLBuilder("execute")
				.addField("code", "return \"ok\";")
				.toString();
		HttpConnection var1 = null;
		try {
			var1 = (HttpConnection) Connector.open(url);
		
			if(var1.getResponseCode() == 200)
			{
				return true;
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static String download(URLBuilder url)
	{
		return download(url.toString());
	}
	
	public static String download(String url)
	{
		HttpConnection var1 = null;
		InputStream var2 = null;
		InputStreamReader var3 = null;
		String var4 = null;

		try {
			var1 = (HttpConnection) Connector.open(url);
			var1.setRequestMethod("GET");
			var1.setRequestProperty("User-Agent", "KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
			var2 = var1.openInputStream();
			var3 = new InputStreamReader(var2, "UTF-8"); 
			StringBuffer var14 = new StringBuffer();
			char[] var5;
			int var6;
			if (var1.getResponseCode() != 200 && var1.getResponseCode() != 401) {
				if(var1.getHeaderField("Location") != null)
				{
					(var1 = (HttpConnection) Connector.open(var1.getHeaderField("Location"))).setRequestMethod("GET");
					var1.setRequestProperty("User-Agent",
							"KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
					var2 = var1.openInputStream();
					var3 = new InputStreamReader(var2, "UTF-16");
					var14 = new StringBuffer();
					if (var1.getResponseCode() == 200 || var1.getResponseCode() == 401) {
						var5 = new char[262144];
	
						while ((var6 = var3.read(var5, 0, 262144)) != -1) {
							var14.append(var5, 0, var6);
						}
	
					}
				}
			} else {
				var5 = new char[262144];

				while ((var6 = var3.read(var5, 0, 262144)) != -1) {
					var14.append(var5, 0, var6);
				}

			}

			var4 = replace(var14.toString(), "<br>", " ");
		} catch (UnsupportedEncodingException var11) {
		} catch (IOException var12) {
		} catch (NullPointerException var13) {
			;
		}

		try {
			var3.close();
		} catch (Exception var10) {
			;
		}

		try {
			var2.close();
		} catch (Exception var9) {
			;
		}

		try {
			var1.close();
		} catch (Exception var8) {
			;
		}

		try {
			if (VikaTouch.API != "https://api.vk.com:443") {
				var4 = replace(
						replace(replace(replace(var4, "http://cs", "https://cs"), "http:\\/\\/cs", "https://cs"),
								"https://vk-api", "http://vk-api"),
						"https:\\/\\/vk-api", "http://vk-api");
			}
		} catch (NullPointerException var7) {
			;
		}

		return var4;
	}
	/*
	public static void makereq(String var0) {
		HttpConnection var1 = null;
		InputStream var2 = null;
		InputStreamReader var3 = null;

		try {
			var1 = (HttpConnection) Connector.open(replace(replace(replace(replace(replace(replace(replace(
					replace(replace(replace(replace(var0, "+", "%2B"), "<", "%3C"), ">", "%3E"), " ", "%20"), "[",
					"%5B"), "]", "%5D"), "|", "%7C"), "{", "%7B"), "}", "%7D"), "\"", "%22"), ",", "%2C"));
			var1.setRequestMethod("GET");
			var1.setRequestProperty("User-Agent", "KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
			var2 = var1.openInputStream();
			var3 = new InputStreamReader(var2);
			StringBuffer var12 = new StringBuffer();
			if (var1.getResponseCode() == 200 || var1.getResponseCode() == 401) {
				char[] var4 = new char[10];
				int var5 = var3.read(var4, 0, 10);
				var12.append(var4, 0, var5);
			}

		} catch (NullPointerException var9) {
		} catch (UnsupportedEncodingException var10) {
		} catch (IOException var11) {
		}

		try {
			var3.close();
		} catch (Exception var8) {
			;
		}

		try {
			var2.close();
		} catch (Exception var7) {
			;
		}

		try {
			var1.close();
		} catch (Exception var6) {
			;
		}
	}
	*/
	public static String sendPostRequest(String var0, String var1) {
		HttpConnection var2 = null;
		DataInputStream var3 = null;
		DataOutputStream var4 = null;
		String var5 = "";

		try {
			(var2 = (HttpConnection) Connector.open(var0, 3)).setRequestMethod("POST");
			var4 = var2.openDataOutputStream();
			byte[] var19 = var1.getBytes();

			int var20;
			for (var20 = 0; var20 < var19.length; ++var20) {
				var4.writeByte(var19[var20]);
			}

			for (var3 = new DataInputStream(var2.openInputStream()); (var20 = var3.read()) != -1; var5 = var5
					+ (char) var20) {
				;
			}
		} catch (IOException var17) {
			var5 = "ERROR";
		} finally {
			try {
				if (var2 != null) {
					var2.close();
				}
			} catch (IOException var16) {
				;
			}

			try {
				if (var3 != null) {
					var3.close();
				}
			} catch (IOException var15) {
				;
			}

			try {
				if (var4 != null) {
					var4.close();
				}
			} catch (IOException var14) {
				;
			}

		}

		return var5;
	}

	/*
	public static String download2(String var0) {
		String var1 = "";
		HttpConnection var2 = null;
		InputStream var3 = null;
		boolean var39 = false;

		label427 : {
			label428 : {
				label429 : {
					label430 : {
						label431 : {
							label432 : {
								label433 : {
									try {
										var39 = true;
										(var2 = (HttpConnection) Connector.open(var0, 3, true)).setRequestMethod("GET");
										var2.setRequestProperty("User-Agent",
												"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
										var2.setRequestProperty("Connection", "close");
										var2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
										var2.setRequestProperty("Accept", "text/plain");
										if (var2.getResponseCode() == 200) {
											var3 = var2.openInputStream();
											byte[] var4 = new byte[4096];

											int var74;
											do {
												if ((var74 = var3.read(var4)) > 0) {
													var1 = var1 + new String(var4, 0, var74);
												}
											} while (var74 > 0);

											var39 = false;
											break label428;
										}

										var0 = "404";
										var39 = false;
										break label432;
									} catch (ConnectionNotFoundException var67) {
										var67.getMessage();
										var0 = "404";
										var39 = false;
									} catch (ClassCastException var68) {
										var68.getMessage();
										var0 = "404";
										var39 = false;
										break label429;
									} catch (IOException var69) {
										var69.getMessage();
										var0 = "404";
										var39 = false;
										break label433;
									} catch (SecurityException var70) {
										var39 = false;
										break label427;
									} catch (NullPointerException var71) {
										var39 = false;
										break label430;
									} catch (Exception var72) {
										var72.getMessage();
										var0 = "404";
										var39 = false;
										break label431;
									} finally {
										if (var39) {
											try {
												
											} catch (Exception var60) {
												;
											}

											try {
												var3.close();
											} catch (Exception var59) {
												;
											}

											try {
												var2.close();
											} catch (Exception var58) {
												;
											}

										}
									}

									try {
										
									} catch (Exception var54) {
										;
									}

									try {
										var3.close();
									} catch (Exception var53) {
										;
									}

									try {
										var2.close();
									} catch (Exception var52) {
										;
									}

									return var0;
								}

								try {
									
								} catch (Exception var45) {
									;
								}

								try {
									var3.close();
								} catch (Exception var44) {
									;
								}

								try {
									var2.close();
								} catch (Exception var43) {
									;
								}

								return var0;
							}

							try {
								
							} catch (Exception var66) {
								;
							}

							try {
								
							} catch (Exception var65) {
								;
							}

							try {
								var2.close();
							} catch (Exception var64) {
								;
							}

							return var0;
						}

						try {
							
						} catch (Exception var51) {
							;
						}

						try {
							var3.close();
						} catch (Exception var50) {
							;
						}

						try {
							var2.close();
						} catch (Exception var49) {
							;
						}

						return var0;
					}

					var0 = "404";

					try {
						
					} catch (Exception var42) {
						;
					}

					try {
						var3.close();
					} catch (Exception var41) {
						;
					}

					try {
						var2.close();
					} catch (Exception var40) {
						;
					}

					return var0;
				}

				try {
					
				} catch (Exception var57) {
					;
				}

				try {
					var3.close();
				} catch (Exception var56) {
					;
				}

				try {
					var2.close();
				} catch (Exception var55) {
					;
				}

				return var0;
			}
			try {
				
			} catch (Exception var63) {
				;
			}

			try {
				var3.close();
			} catch (Exception var62) {
				;
			}

			try {
				var2.close();
			} catch (Exception var61) {
				;
			}

			var1 = replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
					replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
							replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
									replace(replace(replace(replace(replace(replace(replace(replace(replace(
											replace(replace(replace(replace(replace(replace(replace(replace(
													replace(replace(replace(replace(replace(replace(replace(
															replace(replace(replace(replace(replace(replace(
																	replace(replace(replace(
																			replace(replace(replace(replace(
																					replace(replace(replace(replace(
																							replace(replace(replace(
																									var1, "<br>", "\n"),
																									"%D0%B0", "а"),
																									"%D0%B1", "б"),
																							"%D0%B2", "в"), "%D0%B3",
																							"г"), "%D0%B4", "д"),
																							"%D0%B5", "е"),
																					"%D0%B6", "ж"), "%D0%B7", "з"),
																					"%D0%B8", "и"), "%D0%B9", "й"),
																			"%D0%Ba", "к"), "%D0%Bb", "л"), "%D0%Bc",
																			"м"),
																	"%D0%Bd", "н"), "%D0%Be", "о"), "%D0%Bf", "п"),
																	"%D1%80", "р"), "%D1%81", "с"), "%D1%82", "т"),
															"%D1%83", "у"), "%D1%84", "ф"), "%D1%85", "х"), "%D1%86",
															"ц"), "%D1%87", "ч"), "%D1%88", "ш"), "%D1%89", "щ"),
													"%D1%8a", "ъ"), "%D1%8b", "ы"), "%D1%8c", "ь"), "%D1%8d", "э"),
													"%D1%8e", "ю"), "%D1%8f", "я"), "%D0%90", "А"), "%D0%91", "Б"),
											"%D0%92", "В"), "%D0%93", "Г"), "%D0%94", "Д"), "%D0%95", "Е"), "%D0%96",
											"Ж"), "%D0%97", "З"), "%D0%98", "И"), "%D0%99", "Й"), "%D0%9a", "K"),
									"%D0%9b", "Л"), "%D0%9c", "М"), "%D0%9d", "Н"), "%D0%9e", "О"), "%D0%9f", "П"),
									"%D0%a0", "Р"), "%D0%a1", "С"), "%D0%a2", "Т"), "%D0%a3", "У"), "%D0%a4", "Ф"),
							"%D0%a5", "Х"), "%D0%a6", "Ц"), "%D0%a7", "Ч"), "%D0%a8", "Ш"), "%D0%a9", "Щ"), "%D0%aa",
							"Ъ"), "%D0%ab", "Ы"), "%D0%ac", "Ь"), "%D0%ad", "­"), "%D0%ae", "Ю"), "%D0%af", "Я"),
					"%D1%91", "ё"), "%D0%81", "Ё"), "%E3%83%84", "=)"), "%F0%9F%98%8A", ":-)"), "%F0%9F%98%82", "s01"),
					"%F0%9F%98%84", "s02"), "%F0%9F%98%81", "s03"), "%F0%9F%98%83", "s04"), "%F0%9F%98%86", "s05"),
					"%F0%9F%98%AD", "s06");
			if (vikaMobile.vkApi != "https://api.vk.com:443") {
				var1 = replace(var1, "https:", "http:");
			}

			return var1;
		}

		var0 = "404";

		try {
			
		} catch (Exception var48) {
			;
		}

		try {
			var3.close();
		} catch (Exception var47) {
			;
		}

		try {
			var2.close();
		} catch (Exception var46) {
			;
		}

		return var0;
	}
	*/

	public static String strToHex(String var0) {
		char[] var4 = var0.toCharArray();
		StringBuffer var1 = new StringBuffer();

		for (int var2 = 0; var2 < var4.length; ++var2) {
			char var3 = var4[var2];
			var1.append(Integer.toHexString(var3).toUpperCase());
		}

		return var1.toString();
	}
	/*
	public static String method57(String var0) {
		String var1 = "";
		HttpConnection var2 = null;
		InputStream var3 = null;
		boolean var17 = false;

		label165 : {
			try {
				var17 = true;
				if (var0.indexOf(".php") <= 0) {
					var0 = replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
							replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
									replace(replace(replace(replace(replace(replace(replace(replace(replace(
											replace(replace(replace(replace(replace(replace(replace(replace(
													replace(replace(replace(replace(replace(replace(replace(
															replace(replace(replace(replace(replace(replace(
																	replace(replace(replace(replace(replace(
																			replace(replace(replace(replace(
																					replace(replace(replace(replace(
																							replace(replace(replace(
																									replace(var0,
																											"<br>",
																											"\n"),
																									"%D0%B0", "а"),
																									"%D0%B1", "б"),
																									"%D0%B2", "в"),
																							"%D0%B3", "г"), "%D0%B4",
																							"д"), "%D0%B5", "е"),
																							"%D0%B6", "ж"),
																					"%D0%B7", "з"), "%D0%B8", "и"),
																					"%D0%B9", "й"), "%D0%Ba", "к"),
																			"%D0%Bb", "л"), "%D0%Bc", "м"), "%D0%Bd",
																			"н"), "%D0%Be", "о"), "%D0%Bf", "п"),
																	"%D1%80", "р"), "%D1%81", "с"), "%D1%82", "т"),
																	"%D1%83", "у"), "%D1%84", "ф"), "%D1%85", "х"),
															"%D1%86", "ц"), "%D1%87", "ч"), "%D1%88", "ш"), "%D1%89",
															"щ"), "%D1%8a", "ъ"), "%D1%8b", "ы"), "%D1%8c", "ь"),
													"%D1%8d", "э"), "%D1%8e", "ю"), "%D1%8f", "я"), "%D0%90", "А"),
													"%D0%91", "Б"), "%D0%92", "В"), "%D0%93", "Г"), "%D0%94", "Д"),
											"%D0%95", "Е"), "%D0%96", "Ж"), "%D0%97", "З"), "%D0%98", "И"), "%D0%99",
											"Й"), "%D0%9a", "K"), "%D0%9b", "Л"), "%D0%9c", "М"), "%D0%9d", "Н"),
									"%D0%9e", "О"), "%D0%9f", "П"), "%D0%a0", "Р"), "%D0%a1", "С"), "%D0%a2", "Т"),
									"%D0%a3", "У"), "%D0%a4", "Ф"), "%D0%a5", "Х"), "%D0%a6", "Ц"), "%D0%a7", "Ч"),
							"%D0%a8", "Ш"), "%D0%a9", "Щ"), "%D0%aa", "Ъ"), "%D0%ab", "Ы"), "%D0%ac", "Ь"), "%D0%ad",
							"­"), "%D0%ae", "Ю"), "%D0%af", "Я"), "%D1%91", "ё"), "%D0%81", "Ё");
				}

				String var4;
				if (var0.indexOf("php?photoadddr") > 0) {
					var4 = var0.substring(0, var0.indexOf("php?photoadddr") + 14);
					var0 = var0.substring(var0.indexOf("php?photoadddr") + 14);
				} else {
					var4 = var0.substring(0, var0.indexOf("php?") + 4);
					var0 = var0.substring(var0.indexOf("php?") + 4);
				}

				var0 = strToHex(var0);
				(var2 = (HttpConnection) Connector.open(var4 + var0, 3, true)).setRequestMethod("GET");
				var2.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1");
				var2.setRequestProperty("Connection", "close");
				var2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				var2.setRequestProperty("Accept", "text/plain");
				if (var2.getResponseCode() == 200) {
					var3 = var2.openInputStream();
					byte[] var31 = new byte[4096];

					int var30;
					do {
						if ((var30 = var3.read(var31)) > 0) {
							var1 = var1 + new String(var31, 0, var30);
						}
					} while (var30 > 0);

					var17 = false;
				} else {
					var1 = String.valueOf(var2.getResponseCode());
					var17 = false;
				}
				break label165;
			} catch (ConnectionNotFoundException var24) {
				var1 = var24.getMessage();
				var17 = false;
				break label165;
			} catch (ClassCastException var25) {
				var1 = var25.getMessage();
				var17 = false;
				break label165;
			} catch (IOException var26) {
				var1 = var26.getMessage();
				var17 = false;
				break label165;
			} catch (SecurityException var27) {
				var17 = false;
			} catch (Exception var28) {
				var1 = var28.getMessage();
				var17 = false;
				break label165;
			} finally {
				if (var17) {
					try {
						
					} catch (Exception var20) {
						;
					}

					try {
						var3.close();
					} catch (Exception var19) {
						;
					}

					try {
						var2.close();
					} catch (Exception var18) {
						;
					}

				}
			}

			var1 = "Сетевой запрос отменен пользователем!";
		}

		try {
			
		} catch (Exception var23) {
			;
		}

		try {
			var3.close();
		} catch (Exception var22) {
			;
		}

		try {
			var2.close();
		} catch (Exception var21) {
			;
		}

		return replace(replace(replace(replace(replace(replace(replace(replace(
				replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(
						replace(replace(replace(replace(replace(replace(replace(replace(replace(
								replace(replace(replace(replace(replace(replace(replace(
										replace(replace(replace(replace(replace(replace(
												replace(replace(replace(replace(replace(replace(replace(replace(
														replace(replace(replace(replace(replace(replace(
																replace(replace(replace(replace(replace(
																		replace(replace(replace(replace(replace(
																				replace(replace(replace(replace(
																						replace(replace(replace(
																								replace(replace(
																										replace(var1,
																												"<br>",
																												"\n"),
																										"%D0%B0", "а"),
																										"%D0%B1", "б"),
																								"%D0%B2", "в"),
																								"%D0%B3", "г"),
																								"%D0%B4", "д"),
																						"%D0%B5", "е"), "%D0%B6", "ж"),
																						"%D0%B7", "з"), "%D0%B8", "и"),
																				"%D0%B9", "й"), "%D0%Ba", "к"),
																				"%D0%Bb", "л"), "%D0%Bc", "м"),
																				"%D0%Bd", "н"),
																		"%D0%Be", "о"), "%D0%Bf", "п"), "%D1%80", "р"),
																		"%D1%81", "с"), "%D1%82", "т"),
																"%D1%83", "у"), "%D1%84", "ф"), "%D1%85", "х"),
																"%D1%86", "ц"), "%D1%87", "ч"), "%D1%88", "ш"),
														"%D1%89", "щ"), "%D1%8a", "ъ"), "%D1%8b", "ы"), "%D1%8c", "ь"),
														"%D1%8d", "э"), "%D1%8e", "ю"), "%D1%8f", "я"), "%D0%90", "А"),
												"%D0%91", "Б"), "%D0%92", "В"), "%D0%93", "Г"), "%D0%94", "Д"),
												"%D0%95", "Е"), "%D0%96", "Ж"),
										"%D0%97", "З"), "%D0%98", ""), "%D0%99", "Й"), "%D0%9a", "K"), "%D0%9b", "Л"),
										"%D0%9c", "М"), "%D0%9d", "Н"),
								"%D0%9e", "О"), "%D0%9f", "П"), "%D0%a0", "Р"), "%D0%a1", "С"), "%D0%a2", "Т"),
								"%D0%a3", "У"), "%D0%a4", "Ф"), "%D0%a5", "Х"), "%D0%a6", "Ц"),
						"%D0%a7", "Ч"), "%D0%a8", "Ш"), "%D0%a9", "Щ"), "%D0%aa", "Ъ"), "%D0%ab", "Ы"), "%D0%ac", "Ь"),
						"%D0%ad", "­"), "%D0%ae", "Ю"), "%D0%af", "Я"), "%D1%91", "ё"), "%D0%81", "Ё"),
				"%E3%83%84", "=)"), "%F0%9F%98%8A", ":-)"), "%F0%9F%98%82", "s01"), "%F0%9F%98%84", "s02"),
				"%F0%9F%98%81", "s03"), "%F0%9F%98%83", "s04"), "%F0%9F%98%86", "s05"), "%F0%9F%98%AD", "s06");
	}
	*/
	public static String replace(String str, String from, String to) {
		final StringBuffer var3 = new StringBuffer();
		int var4 = str.indexOf(from);
		int var5 = 0;

		for (int var6 = from.length(); var4 != -1; var4 = str.indexOf(from, var5)) {
			var3.append(str.substring(var5, var4)).append(to);
			var5 = var4 + var6;
		}

		var3.append(str.substring(var5, str.length()));
		return var3.toString();
	}
	
	
	public static boolean startsWith(String str, String need)
	{
		int l = need.length();
		return str.substring(0,l).equalsIgnoreCase(need);
	}
	
	public static Image createThumbnail(Image var0, int var1, int var2) {
		int var3 = var0.getWidth();
		int var4 = var0.getHeight();
		if (var2 == -1) {
			var2 = var1 * var4 / var3;
		}

		Image var5;
		Graphics var6 = (var5 = Image.createImage(var1, var2)).getGraphics();

		for (int var7 = 0; var7 < var2; ++var7) {
			for (int var8 = 0; var8 < var1; ++var8) {
				var6.setClip(var8, var7, 1, 1);
				int var9 = var8 * var3 / var1;
				int var10 = var7 * var4 / var2;
				var6.drawImage(var0, var8 - var9, var7 - var10, 20);
			}
		}

		return Image.createImage(var5);
	}

	public static Image resizeIcon(Image var0) {
		return (VikaTouch.mobilePlatform = System.getProperty("microedition.platform")).indexOf("5.5") <= 0
				&& VikaTouch.mobilePlatform.indexOf("5.4") <= 0 && VikaTouch.mobilePlatform.indexOf("5.3") <= 0
				&& VikaTouch.mobilePlatform.indexOf("5.2") <= 0 && VikaTouch.mobilePlatform.indexOf("5.1") <= 0
				&& VikaTouch.mobilePlatform.indexOf("5.0") <= 0 && VikaTouch.mobilePlatform.indexOf("3.2") <= 0
				&& VikaTouch.mobilePlatform.indexOf("5700") <= 0 && VikaTouch.mobilePlatform.indexOf("6110") <= 0
				&& VikaTouch.mobilePlatform.indexOf("6120") <= 0 && VikaTouch.mobilePlatform.indexOf("6121") <= 0
				&& VikaTouch.mobilePlatform.indexOf("NM705i") <= 0 && VikaTouch.mobilePlatform.indexOf("6122") <= 0
				&& VikaTouch.mobilePlatform.indexOf("6124") <= 0 && VikaTouch.mobilePlatform.indexOf("NM706i") <= 0
				&& VikaTouch.mobilePlatform.indexOf("6290") <= 0 && VikaTouch.mobilePlatform.indexOf("E51") <= 0
				&& VikaTouch.mobilePlatform.indexOf("E63") <= 0 && VikaTouch.mobilePlatform.indexOf("E66") <= 0
				&& VikaTouch.mobilePlatform.indexOf("E71") <= 0 && VikaTouch.mobilePlatform.indexOf("E90") <= 0
				&& VikaTouch.mobilePlatform.indexOf("N76") <= 0 && VikaTouch.mobilePlatform.indexOf("N81") <= 0
				&& VikaTouch.mobilePlatform.indexOf("N82") <= 0 && VikaTouch.mobilePlatform.indexOf("N95") <= 0
						? createThumbnail(var0, 25, 25)
						: var0;
	}

	public static Image downloadImage(String url) throws IOException {
		VikaTouch.mobilePlatform = System.getProperty("microedition.platform");
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
			
			
			
			// ЧТЕНИЕ
			try {
					final Image image = ImageStorage.get(filename);
					if(image != null)
					{
						return image;
					}
				
			} catch(Exception e){}
		}

		ByteArrayOutputStream var1 = null;
		final Connection con = Connector.open(url);
		if(con instanceof HttpConnection)
		{
			HttpConnection var2 = (HttpConnection) con; 
			var2.setRequestMethod("GET");
			var2.setRequestProperty("User-Agent", "KateMobileAndroid/51.1 lite-442 (Symbian; SDK 17; x86; Nokia; ru)");
			if (var2.getResponseCode() != 200 && var2.getResponseCode() != 401) {
				url = var2.getHeaderField("Location");
			} else {
			}
			
			var2.close();
		}
		else if(con instanceof FileConnection)
		{
			caching = false;
			con.close();

			FileConnection var10;
			DataInputStream var3 = (var10 = (FileConnection) Connector.open(url)).openDataInputStream();
			final boolean a = true;
			try {
				int var8;
				byte[] var9;
				{
					if(a)
					{
					var8 = (int)var10.fileSize();
					var9 = new byte[var8];
					var3.readFully(var9);
					}
					else
					{
					var1 = new ByteArrayOutputStream();

					while ((var8 = var3.read()) != -1) {
						var1.write(var8);
					}
					
					var9 = var1.toByteArray();
					
					var1.close();
					}
				}
				try {
					final Image var11 = Image.createImage(var9, 0, var9.length);
					return var11 == null ? null : var11;
				} catch (IllegalArgumentException var6) {
					;
				}
			} finally {
				if (var3 != null) {
					var3.close();
				}

				if (var10 != null) {
					var10.close();
				}

				if (var1 != null) {
					var1.close();
				}

			}
		}
		ContentConnection var10;
		DataInputStream var3 = (var10 = (ContentConnection) Connector.open(url)).openDataInputStream();

		
		FileConnection fc2 = null;
		OutputStream os = null;
		try {
			int var8;
			byte[] var9;
			if ((var8 = (int) var10.getLength()) != -1) {
				var9 = new byte[var8];
				var3.readFully(var9);
			} else {
				var1 = new ByteArrayOutputStream();

				while ((var8 = var3.read()) != -1) {
					var1.write(var8);
				}

				var9 = var1.toByteArray();
				var1.close();
			}
			try {
				Image var11 = Image.createImage(var9, 0, var9.length);
				if(var11 != null && caching)
					ImageStorage.save(filename, var11);
				return var11 == null ? null : var11;
			} catch (IllegalArgumentException var6) {
				;
			}
		} finally {
			if (var3 != null) {
				var3.close();
			}

			if (var10 != null) {
				var10.close();
			}

			if (var1 != null) {
				var1.close();
			}
			if(caching)
			{
				if (os != null) {
					os.close();
				}
				if(fc2 != null)
				{
					fc2.close();
				}
			}

		}

		return null;
	}

	public static String time(Date var0) {
		Calendar var1;
		(var1 = Calendar.getInstance()).setTime(var0);
		var1.get(5);
		var1.get(2);
		int var2 = var1.get(11);
		int var4 = var1.get(12);
		String var3 = var2 + ":";
		if (var4 < 10) {
			var3 = var3 + "0";
			var3 = var3 + var4;
		} else {
			var3 = var3 + var4;
		}

		return var3;
	}

	public static String month(Date var0) {
		Calendar var1;
		(var1 = Calendar.getInstance()).setTime(var0);
		int var5 = var1.get(5);
		int var2 = var1.get(2);
		String var8 = (new String[]{"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа",
				"сентября", "октября", "ноября", "декабря"})[var2];
		int var3 = var1.get(1);
		int var4 = var1.get(11);
		int var7 = var1.get(12);
		String var6 = var5 + " " + var8 + " " + var3 + " г.в " + var4 + ":";
		if (var7 < 10) {
			var6 = var6 + "0";
			var6 = var6 + var7;
		} else {
			var6 = var6 + var7;
		}

		return var6;
	}
	/*


	protected static List selectPhoto(String var0) {
		
		List var1 = new List("Выбрать фото", 3);

		try {
			FileConnection var2;
			String var4;
			for (Enumeration var3 = (var2 = (FileConnection) Connector.open(var0, 1)).list("*", true); var3
					.hasMoreElements(); vikaMobile.aVector67.addElement(var4)) {
				var4 = (String) var3.nextElement();
				long var5;
				if ((var2 = (FileConnection) Connector.open(var0 + var4, 1)).isDirectory()) {
					var5 = var2.directorySize(false);
					var1.append(var4 + " - " + Integer.toString((int) (var5 / 1024L)) + "кб\n", vikaMobile.imgPath);
					vikaMobile.aVector100.addElement("directory");
				} else {
					var5 = var2.fileSize();
					var1.append(var4 + " - " + Integer.toString((int) (var5 / 1024L)) + "кб\n", vikaMobile.imgImage);
					vikaMobile.aVector100.addElement(var2.getURL());
				}
			}

			var2.close();
		} catch (IOException var7) {
		} catch (SecurityException var8) {
		}

		return var1;
		return null;
	}
*/
}