package com.example.pokerun_2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pokerun_2.Manager.BackgroundSound;
import com.example.pokerun_2.Manager.GameManager;
import com.example.pokerun_2.Manager.gameSP;
import com.example.pokerun_2.R;
import com.example.pokerun_2.StepCallback;
import com.example.pokerun_2.Manager.TiltDetector;
import com.example.pokerun_2.UserHighScore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import im.delight.android.location.SimpleLocation;

public class BigGameActivity extends AppCompatActivity {

    public static final String BUTTON_STATUS = "BUTTON_STATUS";
    public static final String SPEED_STATUS = "SPEED_STATUS";
    public static final String USER_NAME = "USER_NAME";

    private boolean buttonStatus, speedStatus;

    private FloatingActionButton game_FAB_left;
    private FloatingActionButton game_FAB_right;

    private MaterialTextView game_LBL_score;

    private ArrayList<LinearLayout> game_LLO_lanes;
    private ArrayList<ShapeableImageView> game_IMG_objects;
    private ArrayList<ShapeableImageView> game_IMG_trainers;
    private ArrayList<ShapeableImageView> game_IMG_lives;
    private ArrayList<ShapeableImageView> game_IMG_players;

    private GameManager gameManager;

    private TiltDetector tiltDetector;
    private Timer timer;
    private BackgroundSound mBackgroundSound;
    private Toast toaster;
    private Vibrator v;

    private long startTime = System.currentTimeMillis();
    private long currentTime = System.currentTimeMillis();

    private final int SLOW_SPEED = 750;
    private final int FAST_SPEED = 550;
    private final int VERY_FAST_SPEED = 400;
    private final String SLOW_SPEED_STR = "SLOW_SPEED_STR";
    private final String FAST_SPEED_STR = "FAST_SPEED_STR";
    private final String VERY_FAST_SPEED_STR = "VERY_FAST_SPEED_STR";
    private int delay = SLOW_SPEED;
    private String delayStatus = "";
    private String userName;
    private int score = 0;

    private boolean gameOver = false;
    private SimpleLocation simpleLocation;

