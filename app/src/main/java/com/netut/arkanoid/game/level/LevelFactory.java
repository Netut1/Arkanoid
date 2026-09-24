package com.netut.arkanoid.game.level;

import com.netut.arkanoid.engine.GameConstants;
import com.netut.arkanoid.game.entity.brick.Brick;
import com.netut.arkanoid.game.entity.brick.BrickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelFactory {
    private final int screenWidth;
    private final int brickWidth;
    private final Random random = new Random();

    public static final char CHAR_NORMAL = '-';
    public static final char CHAR_TOUGH = '=';
    public static final char CHAR_INDESTRUCTIBLE = '+';
    public static final char CHAR_EMPTY = '.';

    public LevelFactory(int screenWidth) {
        this.screenWidth = screenWidth;
        this.brickWidth = screenWidth / GameConstants.BRICK_COLS;
    }

    public Level getLevel(int levelNumber) {
        return createLevelFromMask(LevelType.getRandomMask(), levelNumber);
    }

    private Level createLevelFromMask(String[] mask, int levelNumber) {
        if (mask == null || mask.length == 0) {
            return new Level("Уровень " + levelNumber, new ArrayList<>());
        }

        int rows = mask.length;
        for (String line : mask) {
            if (line.length() != GameConstants.BRICK_COLS) {
                throw new IllegalArgumentException("Каждая строка маски должна содержать ровно " +
                        GameConstants.BRICK_COLS + " символов");
            }
        }

        List<Brick> bricks = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            String line = mask[row];
            for (int col = 0; col < GameConstants.BRICK_COLS; col++) {
                char c = line.charAt(col);
                if (c == CHAR_EMPTY) continue;

                BrickType type;
                int color;

                switch (c) {
                    case CHAR_NORMAL:
                        type = BrickType.NORMAL;
                        color = getRandomNormalColor();
                        break;
                    case CHAR_TOUGH:
                        type = BrickType.TOUGH;
                        color = GameConstants.TOUGH_BRICK_COLOR;
                        break;
                    case CHAR_INDESTRUCTIBLE:
                        type = BrickType.INDESTRUCTIBLE;
                        color = GameConstants.INDESTRUCTIBLE_BRICK_COLOR;
                        break;
                    default:
                        continue;
                }

                float left = col * brickWidth;
                float top = row * GameConstants.BRICK_HEIGHT + GameConstants.TOP_OFFSET;
                bricks.add(new Brick(left, top, brickWidth, GameConstants.BRICK_HEIGHT, type, color));
            }
        }

        return new Level("Уровень " + levelNumber, bricks);
    }

    private int getRandomNormalColor() {
        return GameConstants.NORMAL_BRICK_COLORS[random.nextInt(GameConstants.NORMAL_BRICK_COLORS.length)];
    }
}