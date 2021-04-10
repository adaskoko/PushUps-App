package com.example.pushups;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TrainingActivity extends AppCompatActivity {

    TextView counterTV;
    TextView startStopTV;
    SensorManager mySensorManager;
    Sensor myProximitySensor;
    Counter counter;
    EditText typeET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        counterTV = findViewById(R.id.progressTV);
        startStopTV = findViewById(R.id.startStopTV);
        typeET = findViewById(R.id.typeET);
        createProximitySensorListener();
        counter = new Counter(counterTV, typeET, this.getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        counter.close();
    }

    public void createProximitySensorListener(){
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        SensorEventListener proximitySensorEventListener
                = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    if (event.values[0] == 0) {
                        counter.sensorNear();
                    } else {
                        counter.sensorAway();
                    }
                }
            }
        };
        mySensorManager.registerListener(proximitySensorEventListener,
                myProximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void startCounter(View view) {
        if(startStopTV.getText().toString().equals("Start")){
            startStopTV.setText("Stop");
            counter.start();
        }else{
            startStopTV.setText("Start");
            counter.stop();
        }
    }

    public void close(View view) {
        finish();
    }
}