package com.example.arkanoid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PowerUp {
    public float x, y;
    public float radius = GameConstants.POWERUP_RADIUS;
    public float speedY = GameConstants.POWERUP_SPEED;
    public PowerUpType type;

    public PowerUp(float x, float y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        y += speedY;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(getColor());
        canvas.drawCircle(x, y, radius, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(type.getSymbol(), x, y + 10, paint);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    public int getColor() {
        switch (type) {
            case EXPAND_PADDLE: return Color.GREEN;
            case STICKY: return Color.rgb(255, 185, 0);
            case TRIPLE: return Color.MAGENTA;
            case DOUBLE: return Color.CYAN;
            case EXTRA_LIFE: return Color.rgb(255, 215, 0);
            case SLOW: return Color.BLUE;
            case FAST: return Color.RED;
            case MINUS_LIFE: return Color.DKGRAY;
            case SHRINK_PADDLE: return Color.rgb(139, 69, 19);
            default: return Color.WHITE;
        }
    }

    public PowerUpType getType() {
        return type;
    }
}