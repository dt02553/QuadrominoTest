package com.example.quadrominotest;


import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;


public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int backgroundColor = Color.parseColor("#c0c0c0");
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(backgroundColor);
        addPreferencesFromResource(R.xml.preferences);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true); // !?!?

    }
}
