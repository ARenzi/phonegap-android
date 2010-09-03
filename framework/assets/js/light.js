//Ambient Light Sensor

/**
 * This class provides an object constructor watcher to listen a data from a java listener 
 * @constructor
 */
function AmbientLightBase(x) {
	/**
	 * The last known data.
	 */
	this.data = x;
	this.timestamp = new Date().getTime();
}

PhoneGap.addConstructor(function() {
    if (typeof PhoneGap.plugins.AmbientLight == "undefined") PhoneGap.plugins.AmbientLight = new AmbientLightBase();
});


// End LightSensor