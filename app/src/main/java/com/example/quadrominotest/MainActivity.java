package com.example.quadrominotest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    final Context c = this;
    public static String username;

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
        username = settings.getString("playerName", "");

        final Button newGameButton = (Button) findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                        View mView = layoutInflaterAndroid.inflate(R.layout.username_input_dialog, null);
                        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                        alertDialogBuilderUserInput.setView(mView);

                        final EditText usernameInput = (EditText) mView.findViewById(R.id.userInputDialog);
                        alertDialogBuilderUserInput.setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        username = usernameInput.getText().toString().trim();
                                        MainActivity.this.startActivity(new Intent(MainActivity.this, GameActivity.class));
                                    }
                                })

                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogBox, int id) {
                                                dialogBox.cancel();
                                                MainActivity.this.startActivity(new Intent(MainActivity.this, GameActivity.class));
                                            }
                                        });

                        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.show();
                    }
                });

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
        editor.putString("username", username);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
