package com.game;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean lightSensorExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TextView textTitle = findViewById(R.id.textTitle);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null) {
            textTitle.setText("Brak czujnika światła!");
            textTitle.setTextColor(Color.RED);
            lightSensorExists = false;
        } else {
            lightSensorExists = true;
            Options.maxLux = lightSensor.getMaximumRange();
        }
    }

    public void button_options(View v) {
        if (lightSensorExists) {
            startActivity(new Intent(MainActivity.this, OptionsView.class));
        } else {
            Toast.makeText(MainActivity.this, "No Light Sensor!", Toast.LENGTH_SHORT).show();
        }
    }

    public void button_game(View v) {
        if (lightSensorExists) {
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "No Light Sensor!", Toast.LENGTH_SHORT).show();
        }
    }
}
