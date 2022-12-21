package com.example.pokerun_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pokerun_2.BackgroundSound;
import com.example.pokerun_2.GameManager;
import com.example.pokerun_2.R;
import com.example.pokerun_2.StepCallback;
import com.example.pokerun_2.TiltDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BigGameActivity extends AppCompatActivity {

    public static final String BUTTON_STATUS = "BUTTON_STATUS";
    public static final String SPEED_STATUS = "SPEED_STATUS";

    private boolean buttonStatus, speedStatus;

    private FloatingActionButton game_FAB_left;
    private FloatingActionButton game_FAB_right;

    private MaterialTextView game_LBL_score;

    private ArrayList<LinearLayout> game_LLO_lanes;
    private ArrayList<LinearLayout> game_LLO_lives;
    private ArrayList<ShapeableImageView> game_IMG_obstacles;
    private ArrayList<ShapeableImageView> game_IMG_trainers;
    private ArrayList<ShapeableImageView> game_IMG_lives;
    private ArrayList<ShapeableImageView> game_IMG_players;

    private GameManager gameManager;

    private TiltDetector tiltDetector;

    private Timer timer = new Timer();

    private BackgroundSound mBackgroundSound;
    private Toast toaster;
    private Vibrator v;

    private long startTime = System.currentTimeMillis();
    private long millis;

    private int delay = 750;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_game);

