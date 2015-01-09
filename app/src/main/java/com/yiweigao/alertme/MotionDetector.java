package com.yiweigao.alertme;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.FloatMath;
import android.util.Log;

/**
 * Created by yiweigao on 1/8/15.
 */
public class MotionDetector implements SensorEventListener {

//    private Activity activity;

    private Context mainContext;

    private SensorManager sensorManager;
    private Sensor accSensor;

    final float ALPHA = 0.8f;

    private boolean isOn;
    private boolean isCalibrating;
    private boolean hasAcc;

    private float[] gravity;
    private float[] linear_acceleration;

    public MotionDetector(Context context) {
        this.mainContext = context;
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.isOn = false;
        this.isCalibrating = false;
        this.hasAcc = false;
        if (this.accSensor != null) {
            this.hasAcc = true;
        }

        this.gravity = new float[3];
        this.linear_acceleration = new float[3];
    }

    public void turnOn() {
        isOn = true;
    }

    public void turnOff() {
        isOn = false;
    }

    public void register() {
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    public void startCalibration() {
        isCalibrating = true;
    }

    public void stopCalibration() {
        isCalibrating = false;
    }

    private void calibrate(SensorEvent event) {
        // ALPHA is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate
        // default = 0.8f

        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

//        Log.d("calibration", Float.toString(gravity[0]) + " | " +
//                             Float.toString(gravity[1]) + " | " +
//                             Float.toString(gravity[2]));

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            if (isCalibrating) {
                calibrate(event);
                return;
            }

//            float[] linear_acceleration = new float[3];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

//            float currentAccel = FloatMath.sqrt(x * x + y * y + z * z);
            float currentAccel = FloatMath.sqrt(linear_acceleration[0] * linear_acceleration[0] +
                                                linear_acceleration[1] * linear_acceleration[1] +
                                                linear_acceleration[2] * linear_acceleration[2]);
            Log.d("->", Float.toString(currentAccel));

            if (isOn && currentAccel > 0.3f) {
                Intent intent = new Intent("motionDetected");
                intent.putExtra("hasMotion", true);
                LocalBroadcastManager.getInstance(mainContext).sendBroadcast(intent);
                sensorManager.unregisterListener(this);
                isOn = false;
            }
//            mAccel = (mAccel * 0.9f) + (mAccelCurrent * 0.1f);
//            Log.d("->", Float.toString(currentAccel) + " {" + Float.toString(x) +
//                    ", " + Float.toString(y) + ", " + Float.toString(z) + "}");

//            Log.d("->", " {" + Float.toString(linear_acceleration[0]) +
//                    ", " + Float.toString(linear_acceleration[1]) + ", " + Float.toString(linear_acceleration[2]) + "}");

//            if (currentAccel > ALPHA) {
//                Intent intent = new Intent("motionDetected");
//                intent.putExtra("hasMotion", "true");
//                LocalBroadcastManager.getInstance(mainContext).sendBroadcast(intent);
//                sensorManager.unregisterListener(this);
//            }

            // Shake detection
//            float x = accValues[0];
//            float y = accValues[1];
//            float z = accValues[2];
//            float mAccelCurrent = 0.0f;
//            float mAccelLast = mAccelCurrent;
//            mAccelCurrent = FloatMath.sqrt(x * x + y * y + z * z);
//            float delta = mAccelCurrent - mAccelLast;
//            float mAccel = 0.0f;
//            mAccel = mAccel * 1.0f + delta;
//            // Make this higher or lower according to how much
//            // motion you want to detect
//            // original = 0.9f
//
//            // can either change this or change the constant on line 79
//            // current settings seem okay
//            // original = 3
//            if (mAccel > 3) {
//                // do something
//                Log.d("Sensors", "Sensor change detected! " + Float.toString(mAccel));
//            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
