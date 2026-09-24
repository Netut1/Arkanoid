package com.netut.arkanoid.manager.game.entity;

import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.engine.interfaces.GameEventListener;
import com.netut.arkanoid.game.entity.paddle.Paddle;
import com.netut.arkanoid.game.power_up_panel.power_up.PowerUp;
import com.netut.arkanoid.game.power_up_panel.power_up.PowerUpType;
import com.netut.arkanoid.game.GameState;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PowerUpManager {
    private final int screenHeight;
    private final GameState state;
    private BallManager ballManager;
    private final PaddleManager paddleManager;
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final Random random = new Random();
    private GameEventListener listener;

    public PowerUpManager(int screenHeight, GameState state, BallManager ballManager, PaddleManager paddleManager) {
        this.screenHeight = screenHeight;
        this.state = state;
        this.ballManager = ballManager;
        this.paddleManager = paddleManager;
    }

    public void setBallManager(BallManager ballManager) {
        this.ballManager = ballManager;
    }

    public void setListener(GameEventListener listener) {
        this.listener = listener;
    }

    public void addPowerUp(float x, float y, PowerUpType type) {
        powerUps.add(new PowerUp(x, y, type));
    }

    public void update(Paddle paddle) {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();

            if (p.y < paddle.y && p.y + p.radius >= paddle.y) {
                if (p.x + p.radius > paddle.x && p.x - p.radius < paddle.x + paddle.width) {
                    if (listener != null) {
                        listener.onPowerUpCollected(p);
                    }
                    it.remove();
                    continue;
                }
            }

            if (p.y - p.radius > screenHeight) {
                it.remove();
            }
        }
    }

    public void activate(PowerUpType type) {
        switch (type) {
            case EXPAND_PADDLE:
                state.setPaddleExpanded(GameConstants.EFFECT_DURATION);
                break;
            case SHRINK_PADDLE:
                state.setPaddleShrunk(GameConstants.EFFECT_DURATION);
                break;
            case STICKY:
                state.setStickyActive(GameConstants.EFFECT_DURATION);
                break;
            case TRIPLE:
                ballManager.splitBalls(3, random);
                break;
            case DOUBLE:
                ballManager.splitBalls(2, random);
                break;
            case EXTRA_LIFE:
                state.increaseLives();
                break;
            case MINUS_LIFE:
                state.decreaseLives();
                break;
            case SLOW:
                state.setSlowActive(GameConstants.EFFECT_DURATION);
                ballManager.applySpeedEffects();
                break;
            case FAST:
                state.setFastActive(GameConstants.EFFECT_DURATION);
                ballManager.applySpeedEffects();
                break;
        }
    }

    public List<PowerUp> getPowerUps() { return powerUps; }
}