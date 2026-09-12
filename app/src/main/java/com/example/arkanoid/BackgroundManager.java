package com.example.arkanoid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BackgroundManager {
    private Bitmap backgroundBitmap;

    public void loadBackground(Resources res, int drawableId, int width, int height) {
        Bitmap original = BitmapFactory.decodeResource(res, drawableId);
        backgroundBitmap = Bitmap.createScaledBitmap(original, width, height, true);
        original.recycle();
    }

    public Bitmap getBackground() {
        return backgroundBitmap;
    }

    public void recycle() {
        if (backgroundBitmap != null) {
            backgroundBitmap.recycle();
            backgroundBitmap = null;
        }
    }
}