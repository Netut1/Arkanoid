package com.netut.arkanoid.game.power_up_panel.side_panel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.netut.arkanoid.GameActivity;
import com.netut.arkanoid.engine.GameEngine;
import com.netut.arkanoid.game.GameState;
import com.example.arkanoid.R;
import com.netut.arkanoid.manager.StateManager;
import com.netut.arkanoid.game.power_up_panel.power_up.PowerUp;
import com.netut.arkanoid.game.power_up_panel.power_up.PowerUpSlot;

public class SidePanel extends View {
    private Bitmap backIconBitmap;
    private GameEngine engine;
    private Paint paint;
    private List<PowerUpSlot> slots;
    private int slotWidth, slotHeight;
    private static final int SLOT_COUNT = 5;

    public SidePanel(Context context) {
        super(context);
        init();
        loadBackIcon();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);

        slots = new ArrayList<>();
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots.add(new PowerUpSlot());
        }
    }

    private void loadBackIcon() {
        // Используем VectorDrawableCompat для поддержки старых версий
        VectorDrawableCompat drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_back, getContext().getTheme());
        if (drawable == null) {
            // Fallback на обычный Drawable
            Drawable fallback = AppCompatResources.getDrawable(getContext(), R.drawable.ic_back);
            if (fallback instanceof VectorDrawableCompat) {
                drawable = (VectorDrawableCompat) fallback;
            }
        }

        if (drawable != null) {
            int iconSize = 60; // размер в пикселях (примерно 60x60)
            drawable.setBounds(0, 0, iconSize, iconSize);
            // Применяем белый цвет через ColorFilter
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            backIconBitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
            Canvas bitmapCanvas = new Canvas(backIconBitmap);
            drawable.draw(bitmapCanvas);
        }
    }

    public void setEngine(GameEngine engine) {
        this.engine = engine;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        slotWidth = w - 40;
        slotHeight = (int)(h * 0.6f) / SLOT_COUNT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (engine == null) return;

        GameState state = engine.getState();

        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        canvas.drawText(getContext().getString(R.string.max_level, state.getCurrentLevel()),
                getWidth() / 2f, 60, paint);

        // Счёт
        paint.setTextSize(40);
        canvas.drawText(getContext().getString(R.string.score, state.getScore()),
                getWidth() / 2f, 120, paint);  // Сместим ниже

        // Жизни
        paint.setTextSize(50);
        canvas.drawText("❤ " + state.getLives(), getWidth() / 2f, 180, paint);

        drawBackButton(canvas);
        drawPowerUpSlots(canvas);
    }

    private void drawBackButton(Canvas canvas) {
        int centerX = getWidth() / 2;
        int y = 260; // центр круга

        // Рисуем круг (оранжевый фон)
        paint.setColor(0xFFCC6600);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, y, 50, paint);

        // Чёрная обводка
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawCircle(centerX, y, 50, paint);

        // Рисуем иконку
        if (backIconBitmap != null) {
            int iconSize = 60;
            int left = centerX - iconSize / 2;
            int top = y - iconSize / 2;
            canvas.drawBitmap(backIconBitmap, left, top, null);
        }

        // Подпись "Назад"
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(getContext().getString(R.string.back), centerX, y + 70, paint);
    }

    private void drawPowerUpSlots(Canvas canvas) {
        int startY = 350;

        for (int i = 0; i < SLOT_COUNT; i++) {
            PowerUpSlot slot = slots.get(i);
            int y = startY + i * slotHeight;

            if (slot.hasPowerUp()) {
                PowerUp p = slot.getPowerUp();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(p.getColor());
                canvas.drawCircle(getWidth() / 2f, y + slotHeight / 2f,
                        slotHeight / 3f, paint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(30);
                canvas.drawText(p.getType().getSymbol(),
                        getWidth() / 2f, y + slotHeight / 2f + 10, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            if (Math.hypot(x - getWidth() / 2f, y - 260) < 50) {
                if (engine != null && engine.getState().getLives() > 0) {
                    StateManager.saveToPreferences(getContext(), engine.getState());
                }
                ((GameActivity) getContext()).finish();
                return true;
            }

            int startY = 300;
            for (int i = 0; i < SLOT_COUNT; i++) {
                int slotTop = startY + i * slotHeight;
                if (y > slotTop && y < slotTop + slotHeight - 10) {
                    if (slots.get(i).hasPowerUp()) {
                        PowerUp p = slots.get(i).takePowerUp();
                        engine.activatePowerUp(p.getType());
                        rearrangeSlots();
                        invalidate();
                    }
                    break;
                }
            }
        }
        return true;
    }

    public void addPowerUp(PowerUp powerUp) {
        for (PowerUpSlot slot : slots) {
            if (!slot.hasPowerUp()) {
                slot.setPowerUp(powerUp);
                invalidate();
                break;
            }
        }
    }

    private void rearrangeSlots() {
        for (int i = 0; i < slots.size() - 1; i++) {
            if (!slots.get(i).hasPowerUp() && slots.get(i + 1).hasPowerUp()) {
                slots.get(i).setPowerUp(slots.get(i + 1).takePowerUp());
            }
        }
    }
}