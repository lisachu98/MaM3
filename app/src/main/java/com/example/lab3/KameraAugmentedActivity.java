package com.example.lab3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class KameraAugmentedActivity extends Activity implements SensorEventListener{
    private RelativeLayout rl;
    private SensorManager sm;
    private MojeView myCameraOverlay;
    private Preview myCameraView;
    private Sensor gravity;
    private Sensor acceleration;
    private float[] gravityArr = null;
    private float[] accelerationArr = null;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private float[] deviceVector = new float[3];
    private float[] cameraVector = {0, 0, -1};
    private Location molo = new Location(LocationManager.GPS_PROVIDER);
    private Location gg = new Location(LocationManager.GPS_PROVIDER);
    private Location myLocation = new Location(LocationManager.GPS_PROVIDER);
    private LocationManager locationManager;
    private float[] bearingTos= new float[2];
    private float[] kierunekMolo = new float[3];
    private float[] kierunekGG = new float[3];
    private float[] angles = new float[2];

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = (RelativeLayout)findViewById(R.id.relativeLayout1);

        myCameraView = new Preview(this);
        rl.addView(myCameraView);

        myCameraOverlay = new MojeView(this);
        rl.addView(myCameraOverlay);


        sm = (SensorManager)getSystemService(SENSOR_SERVICE);

        gravity = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        acceleration = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        molo.setLatitude(54.44842053031101);
        molo.setLongitude(18.576806645129672);

        gg.setLatitude(54.37156851894329);
        gg.setLongitude(18.619136097257716);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


    }


    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this, gravity);
        sm.unregisterListener(this, acceleration);
        //Toast.makeText(this, "KameraAugmentedActivity.onPause()\nunregisterListener", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, gravity, sm.SENSOR_DELAY_GAME);
        sm.registerListener(this, acceleration, sm.SENSOR_DELAY_GAME);
        //Toast.makeText(this, "KameraAugmentedActivity.onResume()\nregisterListener", Toast.LENGTH_LONG).show();
    }


    //Nadpisane z SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSensorChanged(SensorEvent event) {switch (event.sensor.getType()){
        case Sensor.TYPE_MAGNETIC_FIELD:
            gravityArr = event.values.clone();
            break;
        case Sensor.TYPE_ACCELEROMETER:
            accelerationArr = event.values.clone();
            break;
    }
        if (gravityArr != null && accelerationArr != null){
            SensorManager.getRotationMatrix(rotationMatrix, null, accelerationArr, gravityArr);
            SensorManager.getOrientation(rotationMatrix, orientation);

            float degrees = (float)(Math.toDegrees(orientation[0]) + 360) % 360;

            deviceVector[0] = rotationMatrix[0]*cameraVector[0] + rotationMatrix[1]*cameraVector[1] + rotationMatrix[2]*cameraVector[2];
            deviceVector[1] = rotationMatrix[3]*cameraVector[0] + rotationMatrix[4]*cameraVector[1] + rotationMatrix[5]*cameraVector[2];
            deviceVector[2] = rotationMatrix[6]*cameraVector[0] + rotationMatrix[7]*cameraVector[1] + rotationMatrix[8]*cameraVector[2];

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            bearingTos[0] = myLocation.bearingTo(molo);
            bearingTos[1] = myLocation.bearingTo(gg);

            kierunekMolo[0] = (float)Math.sin(bearingTos[0]);
            kierunekMolo[1] = (float)Math.cos(bearingTos[0]);
            kierunekMolo[2] = 0;

            kierunekGG[0] = (float)Math.sin(bearingTos[1]);
            kierunekGG[1] = (float)Math.cos(bearingTos[1]);
            kierunekGG[2] = 0;

            angles[0] = (float)(Math.toDegrees(getAngle(kierunekMolo, deviceVector)) + 360) % 360;
            angles[1] = (float)(Math.toDegrees(getAngle(kierunekGG,deviceVector)) + 360) % 360;

            myCameraOverlay.setDane(new float[]{degrees, deviceVector[0], deviceVector[1], deviceVector[2], bearingTos[0], bearingTos[1], kierunekMolo[0], kierunekMolo[1], kierunekMolo[2], kierunekGG[0], kierunekGG[1], kierunekGG[2], angles[0], angles[1]});
            myCameraOverlay.invalidate();
        }
    }
    private float dotProduct(float[] a, float[] b)
    {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }
    private float vectorMagnitude(float[] vec)
    {
        return (float) Math.sqrt(vec[0]*vec[0] + vec[1]*vec[1] + vec[2]*vec[2]);
    }
    private float getAngle(float[] a, float b[])
    {
        return (float) Math.acos(
                dotProduct(a, b) / (vectorMagnitude(a) * vectorMagnitude(b))
        );
    }

}