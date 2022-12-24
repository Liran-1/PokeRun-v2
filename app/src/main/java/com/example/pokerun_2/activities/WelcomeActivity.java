package com.example.pokerun_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pokerun_2.Manager.BackgroundSound;
import com.example.pokerun_2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class WelcomeActivity extends AppCompatActivity {

    private BackgroundSound mBackgroundSound;

    private MaterialButton welcome_BTN_start;
    private MaterialButton welcome_BTN_highscore;
    private SwitchMaterial welcome_SWT_gameMode;
    private SwitchMaterial welcome_SWT_buttons;
    private SwitchMaterial welcome_SWT_fast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViews();
        initViews();
    }

    private void initViews() {
        welcome_BTN_start.setOnClickListener(view -> clickedStart());
        welcome_BTN_highscore.setOnClickListener(view -> clickedHighScore());
    }

    private void clickedStart() {

        Intent startIntent = new Intent(WelcomeActivity.this, BigGameActivity.class);
        startIntent.putExtra(BigGameActivity.BUTTON_STATUS, welcome_SWT_buttons.isChecked());
        startIntent.putExtra(BigGameActivity.SPEED_STATUS, welcome_SWT_fast.isChecked());
        startActivity(startIntent);
    }

    private void clickedHighScore() {
        Intent myIntent = new Intent(WelcomeActivity.this, ScoreActivity.class);
        startActivity(myIntent);
    }

    private void findViews() {
        welcome_BTN_start = findViewById(R.id.welcome_BTN_start);
        welcome_BTN_highscore = findViewById(R.id.welcome_BTN_highscore);
        welcome_SWT_buttons = findViewById(R.id.welcome_SWT_buttons);
        welcome_SWT_fast = findViewById(R.id.welcome_SWT_fast);
    }

    private void openGameScreen(String status, int score) {
        String gameMode = "big";
        if(welcome_SWT_gameMode.isActivated()){
            gameMode = "small";
        }
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        scoreIntent.putExtra(String.valueOf(BigGameActivity.SPEED_STATUS), false);
        scoreIntent.putExtra(String.valueOf(BigGameActivity.BUTTON_STATUS), false);
        startActivity(scoreIntent);
        finish();
    }
}