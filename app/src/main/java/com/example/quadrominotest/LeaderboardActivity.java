package com.example.quadrominotest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class LeaderboardActivity extends Activity {

    ListView list;

    void PopulateListView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mMap;

        String[] projection = {
                LeaderboardDB.KEY_NAME,
                LeaderboardDB.KEY_SCORE};

        String selection = LeaderboardDB.KEY_DIFFICULTY + "=?" + " and "
                + LeaderboardDB.KEY_NUMROWS + "=?" + " and "
                + LeaderboardDB.KEY_NUMCOLUMNS + "=?" + " and "
                + LeaderboardDB.KEY_SPEED + " =?";

        String[] selectionArgs = {
                prefs.getString("difficulty_preference_highscores", "Normal"),
                prefs.getString("num_rows_preference_highscores", "20"),
                prefs.getString("num_columns_preference_highscores", "10"),
                prefs.getString("speed_preference_highscores", "Normal")
        };

        String sortOrder = LeaderboardDB.KEY_SCORE + " DESC";

        Cursor cursor = getContentResolver().query(LeaderboardContentProvider.CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        if (cursor == null || cursor.getCount() == 0) {
            mMap = new HashMap<String, String>();
            mMap.put("ID", "1");
            mMap.put("Name", "N/A");
            mMap.put("Score", "N/A");
            mList.add(mMap);
        } else {
            int id = 0;
            cursor.moveToFirst();
            do {
                id++;
                mMap = new HashMap<String, String>();
                mMap.put("ID", Integer.toString(id));
                mMap.put("Name", cursor.getString(cursor.getColumnIndexOrThrow(LeaderboardDB.KEY_NAME)));
                mMap.put("Score", Integer.toString(cursor.getInt(cursor.getColumnIndexOrThrow(LeaderboardDB.KEY_SCORE))));
                mList.add(mMap);
            }
            while (cursor.moveToNext());
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, mList, R.layout.rows_leaderboard, new String[]{"ID", "Name", "Score"}, new int[]{R.id.ID_CELL, R.id.NAME_CELL, R.id.SCORE_CELL});
        list.setAdapter(mSimpleAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        list = (ListView) findViewById(R.id.listView_leaderboard);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (SaveSettings.saveSettingsLeaderboard == 0) {
            SaveSettings.saveSettingsLeaderboard = 1;
            prefs.edit().putString("difficulty_preference_highscores", "Normal").apply();
            prefs.edit().putString("num_rows_preference_highscores", "20").apply();
            prefs.edit().putString("num_columns_preference_highscores", "10").apply();
            prefs.edit().putString("speed_preference_highscores", "Normal").apply();
        }
        PopulateListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PopulateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PopulateListView();
    }
}
