package com.netut.arkanoid.game.entity.ball;

public class Ball {
    public float x, y;
    public float radius;
    public float velX, velY;
    public boolean attached = false;

    public Ball(float x, float y, float radius, float velX, float velY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.velX = velX;
        this.velY = velY;
    }

    public void move() {
        if (!attached) {
            x += velX;
            y += velY;
        }
    }
}