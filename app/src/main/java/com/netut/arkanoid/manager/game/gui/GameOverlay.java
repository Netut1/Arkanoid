package com.netut.arkanoid.manager.game.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.netut.arkanoid.GameActivity;
import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.engine.GameEngine;
import com.netut.arkanoid.engine.gui.GamePanel;
import com.example.arkanoid.R;

public class GameOverlay {
    private final Context context;
    private final int screenWidth;
    private final int screenHeight;
    private final GameEngine engine;
    private GamePanel gamePanel;
    private boolean showing = false;
    private boolean isGameOver = false;
    private int score, level;
    private long startTime;
    private String text;
    private int[] colors;
    private static final long DURATION = GameConstants.ANIMATION_DURATION;
    private static final long TEXT_ANIMATION = GameConstants.DURATION_BLOCK_ANIMATION;
    private float[] charX, charY;
    private int charCount;
    private float cellSize = 120f;
    private float spacing = 10f;

    public GameOverlay(Context context, int screenWidth, int screenHeight,
                       GameEngine engine, GamePanel gamePanel) {
        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.engine = engine;
        this.gamePanel = gamePanel;
    }

    public void showGameOver(int score, int level) {
        this.isGameOver = true;
        this.score = score;
        this.level = level;
        this.text = context.getString(R.string.game_over).toUpperCase();
        this.colors = new int[]{Color.LTGRAY, Color.DKGRAY};
        startAnimation();
    }

    public void showLevelComplete() {
        this.isGameOver = false;
        this.text = context.getString(R.string.level_completed).toUpperCase();
        this.colors = GameConstants.NORMAL_BRICK_COLORS;
        startAnimation();
    }

    private void startAnimation() {
        showing = true;
        startTime = System.currentTimeMillis();
        engine.getGameLoop().pause();
        calculatePositions();
    }

    private void calculatePositions() {
        charCount = text.length();
        charX = new float[charCount];
        charY = new float[charCount];

        float totalWidth = charCount * (cellSize + spacing) - spacing;
        float startX = (screenWidth - totalWidth) / 2;
        float y = screenHeight / 2f - cellSize / 2f;

        for (int i = 0; i < charCount; i++) {
            charX[i] = startX + i * (cellSize + spacing);
            charY[i] = y;
        }
    }

    public void draw(Canvas canvas) {
        if (!showing) return;

        canvas.drawColor(Color.argb(180, 0, 0, 0));

        long elapsed = System.currentTimeMillis() - startTime;
        float progress = Math.min(1f, (float) elapsed / (DURATION - TEXT_ANIMATION));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(GameConstants.BRICK_STROKE_WIDTH);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        for (int i = 0; i < charCount; i++) {
            int color = colors[i % colors.length];
            paint.setColor(color);

            RectF rect = new RectF(charX[i], charY[i],
                    charX[i] + cellSize, charY[i] + cellSize);

            if (progress < 1f) {
                float cx = rect.centerX();
                float cy = rect.centerY();
                float w = rect.width() * progress;
                float h = rect.height() * progress;
                rect.set(cx - w/2, cy - h/2, cx + w/2, cy + h/2);
            }

            canvas.drawRoundRect(rect, 20f, 20f, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rect, 20f, 20f, paint);
            paint.setStyle(Paint.Style.FILL);

            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            double brightness = 0.299 * r + 0.587 * g + 0.114 * b;
            paint.setColor(brightness > 128 ? Color.BLACK : Color.WHITE);

            float textSize = rect.height() * 0.7f;
            paint.setTextSize(textSize);

            float textX = rect.centerX();
            float textY = rect.centerY() - (paint.ascent() + paint.descent()) / 2f;

            canvas.drawText(String.valueOf(text.charAt(i)), textX, textY, paint);
        }

        if (elapsed >= DURATION) {
            finishAnimation();
        }
    }

    private void finishAnimation() {
        showing = false;
        if (isGameOver) {
            ((GameActivity) context).gameOver(score, level);
        } else {
            engine.proceedToNextLevel();
            engine.getGameLoop().resume();
        }
    }

    public boolean isShowing() {
        return showing;
    }
}