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
import com.example.pokerun_2.callbacks.MapCallBack;
import com.example.pokerun_2.utils.gameSP;
import com.example.pokerun_2.R;
import com.example.pokerun_2.UserHighScore;

import java.util.ArrayList;

public class UserListFragment extends Fragment {

    private RecyclerView userRecycledView;
    private MapCallBack mapCallBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_item_recyclerview, container, false);

        findViews(view);
        initViews();

        return view;
    }

    private void initViews() {
        ArrayList list = gameSP.getInstance().getHighScores();
        ScoreAdapter scoreAdapter = new ScoreAdapter(getContext(),list );
        userRecycledView.setLayoutManager(new LinearLayoutManager(getContext()));
        userRecycledView.setAdapter(scoreAdapter);
        scoreAdapter.setMapCallBack(mapCallBack);
    }

    private void findViews(View view) {
        userRecycledView = view.findViewById(R.id.score_RV_userScores);
    }

    public void setCallback(MapCallBack mapCallBack) {
        this.mapCallBack = mapCallBack;
    }
}
