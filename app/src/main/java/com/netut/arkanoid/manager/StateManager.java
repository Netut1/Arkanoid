package com.netut.arkanoid.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.engine.GameEngine;
import com.netut.arkanoid.game.GameState;

public class StateManager {
    private static final String PREFS_NAME = "ArkanoidPrefs";
    private static final String KEY_LEVEL = "saved_level";
    private static final String KEY_SCORE = "saved_score";
    private static final String KEY_LIVES = "saved_lives";

    public static void saveToPreferences(Context context, GameState state) {
        if (state.getLives() > 0) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit()
                    .putInt(KEY_LEVEL, state.getCurrentLevel())
                    .putInt(KEY_SCORE, state.getScore())
                    .putInt(KEY_LIVES, state.getLives())
                    .apply();
        }
    }

    public static boolean restoreFromPreferences(Context context, GameState state, GameEngine engine, int screenWidth) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (prefs.contains(KEY_LEVEL)) {
            int level = prefs.getInt(KEY_LEVEL, 1);
            int score = prefs.getInt(KEY_SCORE, 0);
            int lives = prefs.getInt(KEY_LIVES, GameConstants.INITIAL_LIVES);

            state.setScore(score);
            state.setLives(lives);
            state.setCurrentLevel(level);
            engine.loadCurrentLevel();
            engine.resetPositions();
            return true;
        }
        return false;
    }

    public static void saveToBundle(Bundle outState, GameState state, GameEngine engine) {
        outState.putInt("score", state.getScore());
        outState.putInt("lives", state.getLives());
        outState.putInt("level", state.getCurrentLevel());
        outState.putFloat("paddleX", engine.getPaddle().x);
        outState.putBoolean("ballLaunched", engine.isBallLaunched());
        outState.putLong("ballResetTime", engine.getBallResetTime());
    }

    public static void restoreFromBundle(Bundle bundle, GameState state, GameEngine engine, int screenWidth) {
        int score = bundle.getInt("score", 0);
        int lives = bundle.getInt("lives", GameConstants.INITIAL_LIVES);
        int level = bundle.getInt("level", 1);
        float paddleX = bundle.getFloat("paddleX", screenWidth / 2f - (float) GameConstants.PADDLE_WIDTH / 2);
        boolean launched = bundle.getBoolean("ballLaunched", false);
        long resetTime = bundle.getLong("ballResetTime", 0);

        state.setScore(score);
        state.setLives(lives);
        state.setCurrentLevel(level);

        engine.restoreState(paddleX, launched, resetTime);
    }
}