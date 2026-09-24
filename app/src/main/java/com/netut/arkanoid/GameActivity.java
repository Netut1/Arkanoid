package com.netut.arkanoid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arkanoid.R;
import com.netut.arkanoid.engine.GameEngine;
import com.netut.arkanoid.engine.gui.GamePanel;
import com.netut.arkanoid.game.power_up_panel.side_panel.SidePanel;

public class GameActivity extends AppCompatActivity {

    private GameEngine engine;
    private GamePanel gamePanel;
    private SidePanel sidePanel;
    private SharedPreferences preferences;
    private boolean continueGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("ArkanoidPrefs", MODE_PRIVATE);
        continueGame = getIntent().getBooleanExtra("continueGame", false);

        // Создаём главный контейнер
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setBackgroundResource(R.drawable.arkanoid_game_gameactivity_background);

        // Левая панель (25% ширины)
        sidePanel = new SidePanel(this);
        LinearLayout.LayoutParams sideParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        mainLayout.addView(sidePanel, sideParams);

        // Игровая панель (75% ширины)
        gamePanel = new GamePanel(this, savedInstanceState, continueGame);
        LinearLayout.LayoutParams gameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 5);
        mainLayout.addView(gamePanel, gameParams);

        setContentView(mainLayout);

        // Получаем ссылку на GameEngine из GamePanel
        engine = gamePanel.getEngine();
        sidePanel.setEngine(engine);
        gamePanel.setSidePanel(sidePanel);

        setupImmersiveMode();
    }

    private void setupImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        gamePanel.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gamePanel.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        gamePanel.onSaveInstanceState(outState);
    }

    public void gameOver(int score, int level) {
        // Обновляем достижения
        int maxLevel = preferences.getInt("maxLevel", 1);
        int maxScore = preferences.getInt("record", 0);

        if (level > maxLevel) {
            preferences.edit().putInt("maxLevel", level).apply();
        }
        if (score > maxScore) {
            preferences.edit().putInt("record", score).apply();
        }

        clearSavedGame();
        finish();
    }

    private void clearSavedGame() {
        preferences.edit()
                .remove("saved_level")
                .remove("saved_score")
                .remove("saved_lives")
                .apply();
    }
}