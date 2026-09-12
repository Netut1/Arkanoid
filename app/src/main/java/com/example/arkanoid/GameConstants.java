package com.example.arkanoid;

import android.graphics.Color;

public final class GameConstants {
    private GameConstants() {}

    // Размеры игровых объектов
    public static final int PADDLE_WIDTH = 250;
    public static final int PADDLE_HEIGHT = 40;
    public static final int BALL_RADIUS = 28;
    public static final float BALL_SPEED = 10;

    // Параметры сетки блоков
    public static final int BRICK_COLS = 20;
    public static final int BRICK_HEIGHT = 38;

    // Отступы
    public static final int TOP_OFFSET = 70;
    public static final int PADDLE_Y_OFFSET = 120;

    // Игровые параметры
    public static final int INITIAL_LIVES = 3;
    public static final int TARGET_FPS = 60;
    public static final long RESET_DELAY_MS = 500;
    public static final float PADDLE_HIT_FACTOR = 1.5f;
    public static final float MIN_HORIZONTAL_SPEED_FACTOR = 0.3f;

    // Параметры бонусов
    public static final float POWERUP_DROP_CHANCE = 0.2f;
    public static final int POWERUP_RADIUS = 25;
    public static final float POWERUP_SPEED = 5;
    public static final long EFFECT_DURATION = 10000;
    public static final int PADDLE_EXPAND_AMOUNT = 100;
    public static final int PADDLE_SHRINK_AMOUNT = 50;
    public static final float SLOW_FACTOR = 0.5f;
    public static final float FAST_FACTOR = 1.5f;

    // Цвета
    public static final int PADDLE_COLOR = Color.WHITE;
    public static final int BALL_COLOR = Color.YELLOW;
    public static final int TOUGH_BRICK_COLOR = Color.LTGRAY;
    public static final int TOUGH_BRICK_DAMAGED_COLOR = Color.DKGRAY;
    public static final int INDESTRUCTIBLE_BRICK_COLOR = Color.rgb(30, 30, 30);
    public static final int BRICK_STROKE_COLOR = Color.BLACK;
    public static final int BRICK_STROKE_WIDTH = 2;

    // Цвета обычных кирпичей
    public static final int[] NORMAL_BRICK_COLORS = {
            Color.RED, Color.BLUE, Color.rgb(255, 165, 0), Color.YELLOW, Color.GREEN, Color.MAGENTA
    };

    // UI
    public static final int ANIMATION_DURATION = 5000;
    public static final int DURATION_BLOCK_ANIMATION = 2500;
    public static final double PREDEFINED_PROBABILITY = 0.5;
}