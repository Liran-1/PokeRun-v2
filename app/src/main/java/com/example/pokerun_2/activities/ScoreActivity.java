package com.example.pokerun_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pokerun_2.R;

public class ScoreActivity extends AppCompatActivity {

    public static final String KEY_STATUS   = "KEY_STATUS";
    public static final String KEY_SCORE    = "KEY_SCORE";

    private MapFragment mapFragment;
    private UserListFragment userListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        userListFragment = new UserListFragment();
        mapFragment = new MapFragment();

        findViews();

        initViews();

        Intent previousIntent = getIntent();

    }

    private void findViews() {

    }

    private void initViews() {
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRG_map, mapFragment).commit();

        userListFragment = new UserListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRG_userlist, mapFragment).commit();

//        score_LBL_score = findViewById(R.id.score_LBL_score);
    }
}