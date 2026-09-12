package com.example.arkanoid;

public class GameState {
    private int score;
    private int lives;
    private int currentLevel;

    private long paddleExpandUntil;
    private long paddleShrinkUntil;
    private long slowUntil;
    private long fastUntil;
    private long stickyUntil;

    public GameState() {
        reset();
    }

    public void reset() {
        score = 0;
        lives = GameConstants.INITIAL_LIVES;
        currentLevel = 1;
        clearEffects();
    }

    public void clearEffects() {
        paddleExpandUntil = 0;
        paddleShrinkUntil = 0;
        slowUntil = 0;
        fastUntil = 0;
        stickyUntil = 0;
    }

    // Геттеры и сеттеры для счёта, жизней, уровня
    public int getScore() { return score; }
    public void addScore(int value) { score += value; }
    public int getLives() { return lives; }
    public void decreaseLives() { lives--; }
    public void increaseLives() { if (lives < 3) lives++; }
    public int getCurrentLevel() { return currentLevel; }
    public void nextLevel() { currentLevel++; }
    public void setScore(int score) { this.score = score; }
    public void setLives(int lives) { this.lives = lives; }
    public void setCurrentLevel(int level) { this.currentLevel = level; }

    // Эффекты
    public boolean isPaddleExpanded() { return System.currentTimeMillis() < paddleExpandUntil; }
    public void setPaddleExpanded(long duration) { paddleExpandUntil = System.currentTimeMillis() + duration; }

    public boolean isPaddleShrunk() { return System.currentTimeMillis() < paddleShrinkUntil; }
    public void setPaddleShrunk(long duration) { paddleShrinkUntil = System.currentTimeMillis() + duration; }

    public boolean isSlowActive() { return System.currentTimeMillis() < slowUntil; }
    public void setSlowActive(long duration) { slowUntil = System.currentTimeMillis() + duration; }

    public boolean isFastActive() { return System.currentTimeMillis() < fastUntil; }
    public void setFastActive(long duration) { fastUntil = System.currentTimeMillis() + duration; }

    public boolean isStickyActive() { return System.currentTimeMillis() < stickyUntil; }
    public void setStickyActive(long duration) { stickyUntil = System.currentTimeMillis() + duration; }

    public void updateTimers() {
        long now = System.currentTimeMillis();
        if (paddleExpandUntil > 0 && now > paddleExpandUntil) paddleExpandUntil = 0;
        if (paddleShrinkUntil > 0 && now > paddleShrinkUntil) paddleShrinkUntil = 0;
        if (slowUntil > 0 && now > slowUntil) slowUntil = 0;
        if (fastUntil > 0 && now > fastUntil) fastUntil = 0;
        if (stickyUntil > 0 && now > stickyUntil) stickyUntil = 0;
    }
}