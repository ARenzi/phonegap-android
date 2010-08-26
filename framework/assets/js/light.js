//Light Sensor
//Author: Alberto Renzi 
//alberto.renzi@gmail.com

function LightSensorChange(x)
{
  this.luce = x;
  this.timestamp = new Date().getTime();
}

var lightListener = [];

/**
 * This class provides access to device LightSensor data.
 * @constructor
 */
function LightSensor() {
	/**
	 * The last known Light.
	 */
	this.lastLight = null;
}

/**
 * Asynchronously aquires the current Light.
 * @param {Function} successCallback The function to call when the light
 * data is available
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the light data.
 * @param {LightSensorOptions} options The options for getting the Light data
 * such as timeout.
 */
LightSensor.prototype.getCurrentLight = function(successCallback, errorCallback, options) {
	// If the Light is available then call success
	// If the Light is not available then call error
	// Created for iPhone, Iphone passes back _light obj litteral
	if (typeof successCallback == "function") {
		if(this.lastLight)
			{
				//alert("Chiamata la succcess callback di getCurrentLight");
				successCallback(lightvar);
			}
		else
		{
			//alert("E' la prima volta e chiamo la watchLight di PhoneGap");
			watchLight(this.gotCurrentLight, this.fail);
		}
	}
}


LightSensor.prototype.getCurrentLight = function(key, x)
{
    var l = new LightSensorChange(x);
    l.luce = x;
    //alert("Valore Luce" + x);
    l.win = lightListener[key].win;
    l.fail = lightListener[key].fail;
    this.timestamp = new Date().getTime();
    this.lastLight = l;
    lightListener[key] = l;
    if (typeof l.win == "function") {
      l.win(l);
    }
}


/**
 * Asynchronously aquires the light repeatedly at a given interval.
 * @param {Function} successCallback The function to call each time the acceleration
 * data is available
 * @param {Function} errorCallback The function to call when there is an error 
 * getting the acceleration data.
 * @param {AccelerationOptions} options The options for getting the accelerometer data
 * such as timeout.
 */

LightSensor.prototype.watchLight = function(successCallback, errorCallback, options) {
	
	// TODO: add the interval id to a list so we can clear all watches
  var frequency = (options != undefined)? options.frequency : 10000;
  var lightvar = new LightSensorChange(0);
  lightvar.win = successCallback;
  lightvar.fail = errorCallback;
  lightvar.opts = options;
  var key = lightListener.push( lightvar ) - 1;
  Light.start(frequency, key);
}

/**
 * Clears the specified accelerometer watch.
 * @param {String} watchId The ID of the watch returned from #watchLight.
 */
LightSensor.prototype.clearWatch = function(watchId) {

	Light.stop(watchId);
}

LightSensor.prototype.epicFail = function(watchId, message) {
  sensorWatcher[key].fail();
}

PhoneGap.addConstructor(function() {
    if (typeof navigator.light == "undefined") navigator.light = new LightSensor();
});

// End LightSensor
