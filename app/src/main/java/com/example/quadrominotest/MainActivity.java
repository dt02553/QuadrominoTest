package com.example.quadrominotest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (SaveSettings.saveSettingsApp == 0) {
            SaveSettings.saveSettingsApp = 1;
            prefs.edit().putString("difficulty_preference", "Normal").apply();
            prefs.edit().putString("num_rows_preference", "20").apply();
            prefs.edit().putString("num_columns_preference", "10").apply();
            prefs.edit().putString("speed_preference", "Normal").apply();
        }

        SharedPreferences settings = getSharedPreferences("Preferences", 0);

        final Button newGameButton = (Button) findViewById(R.id.new_game_button);


        Button leaderboardButton = (Button) findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        SaveSettings.saveSettingsLeaderboard = 0;
                        MainActivity.this.startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
                    }
                });

        Button infoButton = (Button) findViewById(R.id.info_button);
        infoButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, InfoActivity.class));
                    }
                });

        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                });

        EditText editText = (EditText) findViewById(R.id.nameEditText);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences("Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        //editor.putString("playerName", playerName);
       //editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
