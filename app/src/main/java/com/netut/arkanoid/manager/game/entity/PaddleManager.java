package com.netut.arkanoid.manager.game.entity;

import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.game.entity.paddle.Paddle;
import com.netut.arkanoid.game.GameState;

public class PaddleManager {
    private final int screenWidth;
    private final GameState state;
    private final int basePaddleWidth;
    private final int paddleHeight;
    private Paddle paddle;

    public PaddleManager(int screenWidth, int paddleHeight, int basePaddleWidth, GameState state) {
        this.screenWidth = screenWidth;
        this.paddleHeight = paddleHeight;
        this.basePaddleWidth = basePaddleWidth;
        this.state = state;
    }

    public void reset(float y) {
        paddle = new Paddle(
                screenWidth / 2f - basePaddleWidth / 2f,
                y,
                basePaddleWidth,
                paddleHeight
        );
    }

    public void update() {
        int currentWidth = basePaddleWidth;
        if (state.isPaddleExpanded()) currentWidth += GameConstants.PADDLE_EXPAND_AMOUNT;
        if (state.isPaddleShrunk()) currentWidth -= GameConstants.PADDLE_SHRINK_AMOUNT;
        if (currentWidth < 50) currentWidth = 50;
        paddle.width = currentWidth;
        if (paddle.x < 0) paddle.x = 0;
        if (paddle.x + paddle.width > screenWidth) paddle.x = screenWidth - paddle.width;

    }

    public void moveTo(float touchX) {
        float newX = touchX - paddle.width / 2;
        if (newX < 0) newX = 0;
        if (newX + paddle.width > screenWidth) newX = screenWidth - paddle.width;
        paddle.x = newX;
    }

    public void setPaddleX(float x) {
        paddle.x = x;
        if (paddle.x < 0) paddle.x = 0;
        if (paddle.x + paddle.width > screenWidth) paddle.x = screenWidth - paddle.width;
    }

    public Paddle getPaddle() { return paddle; }
}