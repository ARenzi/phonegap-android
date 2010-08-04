package com.phonegap;

public class AccelResponse implements Response {

	String key;
	int x;
	int y; 
	int z;
	
	AccelResponse(String mkey, int mx, int my, int mz)
	{
		key = mkey;
		x = mx;
		y = my;
		z = mz;
	}
	
	public String getJson() {
		String json = "{ \"key\" : \"" + key + "\",";
		json += " \"x\" : " + Integer.toString(x) + ",";
		json += " \"y\" : " + Integer.toString(y) + ",";
		json += " \"z\" : " + Integer.toString(z) + "}";
		return json;
	}

}
