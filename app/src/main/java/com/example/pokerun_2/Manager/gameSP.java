package com.example.pokerun_2.Manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.pokerun_2.UserHighScore;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class gameSP {

    private static final String DB_FILE = "DB_FILE";
    private static final String USER_RECORDS = "USER_RECORDS";

    private static gameSP instance = null;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private gameSP(Context context) {
        preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null)
            instance = new gameSP(context);
    }

    public static gameSP getInstance() {
        return instance;
    }

    public ArrayList<UserHighScore> getHighScores() {

        TypeToken<List<UserHighScore>> listType = new TypeToken<List<UserHighScore>>() {};
        Gson gson = new Gson();
        String gameData = preferences.getString(DB_FILE, "");
        ArrayList<UserHighScore> userHighScores  = gson.fromJson(gameData, listType.getType());
        return userHighScores;
    }

    public void setHighScores(ArrayList<UserHighScore> userHighScores) {
        ArrayList<UserHighScore> highScores = userHighScores;
        Gson gson = new Gson();
        if (userHighScores.size() > 10)
            for (int i = 0; i < 10; i++)
                highScores.add(userHighScores.get(i));
        String json = gson.toJson(highScores);
        editor = preferences.edit();
        editor.putString(DB_FILE, json);
        editor.commit();

    }

    public void putInt(String key, int value) {
        editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

}
