package com.phonegap;

import java.util.HashMap;

import android.content.Context;
import android.webkit.WebView;

public class LightBroker {
	private WebView mAppView;
private Context mCtx;
private HashMap<String, LightSensorListener> lightListeners;

public LightBroker(WebView view, Context ctx)
{
	mCtx = ctx;
	mAppView = view;
	lightListeners = new HashMap<String, LightSensorListener>();
}

public String start(int freq, String key)
{
	LightSensorListener listener = new LightSensorListener(key, freq, mCtx, mAppView);
	listener.start(freq);
	lightListeners.put(key, listener);
	return key;
}

public void stop(String key)
{
	LightSensorListener acc = lightListeners.get(key);
	acc.stop();
}
}