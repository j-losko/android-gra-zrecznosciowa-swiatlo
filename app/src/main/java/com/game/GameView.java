package com.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;
    private Thread gameThread = null;

    private Player player;
    private Obstacle[] obstacles;

    private int obstacleCount = 3;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private float currentLuxReading = 0;

    private int currentScore;
    private boolean isGameOver;

    private SharedPreferences sharedPreferences;
    private int highScore;

    GameView(Context context, int screenX, int screenY, SensorManager sensorManager) {
        super(context);

        player = new Player(context, screenY);
        surfaceHolder = getHolder();
        paint = new Paint();

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(lightSensorEventListener,
                lightSensor,
                SensorManager.SENSOR_DELAY_GAME); //SensorManager.SENSOR_DELAY_UI);

        obstacles = new Obstacle[obstacleCount];
        for (int i = 0; i < obstacleCount; i++) {
            obstacles[i] = new Obstacle(context, screenX, screenY);
        }

        currentScore = 0;
        isGameOver = false;

        sharedPreferences = context.getSharedPreferences("highScore", Context.MODE_PRIVATE);
    }

    SensorEventListener lightSensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                currentLuxReading = event.values[0];
            }
        }
    };

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update(currentLuxReading);

        for (int i = 0; i < obstacleCount; i++) {

            currentScore += obstacles[i].update();

            if (Rect.intersects(player.getDetectCollision(), obstacles[i].getDetectCollision())) {
                isGameOver = true;
                playing = false;

                highScore = sharedPreferences.getInt("highScore", 0);
                if (currentScore > highScore) {
                    SharedPreferences.Editor e = sharedPreferences.edit();
                    e.putInt("highScore", currentScore);
                    e.apply();
                }
            }
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {

            canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.WHITE); // background

            paint.setColor(Color.BLACK);
            paint.setTextSize(30);

            paint.setTextSize(30);
            canvas.drawText("Score:" + currentScore, 100, 50, paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            for (int i = 0; i < obstacleCount; i++) {
                canvas.drawBitmap(
                        obstacles[i].getBitmap(),
                        obstacles[i].getX(),
                        obstacles[i].getY(),
                        paint
                );
            }

            if (isGameOver) {
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos = (int) ((canvas.getHeight() / 3) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);

                paint.setTextSize(100);
                yPos = (int) ((canvas.getHeight() / 1.5) - ((paint.descent() + paint.ascent()) / 2));
                String gameOverText = "Your score: " + currentScore + "   High score: " + highScore;
                canvas.drawText(gameOverText, canvas.getWidth() / 2, yPos, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}
