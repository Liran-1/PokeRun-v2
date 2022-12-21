package com.example.pokerun_2;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

public class BackgroundSound extends AsyncTask<Void, Void, Void> {
    private Context context;

    public BackgroundSound(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        MediaPlayer player = MediaPlayer.create(this.context, R.raw.lifelike);
        player.setLooping(true); // Set looping
        player.setVolume(1.0f, 1.0f);
        player.start();

        return null;
    }

}