//        savedInstanceState;
        findViews();
        gameManager = new GameManager(game_IMG_lives.size());

        toaster = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        Intent previousIntent = getIntent();
        buttonStatus = previousIntent.getBooleanExtra(BUTTON_STATUS, true);
        speedStatus = previousIntent.getBooleanExtra(SPEED_STATUS, false);

        initViews();
        setDelay();
        refreshUI();
        startGame();
    }


    //////SET GAME//////
    private void setDelay() {
        if (speedStatus){
            delay = 375;
        }
        else{
            delay = 750;
        }
    }

    private void findViews() {

        game_FAB_left = findViewById(R.id.game_FAB_left);
        game_FAB_right = findViewById(R.id.game_FAB_right);

        game_LBL_score = findViewById(R.id.game_LBL_score);

        game_IMG_lives = new ArrayList<>();
        game_IMG_lives.add(findViewById(R.id.game_IMG_life0));
        game_IMG_lives.add(findViewById(R.id.game_IMG_life1));
        game_IMG_lives.add(findViewById(R.id.game_IMG_life2));

        game_LLO_lanes = new ArrayList<>();
        game_LLO_lanes.add(findViewById(R.id.game_LLO_lane0));
        game_LLO_lanes.add(findViewById(R.id.game_LLO_lane1));
        game_LLO_lanes.add(findViewById(R.id.game_LLO_lane2));
        game_LLO_lanes.add(findViewById(R.id.game_LLO_lane3));
        game_LLO_lanes.add(findViewById(R.id.game_LLO_lane4));
        game_LLO_lanes.add(findViewById(R.id.game_LLO_lane5));
        game_LLO_lanes.add(findViewById(R.id.game_LLO_lane6));

        game_IMG_players = new ArrayList<>();
        game_IMG_players.add(findViewById(R.id.game_IMG_player0));
        game_IMG_players.add(findViewById(R.id.game_IMG_player1));
        game_IMG_players.add(findViewById(R.id.game_IMG_player2));
        game_IMG_players.add(findViewById(R.id.game_IMG_player3));
        game_IMG_players.add(findViewById(R.id.game_IMG_player4));

        game_IMG_trainers = new ArrayList<>();
        game_IMG_trainers.add(findViewById(R.id.game_IMG_trainer0));
        game_IMG_trainers.add(findViewById(R.id.game_IMG_trainer1));
        game_IMG_trainers.add(findViewById(R.id.game_IMG_trainer2));
        game_IMG_trainers.add(findViewById(R.id.game_IMG_trainer3));
        game_IMG_trainers.add(findViewById(R.id.game_IMG_trainer4));

        game_IMG_obstacles = new ArrayList<>();
        int rows = game_LLO_lanes.size(), cols = game_LLO_lanes.get(0).getChildCount();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                game_IMG_obstacles.add((ShapeableImageView) game_LLO_lanes.get(i).getChildAt(j));
            }
        }
    }

    private void initViews() {
        if (buttonStatus) {
            if (tiltDetector == null)
                initTiltDetector();
        } else {
            if (tiltDetector != null)
                tiltDetector.stop();
            game_FAB_right.setOnClickListener(view -> actionRight());
            game_FAB_left.setOnClickListener(view -> actionLeft());
        }
        changeButtonVisibility();
    }

    private void changeButtonVisibility() {
        if (buttonStatus) {
            game_FAB_left.setVisibility(View.INVISIBLE);
            game_FAB_right.setVisibility(View.INVISIBLE);
        } else {
            game_FAB_left.setVisibility(View.VISIBLE);
            game_FAB_right.setVisibility(View.VISIBLE);
        }
    }

    private void initTiltDetector() {
        tiltDetector = new TiltDetector(this, new StepCallback() {
            @Override
            public void tiltRight() {
                actionRight();
            }

            @Override
            public void tiltLeft() {
                actionLeft();
            }
        });
    }

    private void actionRight() {
        gameManager.movePlayerRight();
        updatePlayerPos();
    }

    private void actionLeft() {
        gameManager.movePlayerLeft();
        updatePlayerPos();
    }


    //////UPDATE VISUALS//////
    private void startGame() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> BigGameActivity.this.refreshUI());
                    }
                }
                , delay, delay);
    }

    private void refreshUI() {
        gameManager.setNextState();
        updatePlayerPos();
        updateObjPos();
        updateTrainerPos();
        updateScore();
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (gameManager.checkHit(v)) {
            toast();
        }
        gameManager.setHit(false);
        if (gameManager.isLose(v)) {
            openScoreScreen("Game Over", gameManager.getScore());
        } else if (gameManager.getHits() != 0)
            game_IMG_lives.get(game_IMG_lives.size() - gameManager.getHits()).setVisibility(View.INVISIBLE);

    }

    private void updateScore() {
        int score = gameManager.getScore();
        score += (System.currentTimeMillis() - startTime) / delay;
        game_LBL_score.setText("" + score);
    }

    private void updateObjPos() {                   // update object position
        int[][] currentState = gameManager.getCurrentState();
        int rows = currentState.length, cols = currentState[0].length;
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                if (currentState[i][j] == GameManager.OBSTACLE_CODE) {
//                    game_IMG_obstacles[i * cols + j];
                    game_IMG_obstacles.get(i * cols + j).setVisibility(View.VISIBLE);
                    game_IMG_obstacles.get(i * cols + j).setImageResource(R.drawable.pokeball);
                } else if (currentState[i][j] == GameManager.EMPTY_CODE) {
                    game_IMG_obstacles.get(i * cols + j).setVisibility(View.INVISIBLE);
                } else if (currentState[i][j] == GameManager.SCORE_CODE) {
                    game_IMG_obstacles.get(i * cols + j).setVisibility(View.VISIBLE);
                    game_IMG_obstacles.get(i * cols + j).setImageResource(R.drawable.pokecoin);
                }
            }
        }
    }

    private void updatePlayerPos() {                // update player position
        int[][] currentState = gameManager.getCurrentState();
        int rows = currentState.length, cols = currentState[0].length;
        for (int i = 0; i < cols; i++) {
            if (currentState[rows - 1][i] == GameManager.EMPTY_CODE) {
                game_IMG_players.get(i).setVisibility(View.INVISIBLE);
            } else if (currentState[rows - 1][i] == GameManager.PLAYER_CODE) {
                game_IMG_players.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateTrainerPos() {
        int[][] currentState = gameManager.getCurrentState();
        for (int i = 0; i < currentState[0].length; i++) {
            if (currentState[0][i] == GameManager.OBSTACLE_CODE)
                game_IMG_trainers.get(i).setVisibility(View.VISIBLE);
            else if (currentState[0][i] == GameManager.EMPTY_CODE)
                game_IMG_trainers.get(i).setVisibility(View.INVISIBLE);
        }
    }

    private void toast() {
        if (toaster != null)
            toaster.cancel();
        String name = "Ouch!";
        toaster = Toast
                .makeText(this, name, Toast.LENGTH_SHORT);
        toaster.show();
    }


    ///////CHANGE SCREENS///////
    private void openScoreScreen(String status, int score) {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        scoreIntent.putExtra(ScoreActivity.KEY_SCORE, score);
        scoreIntent.putExtra(ScoreActivity.KEY_STATUS, status);
        startActivity(scoreIntent);
        finish();
    }


    ///////ON PHONE ACTION//////
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        v.cancel();
        toaster.cancel();
        mBackgroundSound.cancel(true);
        if(tiltDetector != null)
            tiltDetector.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        v.cancel();
        toaster.cancel();
        mBackgroundSound.cancel(true);
        if(tiltDetector != null)
            tiltDetector.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        mBackgroundSound = new BackgroundSound(this);
        mBackgroundSound.execute();
        if(tiltDetector != null)
            tiltDetector.start();
    }

    //    private void OpenScoreScreen() {
//        Intent scoreIntent = new Intent(this, ScoreActivity.class);
//        scoreIntent.putExtra(ScoreActivity.KEY_SCORE, score);
//        scoreIntent.putExtra(ScoreActivity.KEY_STATUS, status);
//        startActivity(scoreIntent);
//        finish();
//    }


}