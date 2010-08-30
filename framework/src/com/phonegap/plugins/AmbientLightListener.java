package com.phonegap.plugins;

/*
 * @author : Alberto Renzi, email: alberto.renzi@gmail.com
 */

import java.util.List;
import android.util.Log;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.webkit.WebView;

public class AmbientLightListener 

	implements SensorEventListener{

		WebView mAppView;
		Context mCtx;
		String mKey;
		Sensor mSensor;
		int mTime = 10000;
		boolean started = false;
		
		private SensorManager sensorManager;
		
		private long lastUpdate = -1;
		
		public AmbientLightListener(String key, int freq, Context ctx, WebView appView)
		{
			Log.d("AmbientLight listener", " frequency =" + freq);
			mCtx = ctx;
			mAppView = appView;		
			mKey = key;
			mTime = freq;
			sensorManager = (SensorManager) mCtx.getSystemService(Context.SENSOR_SERVICE);		
		}
		
		public void watch(int time)
		{
			Log.d("Listener Luce: partito", time +"");
			mTime = time;
			
			List<Sensor> list = this.sensorManager.getSensorList(Sensor.TYPE_LIGHT);
			if (list.size() > 0)
			{
				this.mSensor = list.get(0);
				this.sensorManager.registerListener(this, this.mSensor, SensorManager.SENSOR_DELAY_FASTEST);
			}
			else
			{
				mAppView.loadUrl("javascript:navigator.system.AmbientLight.epicFail("+mKey+",'Failed to start')");
			}
		}
		
		public void stop()
		{
			if(started)
				sensorManager.unregisterListener(this);
		}
		
		

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(SensorEvent event) {		
			if (event.sensor.getType() != Sensor.TYPE_LIGHT)
				return;
			long curTime = System.currentTimeMillis();
			if (lastUpdate == -1 || (curTime - lastUpdate) > mTime) {		
				lastUpdate = curTime;
					
				Log.d("AmbientLight plugin. New value =", event.values[0] +"");
				float x = event.values[0];
				String value = "javascript:navigator.system.AmbientLight.recallWathcers('" +mKey + "'," + x +  ")";
				Log.d("script invocato","" + value);
				mAppView.loadUrl(value);
			}		
		}
	
}
