package com.example.arkanoid;

public enum BrickType {
    NORMAL(1, 1),
    TOUGH(2, 2),
    INDESTRUCTIBLE(-1, 0);

    private final int initialHealth;
    private final int scoreValue;

    BrickType(int initialHealth, int scoreValue) {
        this.initialHealth = initialHealth;
        this.scoreValue = scoreValue;
    }

    public int getInitialHealth() { return initialHealth; }
    public int getScoreValue() { return scoreValue; }
}