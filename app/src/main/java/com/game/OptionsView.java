package com.game;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsView extends AppCompatActivity {
    private EditText editTextMinLux;
    private EditText editTextMaxLux;
    private TextView textView_currentLux;
    private Button button_detectThresholds;

    private boolean detectingThresholds = false;
    private float detectMin = 32768;
    private float detectMax = 0;
    private float lightSensorMaximumRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        editTextMinLux = findViewById(R.id.editTextMinLux);
        editTextMaxLux = findViewById(R.id.editTextMaxLux);
        textView_currentLux = findViewById(R.id.textView_currentLux);
        button_detectThresholds = findViewById(R.id.button_detectThresholds);

        editTextMaxLux.setText(String.valueOf(Options.maxLux));
        editTextMinLux.setText(String.valueOf(Options.minLux));

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(lightSensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_GAME);

        lightSensorMaximumRange = lightSensor.getMaximumRange();
        detectMin = lightSensorMaximumRange;
    }

    SensorEventListener lightSensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                final float currentReading = event.values[0];
                textView_currentLux.setText(String.valueOf(currentReading));

                if (detectingThresholds) {
                    if (currentReading < detectMin) {
                        editTextMinLux.setText(String.valueOf(currentReading));
                        detectMin = currentReading;
                    }
                    if (currentReading > detectMax) {
                        editTextMaxLux.setText(String.valueOf(currentReading));
                        detectMax = currentReading;
                    }
                }
            }
        }
    };

    public void backToMenu(View v) {
        float tempMax = Float.parseFloat(editTextMaxLux.getText().toString());
        float tempMin = Float.parseFloat(editTextMinLux.getText().toString());

        if (tempMax <= tempMin) {
            Toast.makeText(this, "Minimum value has to be lower than the maximum value!", Toast.LENGTH_LONG).show();
        } else {
            Options.maxLux = tempMax;
            Options.minLux = tempMin;
            finish();
        }
    }

    public void detectThresholds(View v) {
        if (!detectingThresholds) {
            detectingThresholds = true;
            button_detectThresholds.setText("Stop detecting thresholds");
            editTextMinLux.setText(String.valueOf(detectMin));
            editTextMaxLux.setText(String.valueOf(detectMax));
            editTextMinLux.setEnabled(false);
            editTextMaxLux.setEnabled(false);
        } else {
            detectingThresholds = false;
            button_detectThresholds.setText("Automatically detect thresholds");
            detectMin = lightSensorMaximumRange;
            detectMax = 0;
            editTextMinLux.setEnabled(true);
            editTextMaxLux.setEnabled(true);
        }
    }
}
