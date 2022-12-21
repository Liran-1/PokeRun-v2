package com.example.pokerun_2.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class gameSP {

        private static final String DB_FILE = "DB_FILE";

        private static gameSP instance = null;
        private SharedPreferences preferences;

        private gameSP(Context context){
            preferences = context.getSharedPreferences(DB_FILE,Context.MODE_PRIVATE);
        }

        public static void init(Context context){
            if (instance == null)
                instance = new gameSP(context);
        }

        public static gameSP getInstance() {
            return instance;
        }

        public void putInt(String key, int value) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }

        public int getInt(String key, int defValue) {
            return preferences.getInt(key, defValue);
        }

        public void putString(String key, String value) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public String getString(String key, String defValue) {
            return preferences.getString(key, defValue);
        }

}
