package com.phonegap.api;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.webkit.WebView;

import com.phonegap.DroidGap;

/**
 * CommandManager is exposed to JavaScript in the PhoneGap WebView.
 * 
 * Calling native plugin code can be done by calling CommandManager.exec(...)
 * from JavaScript.
 * 
 * @author davejohnson
 *
 */
public final class CommandManager {
	private static final String EXCEPTION_PREFIX = "[PhoneGap] *ERROR* Exception executing command [";
	private static final String EXCEPTION_SUFFIX = "]: ";
	
	private static final int ISNOTACOMMAND = -1;
	private static final int ISCOMMAND = 1;
	private static final int ISCOMMANDLISTENER = 2;
	
	private Command[] commands;
	
	private final Context ctx;
	private final WebView app;
	
	public CommandManager(WebView app, Context ctx) {
		this.ctx = ctx;
		this.app = app;
	}

	
	public boolean dispatch(CommandResult cr, String callbackId)
	{
		if ((cr.getStatus() == 0)||(cr.getStatus() == 8)) {
			app.loadUrl(cr.toSuccessCallbackString(callbackId));
		} else {
			app.loadUrl(cr.toErrorCallbackString(callbackId));
		}	
		return true;
	}
	
	
	/**
	 * Receives a request for execution and fulfills it by finding the appropriate
	 * Java class and calling it's execute method.
	 * 
	 * CommandManager.exec can be used either synchronously or async. In either case, a JSON encoded 
	 * string is returned that will indicate if any errors have occurred when trying to find
	 * or execute the class denoted by the clazz argument.
	 * 
	 * @param clazz String containing the fully qualified class name. e.g. com.phonegap.FooBar
	 * @param action String containt the action that the class is supposed to perform. This is
	 * passed to the plugin execute method and it is up to the plugin developer 
	 * how to deal with it.
	 * @param callbackId String containing the id of the callback that is execute in JavaScript if
	 * this is an async plugin call.
	 * @param args An Array literal string containing any arguments needed in the
	 * plugin execute method.
	 * @param async Boolean indicating whether the calling JavaScript code is expecting an
	 * immediate return value. If true, either PhoneGap.callbackSuccess(...) or PhoneGap.callbackError(...)
	 * is called once the plugin code has executed.
	 * @return JSON encoded string with a response message and status.
	 */
	public String exec(final String clazz, final String action, final String callbackId, 
			final String jsonArgs, final boolean async) {
		CommandResult cr = null;
		try {
			final JSONArray args = new JSONArray(jsonArgs);
			Class c = getClassByName(clazz);
			if (isPhoneGapCommand(c)==ISCOMMAND) {
				// Create a new instance of the plugin and set the context and webview				
				final Command plugin = (Command) c.newInstance();				
				plugin.setContext(this.ctx);
				plugin.setView(this.app);
				plugin.setCallBackId(callbackId);
				if (async) {
					// Run this on a different thread so that this one can return back to JS
					Thread thread = new Thread(new Runnable() {
						public void run() {
							// Call execute on the plugin so that it can do it's thing
							CommandResult cr = plugin.execute(action, args);
							// Check the status for 0 (success) or otherwise
							if (cr.getStatus() == 0) {
								app.loadUrl(cr.toSuccessCallbackString(callbackId));
							} else {
								app.loadUrl(cr.toErrorCallbackString(callbackId));
							}							
						}
					});
					thread.start();
					return "";
				} else {
					// Call execute on the plugin so that it can do it's thing
					cr = plugin.execute(action, args);
				}
			}
			else if (isPhoneGapCommand(c)==ISCOMMANDLISTENER) {
				// Create a new instance of the plugin and set the context and webview				
				final CommandListener plugin = (CommandListener) c.newInstance();				
				plugin.setContext(this.ctx);
				plugin.setView(this.app);
				plugin.setCallBackId(callbackId);
				plugin.setCommandManager(this);
				
				// Run this on a different thread so that this one can return back to JS
				Thread thread = new Thread(new Runnable() {
					public void run() {
						// Call execute on the plugin so that it can do it's thing
						CommandResult cr = plugin.execute(action, args);
						// Check the status for 0 (success) or otherwise
						if (cr.getStatus() == 8) {
							app.loadUrl(cr.toSuccessCallbackString(callbackId));
						} else {
							app.loadUrl(cr.toErrorCallbackString(callbackId));
						}							
					}
				});
				thread.start();
				return "";				
			}
		} catch (ClassNotFoundException e) {
			cr = new CommandResult(CommandResult.Status.CLASSNOTFOUNDEXCEPTION, 
					"{ message: 'ClassNotFoundException', status: "+CommandResult.Status.CLASSNOTFOUNDEXCEPTION.ordinal()+" }");
		} catch (IllegalAccessException e) {
			cr = new CommandResult(CommandResult.Status.ILLEGALACCESSEXCEPTION, 
					"{ message: 'IllegalAccessException', status: "+CommandResult.Status.ILLEGALACCESSEXCEPTION.ordinal()+" }");
		} catch (InstantiationException e) {
			cr = new CommandResult(CommandResult.Status.INSTANTIATIONEXCEPTION, 
					"{ message: 'InstantiationException', status: "+CommandResult.Status.INSTANTIATIONEXCEPTION.ordinal()+" }");
		} catch (JSONException e) {
			cr = new CommandResult(CommandResult.Status.JSONEXCEPTION, 
					"{ message: 'JSONException', status: "+CommandResult.Status.JSONEXCEPTION.ordinal()+" }");
		}
		// if async we have already returned at this point unless there was an error...
		if (async) {
			app.loadUrl(cr.toErrorCallbackString(callbackId));
		}
		return ( cr != null ? cr.getResult() : "{ status: 0, message: 'all good' }" );
	}
	
	/**
	 * 
	 * 
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Class getClassByName(final String clazz) throws ClassNotFoundException {
		return Class.forName(clazz);
	}

	/**
	 * Get the interfaces that a class implements and see if it implements the
	 * com.phonegap.api.Command interface.
	 * 
	 * @param c The class to check the interfaces of.
	 * @return Boolean indicating if the class implements com.phonegap.api.Command
	 */
	private int isPhoneGapCommand(Class c) {
		
		Class[] interfaces = c.getInterfaces();
		for (int j=0; j<interfaces.length; j++) {
			if (interfaces[j].getName().equals("com.phonegap.api.Command")) 
			{
				return ISCOMMAND;
				
			}
			else if (interfaces[j].getName().equals("com.phonegap.api.CommandListener"))
			{
				return ISCOMMANDLISTENER;				
			}
			
		}
		
		return ISNOTACOMMAND;
	}
}