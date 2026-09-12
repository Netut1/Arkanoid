package com.example.arkanoid;

public class Brick {
    private final float left, top, right, bottom;
    private boolean isVisible;
    private final BrickType type;
    private int currentHealth;
    private final int color;

    public Brick(float left, float top, float width, float height, BrickType type, int color) {
        this.left = left;
        this.top = top;
        this.right = left + width;
        this.bottom = top + height;
        this.type = type;
        this.currentHealth = type.getInitialHealth();
        this.isVisible = true;
        this.color = color;
    }

    public boolean hit() {
        if (type == BrickType.INDESTRUCTIBLE) return false;
        currentHealth--;
        if (currentHealth <= 0) {
            isVisible = false;
            return true;
        }
        return false;
    }

    public boolean isVisible() { return isVisible; }
    public float getLeft() { return left; }
    public float getTop() { return top; }
    public float getRight() { return right; }
    public float getBottom() { return bottom; }
    public int getScoreValue() { return type.getScoreValue(); }
    public BrickType getType() { return type; }

    public int getColor() {
        if (type == BrickType.TOUGH) {
            if (currentHealth == 2) return GameConstants.TOUGH_BRICK_COLOR;
            if (currentHealth == 1) return GameConstants.TOUGH_BRICK_DAMAGED_COLOR;
        }
        return color;
    }
}