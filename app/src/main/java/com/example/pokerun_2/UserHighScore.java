package com.example.pokerun_2;

import java.util.Arrays;

public class UserHighScore implements Comparable<UserHighScore>{

    private String name;
    private int score;
    private int place;
    private double coordinates[];

    public UserHighScore(String name, int score,  double[] coordinates) {
        this.coordinates = coordinates;
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int compareTo(UserHighScore userHighScore) {
        if(this.score < userHighScore.score)
            return 1;
        else if(this.score > userHighScore.score)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return "UserHighScore{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", place=" + place +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}
