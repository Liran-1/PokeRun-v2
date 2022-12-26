package com.example.pokerun_2.Manager;

import android.app.Application;

import com.example.pokerun_2.utils.gameSP;

public class gameSPStarter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        gameSP.init(this);
    }
}
