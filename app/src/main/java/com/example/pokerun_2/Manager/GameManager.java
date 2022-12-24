package com.example.pokerun_2.Manager;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.Arrays;
import java.util.Random;

public class GameManager {

    public static final int EMPTY_CODE      = 0;
    public static final int PLAYER_CODE     = 1;
    public static final int OBSTACLE_CODE   = 2;
    public static final int SCORE_CODE      = 3;

    public static final int SCORE_CODE_POINTS = 10;

    private int score   = 0;
    private int lives;
    private int hits    = 0;

    private final int lanes = 5;
    private final int rows  = 8;

    private int[][] currentState    = new int[rows][lanes];
    private int currentPlayerIndex  = lanes / 2;

    private int obstacleCounter = lanes - 1;        // to make sure user isn't blocked

    private boolean hit = false;

    public GameManager(int lives) {
        this.lives = lives;
        initGameMat();
    }

    private void initGameMat() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < lanes; j++)
                currentState[i][j] = EMPTY_CODE;
        currentState[rows - 1][currentPlayerIndex] = PLAYER_CODE;
    }

    public void setNextState() {
        Random r = new Random();
        moveObjectsForward();
        if(currentState[rows - 1][currentPlayerIndex] == OBSTACLE_CODE){    //obj code overrides
            setHit(true);                                                   //player code
        }
        if(currentState[rows - 1][currentPlayerIndex] == SCORE_CODE){    //obj code overrides
            addPointsToScore(SCORE_CODE_POINTS);                                                   //player code
        }

        currentState[rows - 1][currentPlayerIndex] = PLAYER_CODE;           //put player code back
        Arrays.fill(currentState[0], EMPTY_CODE);   // clean first row

        setNextRoadObject(r);
    }

    private void moveObjectsForward() {
        for (int i = rows - 1; i > 0; i--) {        // move objs forward
            for (int j = 0; j < lanes; j++) {
                currentState[i][j] = currentState[i - 1][j];
            }
        }
    }

    private void setNextRoadObject(Random r) {

        int roadObject = rollRoadObject(r);
        int chosenLane = r.nextInt(lanes);
        if (roadObject == OBSTACLE_CODE) {
            if (obstacleCounter > 0)
                currentState[0][chosenLane] = roadObject;
            obstacleCounter--;
        }
        else
            currentState[0][chosenLane] = roadObject;
        if (obstacleCounter == -1)
            obstacleCounter = lanes - 1;
    }

    private int rollRoadObject(Random r) {
        int objRoll = r.nextInt(10);
        if(objRoll > 3)
            return OBSTACLE_CODE;
        else
            return SCORE_CODE;
    }

    public int[][] getCurrentState() {
        return currentState;
    }

    public void movePlayerRight() {
        if (currentState[rows - 1][lanes - 1] == PLAYER_CODE)
            return;

        currentState[rows - 1][currentPlayerIndex] = EMPTY_CODE;
        currentState[rows - 1][currentPlayerIndex + 1] = PLAYER_CODE;
        currentPlayerIndex += 1;
    }

    public void movePlayerLeft() {
        if (currentState[rows - 1][0] == PLAYER_CODE)
            return;

        currentState[rows - 1][currentPlayerIndex] = EMPTY_CODE;
        currentState[rows - 1][currentPlayerIndex - 1] = PLAYER_CODE;
        currentPlayerIndex -= 1;
    }

    public boolean isLose(Vibrator v) {
        return lives == 0;
    }

    public boolean checkHit(Vibrator v) {
        if(isHit()){
            if (hits < lives)
                hits++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
            return true;
        }
        return false;
    }

    public int getLives() {
        return lives;
    }

    public int getRows() {
        return rows;
    }

    public int getLanes() {
        return lanes;
    }

    public int getHits() {
        return hits;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public int getScore() {
        return score;
    }

    public void addPointsToScore(int points) {
        score += points;
    }
}
