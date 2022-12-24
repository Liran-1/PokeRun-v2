package com.example.pokerun_2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokerun_2.R;
import com.example.pokerun_2.UserHighScore;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<UserHighScore> userHighScores;
    private Context context;


    public ScoreAdapter(Context context, ArrayList<UserHighScore> userHighScores) {
        this.context        = context;
        this.userHighScores = userHighScores;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        ScoreViewHolder scoreViewHolder = new ScoreViewHolder(view);

        return scoreViewHolder;
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder viewHolder, int position) {
        UserHighScore userHighScore = getItem(position);
        viewHolder.user_LBL_placement   .setText(userHighScore.getPlace());
        viewHolder.player_LBL_score     .setText(userHighScore.getScore());
        viewHolder.user_LBL_name        .setText(userHighScore.getName());

    }

    private UserHighScore getItem(int position) {
        return userHighScores.get(position);
    }

    @Override
    public int getItemCount() {
        return userHighScores == null ? 0 : userHighScores.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView user_LBL_placement;
        private MaterialTextView player_LBL_score;
        private MaterialTextView user_LBL_name;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            // Define click listener for the ViewHolder's View

            user_LBL_placement  = itemView.findViewById(R.id.user_LBL_placement);
            player_LBL_score    = itemView.findViewById(R.id.player_LBL_score);
            user_LBL_name       = itemView.findViewById(R.id.user_LBL_name);
        }
    }

}

