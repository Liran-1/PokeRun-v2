package com.example.pokerun_2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokerun_2.Adapter.ScoreAdapter;
import com.example.pokerun_2.utils.gameSP;
import com.example.pokerun_2.R;
import com.example.pokerun_2.UserHighScore;

import java.util.ArrayList;

public class UserListFragment extends Fragment {

    private Context conext;
    private RecyclerView userRecycledView;
    private ScoreAdapter scoreAdapter;

    public UserListFragment(Context context) {
        this.conext = context;
        ArrayList<UserHighScore> userHighScores = gameSP.getInstance().getHighScores();
        scoreAdapter = new ScoreAdapter(context,userHighScores);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_item_recyclerview, container, false);

        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        userRecycledView.setLayoutManager(new LinearLayoutManager(conext));
        userRecycledView.setAdapter(scoreAdapter);
    }

    private void findViews(View view) {
        userRecycledView = view.findViewById(R.id.score_RV_userScores);
    }

    public ScoreAdapter getScoreAdapter() {
        return scoreAdapter;
    }

    public UserListFragment setScoreAdapter(ScoreAdapter scoreAdapter) {
        this.scoreAdapter = scoreAdapter;
        return this;
    }

}
