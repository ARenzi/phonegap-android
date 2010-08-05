package com.phonegap;

import java.util.HashMap;

import android.content.Context;
import android.webkit.WebView;

public class AccelBroker {
    private WebView mAppView;
	private Context mCtx;
	private HashMap<String, AccelListener> accelListeners;
	private CallbackServer mCallback;
	
	public AccelBroker(CallbackServer cServer, Context ctx)
	{
		mCtx = ctx;
		mCallback = cServer;
		accelListeners = new HashMap<String, AccelListener>();
	}
	
	public String start(int freq, String key)
	{
		AccelListener listener = new AccelListener(key, freq, mCtx, mCallback);
		listener.start(freq);
		accelListeners.put(key, listener);
		return key;
	}
	
	public void stop(String key)
	{
		AccelListener acc = accelListeners.get(key);
		acc.stop();
	}

}
