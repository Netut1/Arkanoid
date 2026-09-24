package com.netut.arkanoid.engine.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.netut.arkanoid.engine.GameEngine;
import com.netut.arkanoid.engine.GameLoop;
import com.example.arkanoid.R;
import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.engine.interfaces.GameEventListener;
import com.netut.arkanoid.engine.interfaces.InputHandler;
import com.netut.arkanoid.game.entity.ball.Ball;
import com.netut.arkanoid.game.entity.brick.Brick;
import com.netut.arkanoid.game.entity.paddle.Paddle;
import com.netut.arkanoid.game.power_up_panel.power_up.PowerUp;
import com.netut.arkanoid.game.power_up_panel.side_panel.SidePanel;
import com.netut.arkanoid.game.GameState;
import com.netut.arkanoid.manager.game.gui.BackgroundManager;
import com.netut.arkanoid.manager.game.gui.GameOverlay;
import com.netut.arkanoid.manager.StateManager;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, GameEventListener {

    private GameLoop gameLoop;
    private GameEngine engine;
    private GameState state;
    private InputHandler inputHandler;
    private BackgroundManager backgroundManager;
    private GameOverlay overlay;
    private SidePanel sidePanel;
    private Bundle savedState;
    private boolean continueGame;
    private Paint paint;

    public GamePanel(Context context, Bundle savedInstanceState, boolean continueGame) {
        super(context);
        this.savedState = savedInstanceState;
        this.continueGame = continueGame;
        getHolder().addCallback(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setSidePanel(SidePanel sidePanel) {
        this.sidePanel = sidePanel;
    }

    public GameEngine getEngine() {
        return engine;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        int width = getWidth();
        int height = getHeight();

        backgroundManager = new BackgroundManager();
        backgroundManager.loadBackground(getResources(), R.drawable.arkanoid_game_background, width, height);

        engine = new GameEngine(width, height);
        engine.setListener(this);
        // Передаём engine в боковую панель
        if (sidePanel != null) {
            sidePanel.setEngine(engine);
        }

        state = engine.getState();

        if (savedState != null) {
            StateManager.restoreFromBundle(savedState, state, engine, width);
            engine.loadCurrentLevel();
            savedState = null;
        } else if (continueGame) {
            StateManager.restoreFromPreferences(getContext(), state, engine, width);
            engine.resetGame();
        } else {
            engine.resetGame();
        }

        inputHandler = new InputHandler(engine);
        overlay = new GameOverlay(getContext(), width, height, engine, this);
        gameLoop = new GameLoop(engine, this::draw);
        gameLoop.start();
    }

    private void draw() {
        SurfaceHolder holder = getHolder();
        if (!holder.getSurface().isValid()) return;

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) return;

        if (overlay.isShowing()) {
            overlay.draw(canvas);
        } else {
            // Отрисовка игры
            if (backgroundManager.getBackground() != null) {
                canvas.drawBitmap(backgroundManager.getBackground(), 0, 0, null);
            } else {
                canvas.drawColor(Color.BLACK);
            }

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            // Рисуем блоки
            for (Brick brick : engine.getBricks()) {
                if (brick.isVisible()) {
                    paint.setColor(brick.getColor());
                    canvas.drawRect(brick.getLeft(), brick.getTop(),
                            brick.getRight(), brick.getBottom(), paint);
                }
            }

            // Рисуем платформу
            Paddle paddle = engine.getPaddle();
            paint.setColor(GameConstants.PADDLE_COLOR);
            canvas.drawRect(paddle.x, paddle.y, paddle.x + paddle.width,
                    paddle.y + paddle.height, paint);

            // Рисуем шарики
            paint.setColor(GameConstants.BALL_COLOR);
            for (Ball ball : engine.getBalls()) {
                canvas.drawCircle(ball.x, ball.y, ball.radius, paint);
            }

            // Рисуем падающие бонусы
            for (PowerUp p : engine.getPowerUps()) {
                p.draw(canvas, paint);
            }
        }

        holder.unlockCanvasAndPost(canvas);
        // Обновляем боковую панель
        if (sidePanel != null) {
            sidePanel.postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return inputHandler.onTouchEvent(event);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (gameLoop != null) gameLoop.stop();
        if (backgroundManager != null) backgroundManager.recycle();
    }

    @Override
    public void onGameOver(int score, int level) {
        overlay.showGameOver(score, level);
    }

    @Override
    public void onLevelComplete() {
        overlay.showLevelComplete();
    }

    @Override
    public void onPowerUpCollected(PowerUp powerUp) {
        if (sidePanel != null) {
            sidePanel.addPowerUp(powerUp);
        }
    }

    public void pause() {
        if (gameLoop != null) gameLoop.pause();
    }

    public void resume() {
        if (gameLoop != null) gameLoop.resume();
    }

    public void onSaveInstanceState(Bundle outState) {
        StateManager.saveToBundle(outState, state, engine);
    }
}