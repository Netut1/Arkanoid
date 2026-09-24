package com.netut.arkanoid.game.power_up_panel.power_up;

public enum PowerUpType {
    EXPAND_PADDLE("<->"),
    STICKY("~~"),
    TRIPLE("×3"),
    DOUBLE("×2"),
    EXTRA_LIFE("+"),
    SLOW("\\//"),
    FAST("//\\"),
    MINUS_LIFE("-"),
    SHRINK_PADDLE(">-<");

    private final String symbol;

    PowerUpType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    // Статический массив всех типов для случайного выбора
    public static final PowerUpType[] ALL = values();
}