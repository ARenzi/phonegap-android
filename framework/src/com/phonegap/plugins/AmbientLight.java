package com.phonegap.plugins;

/*
 * @author : Alberto Renzi, email: alberto.renzi@gmail.com
 */

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.phonegap.api.Command;
import com.phonegap.api.CommandResult;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

public class AmbientLight implements Command 
{
	private static final String WATCH = "watch";
	private static final String STOPWATCHING = "stopWatching";
	
	private WebView mAppView;
	private Context mCtx;
	private HashMap<String, AmbientLightListener> lightListeners;

public AmbientLight()
{
	super();
	lightListeners = new HashMap<String, AmbientLightListener>();
}


public AmbientLight(WebView view, Context ctx)
{
	mCtx = ctx;
	mAppView = view;
	lightListeners = new HashMap<String, AmbientLightListener>();
}


public String watch(int freq, String key)
{
	Log.d("AmbientLight","watch chiamata key: " + key );
	AmbientLightListener listener = new AmbientLightListener(key, freq, mCtx, mAppView);
	listener.watch(freq);
	lightListeners.put(key, listener);
	return key;
}

public void stop(String key)
{
	AmbientLightListener acc = lightListeners.get(key);
	acc.stop();
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
			this.watch(Integer.parseInt(args.getJSONObject(0).getString("freq")),args.getJSONObject(1).getString("key"));						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			cr = new CommandResult(CommandResult.Status.JSONEXCEPTION, "Errore");
			//e.printStackTrace();
			return cr;
		}
	}
	else if (action.equalsIgnoreCase(STOPWATCHING))
	{	
		try {
			this.stop(args.getJSONObject(1).getString("key"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	else {
		//For now nothing
		}	
	return cr= new CommandResult(CommandResult.Status.OK, "");
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
}