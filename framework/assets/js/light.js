//Ambient Light Sensor

/**
 * This class provides an object constructor watcher to listen a data from a java listener 
 * @constructor
 */
function Watcher(x) {
	/**
	 * The last known data.
	 */
	this.data = x;
	this.timestamp = new Date().getTime();
}


if (typeof navigator.system == "undefined") navigator.system =  function(){};


//Sample vector for listeners. It can be used for all plugin that have something to observe
var watcherListener= [];

//I've watched on http://www.w3.org/TR/2010/WD-system-info-api-20100202/#system-properties
navigator.system.watch = function(clazz, success, args) {	
	var watchervar = new Watcher(0);
	var mkey = watcherListener.push( watchervar ) - 1;
	args[1]= {key:mkey};
		
	watchervar.win = success;	
	
	var sucPhoneGapCommand = function(a){				
	};
	var failPhoneGapCommand = function(){ 
		alert("PhoneGap command fail");
	};

	var callbackId = clazz + PhoneGap.callbackId++;
	PhoneGap.callbacks[callbackId] = {success:sucPhoneGapCommand, fail:failPhoneGapCommand};
	return CommandManager.exec("com.phonegap.plugins."+clazz, "watch", callbackId, JSON.stringify(args), true);
};



Watcher.prototype.recallWathcers = function(key, data)
{        	
	var l = watcherListener[key];
	l.data = data;
	l.timestamp = new Date().getTime();	
	if (typeof l.win == "function") {		
		l.win(l.data);
	}
}

Watcher.prototype.epicFail = function(key, message) {
  sensorWatcher[key].fail();
}


PhoneGap.addConstructor(function() {
    if (typeof navigator.system.AmbientLight == "undefined") navigator.system.AmbientLight = new Watcher();
});

// End LightSensor