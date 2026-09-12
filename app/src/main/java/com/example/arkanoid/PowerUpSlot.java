package com.example.arkanoid;

public class PowerUpSlot {
    private PowerUp powerUp;

    public boolean hasPowerUp() {
        return powerUp != null;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public PowerUp takePowerUp() {
        PowerUp p = powerUp;
        powerUp = null;
        return p;
    }
}