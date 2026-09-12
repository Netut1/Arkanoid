package com.example.arkanoid;

public interface GameEventListener {
    void onGameOver(int score, int level);
    void onLevelComplete();
    void onPowerUpCollected(PowerUp powerUp);
}