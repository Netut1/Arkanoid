package com.netut.arkanoid.engine.interfaces;

import android.view.MotionEvent;

import com.netut.arkanoid.engine.GameEngine;

public class InputHandler {
    private final GameEngine engine;

    public InputHandler(GameEngine engine) {
        this.engine = engine;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                engine.launchBalls();
            case MotionEvent.ACTION_MOVE:
                engine.movePaddle(event.getX());
                break;
        }
        return true;
    }
}