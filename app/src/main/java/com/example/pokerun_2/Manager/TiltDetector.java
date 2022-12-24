package com.example.pokerun_2.Manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.pokerun_2.StepCallback;

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

                calculateStep(x);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x) {
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
