package com.netut.arkanoid.engine.interfaces;

import com.netut.arkanoid.game.power_up_panel.power_up.PowerUp;

public interface GameEventListener {
    void onGameOver(int score, int level);
    void onLevelComplete();
    void onPowerUpCollected(PowerUp powerUp);
}