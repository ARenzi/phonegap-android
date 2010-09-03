package com.phonegap.plugins;

/*
 * @author : Alberto Renzi, email: alberto.renzi@gmail.com
 */

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.phonegap.api.Command;
import com.phonegap.api.CommandListener;
import com.phonegap.api.CommandManager;
import com.phonegap.api.CommandResult;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;
import android.webkit.WebView;

public class AmbientLight implements CommandListener 
{
	private static final String WATCH = "watch";
	private static final String STOPWATCHING = "stopWatching";
	
	private WebView mAppView;
	private Context mCtx;
			
	Sensor mSensor;		
	int mTime = 10000;	//Default delay value millisecond
	boolean started = false;
	
	private SensorManager sensorManager;
	
	private long lastUpdate = -1;
	private String callbackId;
	private CommandManager mCm;	//This is set by commandManager with a new instance
								//with this reference i can activate other plugin 

public AmbientLight()
{
	super();		
}


public void watch(int freq)
{
	this.mTime=freq;
	sensorManager = (SensorManager) mCtx.getSystemService(Context.SENSOR_SERVICE);
	Log.d("AmbientLight","watch chiamata" );
	List<Sensor> list = this.sensorManager.getSensorList(Sensor.TYPE_LIGHT);
	if (list.size() > 0)
	{
		this.mSensor = list.get(0);
		this.sensorManager.registerListener(this, this.mSensor, SensorManager.SENSOR_DELAY_FASTEST);
	}		
}

public void stop(String key)
{
	if(started)
		sensorManager.unregisterListener(this);
}




/**
 * Executes the request and returns CommandResult.
 * 
 * @param action The command to execute.
 * @param args JSONArry of arguments for the command.
 * @return A CommandResult object with a status and message.
 */
public CommandResult execute(String action, JSONArray args){
	CommandResult cr = null;
	
	if (action.equalsIgnoreCase(WATCH))
	{
		try {					
			this.watch(Integer.parseInt(args.getJSONObject(0).getString("freq")));						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			cr = new CommandResult(CommandResult.Status.JSONEXCEPTION, "Errore");
			return cr;
		}
	}
	else if (action.equalsIgnoreCase(STOPWATCHING))
	{	
		try {
			this.stop(args.getJSONObject(1).getString("key"));
		} catch (JSONException e) {
			cr = new CommandResult(CommandResult.Status.JSONEXCEPTION, "Errore");
			return cr;
		}		
	}
	else {
		//For now nothing
		}	
	return cr= new CommandResult(CommandResult.Status.OKLISTENER, "0");
}


/**
 * Sets the context of the Command. This can then be used to do things like
 * get file paths associated with the Activity.
 * 
 * @param ctx The context of the main Activity.
 */
public void setContext(Context ctx) {
	// TODO Auto-generated method stub
	mCtx = ctx;	
}

/**
 * Sets the main View of the application, this is the WebView within which 
 * a PhoneGap app runs.
 * 
 * @param webView The PhoneGap WebView
 */
public void setView(WebView webView) {
	// TODO Auto-generated method stub
	mAppView = webView;	
}


public void onSensorChanged(SensorEvent event) {
	// TODO Auto-generated method stub
	if (event.sensor.getType() != Sensor.TYPE_LIGHT)
		return;
	long curTime = System.currentTimeMillis();
	if (lastUpdate == -1 || (curTime - lastUpdate) > mTime) {		
		lastUpdate = curTime;
			
		Log.d("AmbientLight plugin. New value =", event.values[0] +"");
		float x = event.values[0];
		
		CommandResult cr = new CommandResult(CommandResult.Status.OKLISTENER, ""+x);
		mCm.dispatch(cr, callbackId);					
	}	
}


public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	
}





public void setCallBackId(String callbackId) {
	// TODO Auto-generated method stub
	this.callbackId=callbackId;
}





public void setCommandManager(CommandManager commandManager) {
	// TODO Auto-generated method stub
	this.mCm=commandManager;
}
}