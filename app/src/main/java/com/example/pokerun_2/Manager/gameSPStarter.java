package com.example.pokerun_2.Manager;

import android.app.Application;

public class gameSPStarter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        gameSP.init(this);
    }
}
