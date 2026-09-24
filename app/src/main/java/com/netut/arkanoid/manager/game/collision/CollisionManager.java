package com.netut.arkanoid.manager.game.collision;

import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.game.entity.ball.Ball;
import com.netut.arkanoid.game.entity.brick.Brick;
import com.netut.arkanoid.game.entity.paddle.Paddle;

public class CollisionManager {

    public static boolean checkCollision(Ball ball, float left, float top, float right, float bottom) {
        return !(ball.x + ball.radius < left ||
                ball.x - ball.radius > right ||
                ball.y + ball.radius < top ||
                ball.y - ball.radius > bottom);
    }

    public static boolean handleBrickCollision(Ball ball, Brick brick) {
        float overlapLeft = ball.x + ball.radius - brick.getLeft();
        float overlapRight = brick.getRight() - (ball.x - ball.radius);
        float overlapTop = ball.y + ball.radius - brick.getTop();
        float overlapBottom = brick.getBottom() - (ball.y - ball.radius);

        if (overlapLeft < overlapRight && overlapLeft < overlapTop && overlapLeft < overlapBottom) {
            ball.velX = -ball.velX;
            ball.x = brick.getLeft() - ball.radius;
        } else if (overlapRight < overlapLeft && overlapRight < overlapTop && overlapRight < overlapBottom) {
            ball.velX = -ball.velX;
            ball.x = brick.getRight() + ball.radius;
        } else if (overlapTop < overlapLeft && overlapTop < overlapRight && overlapTop < overlapBottom) {
            ball.velY = -ball.velY;
            ball.y = brick.getTop() - ball.radius;
        } else {
            ball.velY = -ball.velY;
            ball.y = brick.getBottom() + ball.radius;
        }

        return brick.hit();
    }

    public static boolean handlePaddleCollision(Ball ball, Paddle paddle) {
        float overlapLeft = ball.x + ball.radius - paddle.x;
        float overlapRight = paddle.x + paddle.width - (ball.x - ball.radius);
        float overlapTop = ball.y + ball.radius - paddle.y;
        float overlapBottom = paddle.y + paddle.height - (ball.y - ball.radius);

        if (overlapLeft < 0 || overlapRight < 0 || overlapTop < 0 || overlapBottom < 0) {
            return false;
        }

        float minOverlap = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

        if (minOverlap == overlapLeft) {
            ball.x = paddle.x - ball.radius;
            ball.velX = -ball.velX;
        } else if (minOverlap == overlapRight) {
            ball.x = paddle.x + paddle.width + ball.radius;
            ball.velX = -ball.velX;
        } else if (minOverlap == overlapTop) {
            ball.y = paddle.y - ball.radius;
            float hitPos = (ball.x - (paddle.x + paddle.width / 2)) / (paddle.width / 2);
            ball.velX = hitPos * GameConstants.BALL_SPEED * GameConstants.PADDLE_HIT_FACTOR;
            float minSpeed = GameConstants.BALL_SPEED * GameConstants.MIN_HORIZONTAL_SPEED_FACTOR;
            if (Math.abs(ball.velX) < minSpeed) {
                ball.velX = (ball.velX > 0) ? minSpeed : -minSpeed;
            }
            ball.velY = -Math.abs(ball.velY);
        } else {
            ball.y = paddle.y + paddle.height + ball.radius;
            ball.velY = -ball.velY;
        }
        return true;
    }
}