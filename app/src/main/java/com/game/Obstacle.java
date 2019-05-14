package com.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Obstacle {

    private Bitmap bitmap;

    private int x;
    private int y;

    private int speed;

    private int maxX;
    private int minX;

    private int maxY;

    private int halfBitmapHeight;
    private int quarterBitmapHeight;

    private Rect detectCollision;

    Obstacle(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.przeszkoda);

        halfBitmapHeight = (bitmap.getHeight() / 2);
        quarterBitmapHeight = (bitmap.getHeight() / 4);

        maxX = screenX;
        maxY = screenY;
        minX = 0;

        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;

        // przeszkody mogą w 1/4 zakryte przez górę lub dół ekranu
        y = generator.nextInt(maxY - halfBitmapHeight) - quarterBitmapHeight;

        detectCollision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    int update() {
        int addToScore = 0;
        x -= speed;

        if (x < minX - bitmap.getWidth()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;

            // przeszkody mogą w 1/4 zakryte przez górę lub dół ekranu
            y = generator.nextInt(maxY - halfBitmapHeight) - quarterBitmapHeight;
            addToScore = 1;
        }

        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
        detectCollision.left = x;
        return addToScore;
    }

    Bitmap getBitmap() {
        return bitmap;
    }

    int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    int getY() {
        return y;
    }

    Rect getDetectCollision() {
        return detectCollision;
    }

}
