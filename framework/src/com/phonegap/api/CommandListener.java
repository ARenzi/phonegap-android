package com.phonegap.api;

import org.json.JSONArray;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.webkit.WebView;

public interface CommandListener extends SensorEventListener,Command{

	void setCommandManager(CommandManager commandManager);
	
}
