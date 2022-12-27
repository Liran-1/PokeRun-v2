package com.example.pokerun_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.pokerun_2.Adapter.ScoreAdapter;
import com.example.pokerun_2.R;
import com.example.pokerun_2.UserHighScore;
import com.example.pokerun_2.callbacks.MapCallBack;
import com.example.pokerun_2.fragments.MapFragment;
import com.example.pokerun_2.fragments.UserListFragment;
import com.example.pokerun_2.utils.gameSP;

public class ScoreActivity extends AppCompatActivity {

    public static final String KEY_USERNAME = "KEY_USERNAME";
    public static final String KEY_SCORE    = "KEY_SCORE";
    public static final String KEY_LATITUDE = "KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE";

    private MapFragment mapFragment;
    private UserListFragment userListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        userListFragment = new UserListFragment();
        userListFragment.setCallback(new MapCallBack() {
            @Override
            public void userHighScoreClicked(UserHighScore userHighScore, int pos) {
                mapFragment.goToLocation(userHighScore);
            }
        });
        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, userListFragment).commit();

    }

}