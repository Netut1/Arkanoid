package com.example.arkanoid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickManager {
    private final GameState state;
    private final PowerUpManager powerUpManager;
    private final Random random = new Random();
    private List<Brick> bricks = new ArrayList<>();

    public BrickManager(GameState state, PowerUpManager powerUpManager) {
        this.state = state;
        this.powerUpManager = powerUpManager;
    }

    public void loadLevel(Level level) {
        bricks = new ArrayList<>(level.getBricks());
    }

    public void checkCollisionWithBall(Ball ball) {
        for (Brick brick : bricks) {
            if (!brick.isVisible()) continue;
            if (CollisionManager.checkCollision(ball, brick.getLeft(), brick.getTop(),
                    brick.getRight(), brick.getBottom())) {
                boolean destroyed = CollisionManager.handleBrickCollision(ball, brick);
                if (destroyed) {
                    state.addScore(brick.getScoreValue());
                    if (random.nextFloat() < GameConstants.POWERUP_DROP_CHANCE) {
                        PowerUpType[] types = PowerUpType.ALL;
                        PowerUpType type = types[random.nextInt(types.length)];
                        float cx = (brick.getLeft() + brick.getRight()) / 2;
                        float cy = (brick.getTop() + brick.getBottom()) / 2;
                        powerUpManager.addPowerUp(cx, cy, type);
                    }
                }
                break;
            }
        }
    }

    public boolean isLevelComplete() {
        for (Brick brick : bricks) {
            if (brick.isVisible() && brick.getType() != BrickType.INDESTRUCTIBLE) {
                return false;
            }
        }
        return true;
    }

    public List<Brick> getBricks() { return bricks; }
}