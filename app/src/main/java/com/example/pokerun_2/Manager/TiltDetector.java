package com.example.pokerun_2.Manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.pokerun_2.callbacks.StepCallback;

public class TiltDetector {
    private StepCallback stepCallback;
    private SensorManager sensorManager;
    private Sensor sensor;

    private long timestamp = 0;

    private SensorEventListener sensorEventListener;

    public TiltDetector(Context context, StepCallback _stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = _stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                calculateStep(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x, float y) {
        if (System.currentTimeMillis() - timestamp > 150) {
            timestamp = System.currentTimeMillis();
            if (x < -3.0) {
                if (stepCallback != null)
                    stepCallback.tiltRight();
            }
            if (x > 3.0) {
                if (stepCallback != null)
                    stepCallback.tiltLeft();
            }
            if(y >= 0.0 && y < 2.0){
                if (stepCallback != null)
                    stepCallback.speedSlow();
            }
            if(y >= 4.0 && y < 7.0){
                if (stepCallback != null)
                    stepCallback.speedFast();
            }
            if(y >= 8.0){
                if (stepCallback != null)
                    stepCallback.speedVeryFast();
            }
        }
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
