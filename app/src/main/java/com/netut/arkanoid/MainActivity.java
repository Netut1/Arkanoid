package com.netut.arkanoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arkanoid.R;
import com.netut.arkanoid.game.entity.brick.BrickTitleColorizer;

public class MainActivity extends AppCompatActivity {

    private TextView recordLevelView, recordScoreView;
    private Button startButton, continueButton;
    private LinearLayout startContainer, continueContainer, buttonsContainer;
    private SharedPreferences preferences;
    private LinearLayout titleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = findViewById(R.id.mainLayout);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mainLayout.setBackgroundResource(R.drawable.arkanoid_main_background);
        } else {
            mainLayout.setBackgroundResource(R.drawable.arkanoid_main_vertical_background);
        }

        initViews();
        setupTitleBlocks();
        setupButtons();
        setupImmersiveMode();
    }

    private void initViews() {
        titleContainer = findViewById(R.id.titleContainer);
        recordLevelView = findViewById(R.id.textViewMaxLevel);
        recordScoreView = findViewById(R.id.textViewRecord);
        startButton = findViewById(R.id.buttonStart);
        continueButton = findViewById(R.id.buttonContinue);
        startContainer = findViewById(R.id.startContainer);
        continueContainer = findViewById(R.id.continueContainer);
        buttonsContainer = findViewById(R.id.buttonsContainer);
        preferences = getSharedPreferences("ArkanoidPrefs", MODE_PRIVATE);
    }

    private void setupTitleBlocks() {
        // Центрируем блоки в верхней половине экрана
        titleContainer.post(() -> {
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int topMargin = screenHeight / 4 - titleContainer.getHeight() / 2;
            ((LinearLayout.LayoutParams) titleContainer.getLayoutParams()).topMargin = topMargin;
            titleContainer.requestLayout();
        });

        // Раскрашиваем кирпичи
        BrickTitleColorizer colorizer = new BrickTitleColorizer(getResources());
        colorizer.colorize(titleContainer);
    }

    private void setupButtons() {
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("continueGame", false);
            startActivity(intent);
        });

        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("continueGame", true);
            startActivity(intent);
        });
    }

    private void setupImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
        updateContinueButton();
    }

    private void updateStats() {
        int maxLevel = preferences.getInt("maxLevel", 1);
        int maxScore = preferences.getInt("record", 0);
        recordLevelView.setText(getString(R.string.max_level, maxLevel));
        recordScoreView.setText(getString(R.string.record, maxScore));
    }

    private void updateContinueButton() {
        boolean hasSavedGame = preferences.contains("saved_level");
        if (hasSavedGame) {
            continueContainer.setVisibility(View.VISIBLE);
            // Центрируем обе кнопки (они уже в горизонтальном LinearLayout, он сам центрирует)
        } else {
            continueContainer.setVisibility(View.GONE);
        }
    }
}