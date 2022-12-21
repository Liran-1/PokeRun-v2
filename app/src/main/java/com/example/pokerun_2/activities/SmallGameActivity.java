package com.example.pokerun_2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.example.pokerun_2.GameManager;
import com.example.pokerun_2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SmallGameActivity extends AppCompatActivity {

    private FloatingActionButton game_FAB_left;
    private FloatingActionButton game_FAB_right;

    private ShapeableImageView[] game_IMG_obstacles;
    private ShapeableImageView[] game_IMG_trainers;
    private ShapeableImageView[] game_IMG_lives;
    private ShapeableImageView[] game_IMG_players;

    private GameManager gameManager;

    private Timer timer = new Timer();

    private Toast toaster;

    private long startTime;

    final int DELAY = 750;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_game);

        findViews();
        gameManager = new GameManager(game_IMG_lives.length);

        toaster = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        refreshUI();
        initViews();
        startGame();
    }

    private void findViews() {

        game_FAB_left = findViewById(R.id.game_FAB_left);
        game_FAB_right = findViewById(R.id.game_FAB_right);

        game_IMG_lives = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_life0),
                findViewById(R.id.game_IMG_life1),
                findViewById(R.id.game_IMG_life2)
        };

        game_IMG_players = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_player0),
                findViewById(R.id.game_IMG_player1),
                findViewById(R.id.game_IMG_player2)
        };
        game_IMG_trainers = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_trainer0),
                findViewById(R.id.game_IMG_trainer1),
                findViewById(R.id.game_IMG_trainer2)
        };

        game_IMG_obstacles = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_obstacle0),
                findViewById(R.id.game_IMG_obstacle1),
                findViewById(R.id.game_IMG_obstacle2),
                findViewById(R.id.game_IMG_obstacle3),
                findViewById(R.id.game_IMG_obstacle4),
                findViewById(R.id.game_IMG_obstacle5),
                findViewById(R.id.game_IMG_obstacle6),
                findViewById(R.id.game_IMG_obstacle7),
                findViewById(R.id.game_IMG_obstacle8),
                findViewById(R.id.game_IMG_obstacle9),
                findViewById(R.id.game_IMG_obstacle10),
                findViewById(R.id.game_IMG_obstacle11),
                findViewById(R.id.game_IMG_obstacle12),
                findViewById(R.id.game_IMG_obstacle13),
                findViewById(R.id.game_IMG_obstacle14)
        };

    }

    private void initViews() {
        game_FAB_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmallGameActivity.this.clickedRight();
            }
        });
        game_FAB_left.setOnClickListener(view -> clickedLeft());
    }

    private void clickedRight() {
        gameManager.movePlayerRight();
        updatePlayerPos();
    }

    private void clickedLeft() {
        gameManager.movePlayerLeft();
        updatePlayerPos();
    }

    private void startGame() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> SmallGameActivity.this.refreshUI());
                    }
                }
                , DELAY, DELAY);
    }

    private void refreshUI() {
        gameManager.setNextState();
        updatePlayerPos();
        updateObjPos();
        updateTrainerPos();
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(gameManager.checkHit(v)){
            toast();
        }
        gameManager.setHit(false);
//        if(isLose(v){
//            OpenScoreScreen();
//        } else {
        if (gameManager.getHits() != 0)
            game_IMG_lives[game_IMG_lives.length - gameManager.getHits()].setVisibility(View.INVISIBLE);
//        }
    }

    private void updateObjPos() {
        int[][] currentState = gameManager.getCurrentState();
        int rows = currentState.length, cols = currentState[0].length;
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                if (currentState[i][j] == GameManager.OBSTACLE_CODE) {
                    game_IMG_obstacles[i * cols + j].setVisibility(View.VISIBLE);
                }
                if (currentState[i][j] == GameManager.EMPTY_CODE) {
                    game_IMG_obstacles[i * cols + j].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void updatePlayerPos() {
        int[][] currentState = gameManager.getCurrentState();
        int rows = currentState.length, cols = currentState[0].length;
        for (int i = 0; i < cols; i++) {                            // update player position
            if (currentState[rows - 1][i] == GameManager.EMPTY_CODE) {
                game_IMG_players[i].setVisibility(View.INVISIBLE);
            } else if (currentState[rows - 1][i] == GameManager.PLAYER_CODE) {
                game_IMG_players[i].setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateTrainerPos(){
        int[][] currentState = gameManager.getCurrentState();
        for (int i = 0; i < currentState[0].length; i++) {
            if(currentState[0][i] == GameManager.OBSTACLE_CODE)
                game_IMG_trainers[i].setVisibility(View.VISIBLE);
            else if(currentState[0][i] == GameManager.EMPTY_CODE)
                game_IMG_trainers[i].setVisibility(View.INVISIBLE);
        }
    }

    private void toast() {
        if (toaster != null)
            toaster.cancel();
        String name = "Ouch!";
        toaster = Toast
                .makeText(this, name,Toast.LENGTH_SHORT);
        toaster.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
    }

    //    private void OpenScoreScreen() {
//        Intent scoreIntent = new Intent(this, ScoreActivity.class);
//        scoreIntent.putExtra(ScoreActivity.KEY_SCORE, score);
//        scoreIntent.putExtra(ScoreActivity.KEY_STATUS, status);
//        startActivity(scoreIntent);
//        finish();
//    }




}