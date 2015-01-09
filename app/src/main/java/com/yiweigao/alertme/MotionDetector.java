package com.yiweigao.alertme;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.util.Log;

/**
 * Created by yiweigao on 1/8/15.
 */
public class MotionDetector implements SensorEventListener {

//    private Activity activity;

    private SensorManager sensorManager;
    private Sensor accSensor;
//    protected Sensor gyroSensor;

//    private float[] accValues;
//    private float[] gyroValues;

    private boolean hasAcc = false;
//    private boolean hasGyro = false;

    private float previousAccel;

    public MotionDetector(Context context) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        gyroSensor= sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (accSensor != null) {
            hasAcc = true;
        }
//        if (gyroSensor != null) {
//            hasGyro = true;
//        }



    }

    public void register() {
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregister() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
//            Toast.makeText(this, "Accelerometer has detected movement!", Toast.LENGTH_SHORT).show();

//            accValues = event.values.clone();


            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

//            Log.d("onSensonChanged()", "\n" + Float.toString(x) + "\t" + Float.toString(y) + "\t" + Float.toString(z));

            float currentAccel = FloatMath.sqrt(x * x + y * y + z * z);
//            mAccel = (mAccel * 0.9f) + (mAccelCurrent * 0.1f);
            Log.d("onSensorChanged", Float.toString(currentAccel));

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
