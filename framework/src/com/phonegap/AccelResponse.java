package com.phonegap;

public class AccelResponse implements Response {

	String key;
	float x;
	float y; 
	float z;
	
	
	public AccelResponse(String mKey, float mx, float my, float mz) {
		key = mKey;
		x = mx;
		y = my;
		z = mz;
	}

	public String getJson() {
		String json = "{ \"type\" : \"accel\", \"key\" : \"" + key + "\",";
		json += " \"x\" : " + Float.toString(x) + ",";
		json += " \"y\" : " + Float.toString(y) + ",";
		json += " \"z\" : " + Float.toString(z) + "}";
		return json;
	}

}
