package com.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Player {

    private Bitmap bitmap;

    private int x;
    private int y;

    private float screenY;
    private float moveHowManyPixels;

    private Rect detectCollision;

    Player(Context context, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite);

        x = 250;

        this.screenY = screenY - bitmap.getHeight();
        moveHowManyPixels = this.screenY / (Options.maxLux - Options.minLux);
        y = screenY;

        detectCollision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    void update(float currentLuxReading) {
        float lux = currentLuxReading;

        if (lux < Options.minLux) {
            lux = Options.minLux;
        } else if (lux > Options.maxLux) {
            lux = Options.maxLux;
        }
        y = (int) (screenY - ((lux - Options.minLux) * moveHowManyPixels));

        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
        detectCollision.left = x;
    }

    Bitmap getBitmap() {
        return bitmap;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Rect getDetectCollision() {
        return detectCollision;
    }
}
