package com.example.pokerun_2.Manager;

import com.example.pokerun_2.UserHighScore;

import java.util.Comparator;

public class UserComparator implements Comparator<UserHighScore> {

    @Override
    public int compare(UserHighScore uHS1, UserHighScore uHS2) {
        return uHS1.compareTo(uHS2);
    }
}
