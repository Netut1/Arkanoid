package com.netut.arkanoid.game.level;

import com.netut.arkanoid.game.entity.brick.Brick;

import java.util.List;

public class Level {
    private final String name;
    private final List<Brick> bricks;

    public Level(String name, List<Brick> bricks) {
        this.name = name;
        this.bricks = bricks;
    }

    public String getName() { return name; }
    public List<Brick> getBricks() { return bricks; }
}