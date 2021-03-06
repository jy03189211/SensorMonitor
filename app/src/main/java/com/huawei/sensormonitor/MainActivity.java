package com.huawei.sensormonitor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_HR=100;
    private final static int WINDOW_SIZE=5;
    private static final String TAG = "SensorMonitor";
    private static List<Float> window;
    private SensorManager mSensorManager;
    private Sensor mHRmeter;


    private TextView mHeartRateAttr;
    private TextView mHeartRateName;
    private float heartRateValue;
    private boolean mIsRunning;

    static {
        System.loadLibrary("optimal_filtering-jni");
    }
    private native float floatFromJNI(float heartrate, float[] arr);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIsRunning=true;
        mSensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null){
            Log.d(TAG,"HR sensor detected !");
            window=new ArrayList<Float>();
        } else {
            Log.d(TAG,"NO HR sensor ！");
        }
        //Check sensor permission
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.BODY_SENSORS);

        if (permissionCheck!=PackageManager.PERMISSION_GRANTED){
            Log.d(TAG,"Permission denied");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BODY_SENSORS},
                    MY_PERMISSIONS_REQUEST_READ_HR);
        }
        mHRmeter = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        mHeartRateAttr = findViewById(R.id.heart_value);
        mHeartRateName = findViewById(R.id.heart_key);

        // Enables Always-on
        setAmbientEnabled();

    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        mHeartRateAttr.setTextColor(Color.WHITE);
        mHeartRateAttr.getPaint().setAntiAlias(false);
        mHeartRateName.setTextColor(Color.WHITE);
        mHeartRateName.getPaint().setAntiAlias(false);
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        mHeartRateAttr.setTextColor(Color.GREEN);
        mHeartRateAttr.getPaint().setAntiAlias(true);
        mHeartRateName.setTextColor(Color.GREEN);
        mHeartRateName.getPaint().setAntiAlias(true);
    }

    //TODO: 1. permission request other than hear rate
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_HR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Permission granted");

                } else {
                    Log.d(TAG,"Permission denied");
                    heartRateValue=0;
                    mHeartRateAttr.setText(Float.toString(heartRateValue));
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        if(mIsRunning==false){

            mIsRunning=true;
        }
        mHeartRateAttr.setTextColor(Color.GREEN);
        mHeartRateName.setTextColor(Color.GREEN);
        mHeartRateAttr.setText("Loading ...");
        mHeartRateAttr.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        super.onResume();
        mSensorManager.registerListener(this, mHRmeter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        if(mIsRunning==true){

            mIsRunning=false;
        }
        mHeartRateAttr.setTextColor(Color.RED);
        mHeartRateAttr.setText("Paused, clicked again to resume.");
        mHeartRateAttr.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        mHeartRateAttr.setGravity(Gravity.CENTER_HORIZONTAL);
        mHeartRateName.setTextColor(Color.RED);

        super.onPause();

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float currentState;

        heartRateValue=(sensorEvent.values.length>0?sensorEvent.values[0]:0.0f);

        //window stepping
        window.add(heartRateValue);
        if(window.size()>WINDOW_SIZE){
            window=window.subList(Math.max(window.size() - WINDOW_SIZE, 0), window.size());
            //Log.d(TAG,"windows list:"+window);
        }

        //Log.d(TAG,"Java array:"+ Arrays.toString(convertFloatPrim(window)));
        currentState=floatFromJNI(heartRateValue,convertFloatPrim(window));
        mHeartRateAttr.setText(Float.toString(currentState));
        //Log.d(TAG, "Java:\nheart rate value:"+heartRateValue+"\n"+"accuracy:"+sensorEvent.accuracy+"\n"+"timestamp:"+sensorEvent.timestamp);
        Log.d(TAG, "Java raw value:       "+heartRateValue);
        Log.d(TAG, "Native smoothed value:"+currentState);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_STEM_1) {
                if(mIsRunning==true){
                    onPause();
                }else {
                    onResume();
                }

                // Do stuff
                Log.d(TAG, "KEYCODE_STEM_11111 clicked ");
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_STEM_2) {
                Log.d(TAG, "KEYCODE_STEM_22222 clicked");
                // Do stuff
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_STEM_3) {
                Log.d(TAG, "KEYCODE_STEM_33333 clicked");
                // Do stuff
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public static float[] convertFloatPrim(List<Float> floats)
    {
        float[] ret = new float[floats.size()];
        Iterator<Float> iterator = floats.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().floatValue();
        }
        return ret;
    }

}
