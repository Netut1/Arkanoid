package com.netut.arkanoid.game.entity.brick;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netut.arkanoid.engine.GameConstants;

import java.util.Random;

public class BrickTitleColorizer {
    private final int[] brickColors;
    private final int strokeWidth;
    private final int strokeColor;
    private final float cornerRadius;
    private final Random random;

    public BrickTitleColorizer(Resources resources) {
        this.brickColors = GameConstants.NORMAL_BRICK_COLORS;
        this.strokeWidth = GameConstants.BRICK_STROKE_WIDTH;
        this.strokeColor = GameConstants.BRICK_STROKE_COLOR;
        this.cornerRadius = 8 * resources.getDisplayMetrics().density;
        this.random = new Random();
    }

    public void colorize(LinearLayout titleLayout) {
        for (int i = 0; i < titleLayout.getChildCount(); i++) {
            View child = titleLayout.getChildAt(i);
            if (child instanceof TextView) {
                colorizeTextView((TextView) child);
            }
        }
    }

    private void colorizeTextView(TextView tv) {
        int bgColor = brickColors[random.nextInt(brickColors.length)];

        int r = Color.red(bgColor);
        int g = Color.green(bgColor);
        int b = Color.blue(bgColor);
        double brightness = 0.299 * r + 0.587 * g + 0.114 * b;
        int textColor = brightness > 128 ? Color.BLACK : Color.WHITE;
        tv.setTextColor(textColor);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(bgColor);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setCornerRadius(cornerRadius);
        tv.setBackground(drawable);
    }
}