package com.example.pokerun_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.pokerun_2.R;
import com.google.android.material.textview.MaterialTextView;

public class ScoreActivity extends AppCompatActivity {

    public static final String KEY_STATUS = "KEY_STATUS";
    public static final String KEY_SCORE = "KEY_SCORE";

    private MaterialTextView score_LBL_score;
    private MapFragment mapFragment;
    private UserFragment userFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        userFragment = new UserFragment();
        mapFragment = new MapFragment();

        findViews();

        Intent previousIntent = getIntent();
        String status = previousIntent.getStringExtra(KEY_STATUS);
        int score = previousIntent.getIntExtra(KEY_SCORE,0);
//        score_LBL_score.setText(status + "\n" + score);
    }

    private void findViews() {
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRG_map, mapFragment).commit();

//        score_LBL_score = findViewById(R.id.score_LBL_score);
    }
}