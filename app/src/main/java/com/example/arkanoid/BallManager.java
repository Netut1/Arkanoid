package com.example.arkanoid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BallManager {
    private final int screenWidth, screenHeight;
    private final GameState state;
    private final PaddleManager paddleManager;
    private final BrickManager brickManager;
    private final List<Ball> balls = new ArrayList<>();
    private boolean ballLaunched = false;
    private long ballResetTime = 0;
    private final float baseBallSpeed;

    public BallManager(int screenWidth, int screenHeight, GameState state,
                       PaddleManager paddleManager, BrickManager brickManager,
                       float baseBallSpeed) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.state = state;
        this.paddleManager = paddleManager;
        this.brickManager = brickManager;
        this.baseBallSpeed = baseBallSpeed;
    }

    public void reset(Paddle paddle) {
        balls.clear();
        Ball ball = new Ball(
                paddle.x + paddle.width / 2,
                paddle.y - GameConstants.BALL_RADIUS - 1,
                GameConstants.BALL_RADIUS,
                baseBallSpeed,
                -baseBallSpeed
        );
        balls.add(ball);
        ballLaunched = false;
        ballResetTime = 0;
    }

    public void update(Paddle paddle) {
        applySpeedEffects();

        Iterator<Ball> it = balls.iterator();
        while (it.hasNext()) {
            Ball ball = it.next();

            if (ball.attached) {
                ball.x = paddle.x + paddle.width / 2;
                continue;
            }

            ball.move();

            if (ball.x - ball.radius < 0) {
                ball.x = ball.radius;
                ball.velX = -ball.velX;
            } else if (ball.x + ball.radius > screenWidth) {
                ball.x = screenWidth - ball.radius;
                ball.velX = -ball.velX;
            }
            if (ball.y - ball.radius < 0) {
                ball.y = ball.radius;
                ball.velY = -ball.velY;
            }

            if (ball.y + ball.radius > screenHeight) {
                it.remove();
                if (balls.isEmpty()) {
                    handleLifeLost(paddle);
                    return;
                }
                continue;
            }

            if (ball.y + ball.radius > paddle.y && ball.x > paddle.x && ball.x < paddle.x + paddle.width) {
                if (state.isStickyActive()) {
                    ball.attached = true;
                } else {
                    CollisionManager.handlePaddleCollision(ball, paddle);
                }
            }

            brickManager.checkCollisionWithBall(ball);
        }

        ballLaunched = balls.stream().anyMatch(b -> !b.attached);
    }

    private void handleLifeLost(Paddle paddle) {
        state.decreaseLives();
        if (state.getLives() <= 0) {
            // GameOver будет обработан через listener
        } else {
            reset(paddle);
            ballLaunched = false;
            ballResetTime = GameConstants.RESET_DELAY_MS;
        }
    }

    public void applySpeedEffects() {
        float factor = 1f;
        if (state.isSlowActive()) factor *= GameConstants.SLOW_FACTOR;
        if (state.isFastActive()) factor *= GameConstants.FAST_FACTOR;

        for (Ball ball : balls) {
            if (!ball.attached) {
                float speed = (float) Math.hypot(ball.velX, ball.velY);
                if (speed > 0) {
                    float newSpeed = baseBallSpeed * factor;
                    float ratio = newSpeed / speed;
                    ball.velX *= ratio;
                    ball.velY *= ratio;
                }
            }
        }
    }

    public void splitBalls(int count, Random random) {
        List<Ball> newBalls = new ArrayList<>();
        for (Ball ball : balls) {
            if (!ball.attached) {
                for (int i = 0; i < count - 1; i++) {
                    Ball newBall = new Ball(ball.x, ball.y, ball.radius, ball.velX, ball.velY);
                    double angle = Math.atan2(ball.velY, ball.velX);
                    angle += (random.nextDouble() - 0.5) * 0.5;
                    float speed = (float) Math.hypot(ball.velX, ball.velY);
                    newBall.velX = (float) (speed * Math.cos(angle));
                    newBall.velY = (float) (speed * Math.sin(angle));
                    newBalls.add(newBall);
                }
            }
        }
        balls.addAll(newBalls);
    }

    public void launchAll() {
        for (Ball ball : balls) {
            if (ball.attached) {
                ball.attached = false;
                ball.velX = baseBallSpeed * (new Random().nextBoolean() ? 1 : -1);
                ball.velY = -baseBallSpeed;
            }
        }
        if (!ballLaunched && ballResetTime == 0) {
            ballLaunched = true;
        }
    }

    public void setBallLaunched(boolean launched) { this.ballLaunched = launched; }
    public void setBallResetTime(long resetTime) { this.ballResetTime = resetTime; }
    public boolean isBallLaunched() { return ballLaunched; }
    public long getBallResetTime() { return ballResetTime; }
    public List<Ball> getBalls() { return balls; }
}