    private void setDelay(String speed) {
        if (!buttonStatus) {
            if(!delayStatus.equals(speed))
                switch (speed) {
                    case SLOW_SPEED_STR:
                        delay = SLOW_SPEED;
                        delayStatus = SLOW_SPEED_STR;
                        timer.cancel();
                        startGame();
                        break;
                    case FAST_SPEED_STR:
                        delay = FAST_SPEED;
                        delayStatus = FAST_SPEED_STR;
                        timer.cancel();
                        startGame();
                        break;
                    case VERY_FAST_SPEED_STR:
                        delay = VERY_FAST_SPEED;
                        delayStatus = VERY_FAST_SPEED_STR;
                        timer.cancel();
                        startGame();
                        break;
                }
        } else {
            if (speedStatus) {
                delay = FAST_SPEED;
            } else {
                delay = SLOW_SPEED;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_game);

        findViews();
        gameManager = new GameManager(game_IMG_lives.size());

        Intent previousIntent = getIntent();
        buttonStatus = previousIntent.getBooleanExtra(BUTTON_STATUS, true);
        speedStatus = previousIntent.getBooleanExtra(SPEED_STATUS, false);
        userName = previousIntent.getStringExtra(USER_NAME);

        simpleLocation =  gameManager.initLocation(this);
        initViews();
        refreshUI();                                //initial setup (hides unrelevent views)
        startGame();
        setDelay(SLOW_SPEED_STR);
    }


    //////SET GAME//////

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

        game_IMG_objects = new ArrayList<>();
        int rows = game_LLO_lanes.size(), cols = game_LLO_lanes.get(0).getChildCount();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                game_IMG_objects.add((ShapeableImageView) game_LLO_lanes.get(i).getChildAt(j));
            }
        }
    }

    private void initViews() {
        if (!buttonStatus) {
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

    private void startGame() {
        currentTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if(!gameOver) {
                            runOnUiThread(() -> BigGameActivity.this.refreshUI());
                        }
                    }
                }
                , delay, delay);
    }

    private void changeButtonVisibility() {
        if (!buttonStatus) {
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

            @Override
            public void speedSlow() {
                actionSpeed(SLOW_SPEED_STR);
            }

            @Override
            public void speedFast() {
                actionSpeed(FAST_SPEED_STR);
            }

            @Override
            public void speedVeryFast() {
                actionSpeed(VERY_FAST_SPEED_STR);
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

    private void actionSpeed(String speed) {
        setDelay(speed);
    }

    // method to check for permissions
    private boolean checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        else
// If we want background location
// on Android 10.0 and higher,
// use:
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    //////UPDATE VISUALS//////

    private void refreshUI() {
        gameManager.setNextState();
        updatePlayerPos();
        updateObjPos();
        updateTrainerPos();
        updateScore();
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (gameManager.checkHit(v)) {
            toast(GameManager.OBSTACLE_CODE);
        }
        gameManager.setHit(false);
        if (gameManager.isLose(v)) {
            gameOver = true;
            checkHighScore();
            openScoreScreen(userName, gameManager.getScore());
        } else if (gameManager.getHits() != 0)
            game_IMG_lives.get(game_IMG_lives.size() - gameManager.getHits()).setVisibility(View.INVISIBLE);

    }

    private void checkHighScore() {
        float[] coordinates = {(float) simpleLocation.getLatitude(), (float) simpleLocation.getLongitude()};
        UserHighScore currentUser = new UserHighScore(userName, score, coordinates);
        ArrayList<UserHighScore> userHighScores = gameSP.getInstance().getHighScores();
        if (userHighScores != null) {
            for (UserHighScore user : userHighScores) {
                if (currentUser.getScore() > user.getScore())
                    userHighScores.set(user.getPlace(), currentUser);
            }
        } else {
            userHighScores = new ArrayList<>();
            userHighScores.add(currentUser);
        }
        gameSP.getInstance().setHighScores(userHighScores);
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

    private void updateObjPos() {                   // update object position
        int[][] currentState = gameManager.getCurrentState();
        int rows = currentState.length, cols = currentState[0].length;
        for (int i = 0; i < rows - 1; i++) {
            for (int j = 0; j < cols; j++) {
                if (currentState[i][j] == GameManager.OBSTACLE_CODE) {
//                    game_IMG_obstacles[i * cols + j];
                    game_IMG_objects.get(i * cols + j).setVisibility(View.VISIBLE);
                    game_IMG_objects.get(i * cols + j).setImageResource(R.drawable.pokeball);
                } else if (currentState[i][j] == GameManager.EMPTY_CODE) {
                    game_IMG_objects.get(i * cols + j).setVisibility(View.INVISIBLE);
                } else if (currentState[i][j] == GameManager.SCORE_CODE) {
                    game_IMG_objects.get(i * cols + j).setVisibility(View.VISIBLE);
                    game_IMG_objects.get(i * cols + j).setImageResource(R.drawable.pokecoin);
                }
            }
        }
    }

    private void updateTrainerPos() {
        int[][] currentState = gameManager.getCurrentState();
        for (int i = 0; i < currentState[0].length; i++) {
            if (currentState[0][i] == GameManager.OBSTACLE_CODE)
                game_IMG_trainers.get(i).setVisibility(View.VISIBLE);
            else
                game_IMG_trainers.get(i).setVisibility(View.INVISIBLE);
        }
    }

    private void updateScore() {
        long timeScore = (System.currentTimeMillis() - currentTime);
        currentTime = System.currentTimeMillis();
        timeScore /= delay;                                   // score over time
        gameManager.addPointsToScore((int)timeScore);
        score = gameManager.getScore();
        game_LBL_score.setText("" + score);
    }

    private void toast(int hitCode) {
        if (toaster != null)
            toaster.cancel();
        String message = "";
        if (hitCode == GameManager.OBSTACLE_CODE)
            message = "Ouch!";
        else if (hitCode == GameManager.SCORE_CODE)
            message = "Money!";
        toaster = Toast
                .makeText(this, message, Toast.LENGTH_SHORT);
        toaster.show();
    }


    //////CHANGE SCREENS//////
    private void openScoreScreen(String userName, int score) {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        scoreIntent.putExtra(ScoreActivity.KEY_SCORE, score);
        scoreIntent.putExtra(ScoreActivity.KEY_USERNAME, userName);
        scoreIntent.putExtra(ScoreActivity.KEY_LATITUDE, 0);
        scoreIntent.putExtra(ScoreActivity.KEY_LONGITUDE, 0);
        startActivity(scoreIntent);
        finish();
    }


    //////ON PHONE ACTION//////
    @Override
    protected void onStop() {
        gameOver = true;
        timer.cancel();
        v.cancel();
        toaster.cancel();
        mBackgroundSound.cancel(true);
        if (tiltDetector != null)
            tiltDetector.stop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        gameOver = true;
        timer.cancel();
        v.cancel();
        toaster.cancel();
        mBackgroundSound.cancel(true);
        if (tiltDetector != null)
            tiltDetector.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        gameOver = false;
        mBackgroundSound = new BackgroundSound(this);
        mBackgroundSound.execute();
        if (tiltDetector != null)
            tiltDetector.start();
        startGame();
        super.onResume();
    }

}