package com.example.quadrominotest;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        difficulty = prefs.getString("difficulty_preference", "Normal");
        NUM_ROWS = Integer.parseInt(prefs.getString("num_rows_preference", "20")) + 6;
        NUM_COLUMNS = Integer.parseInt(prefs.getString("num_columns_preference", "10")) + 6;
        speed = prefs.getString("speed_preference", "Normal");
        switch (speed) {
            case "Slow": {
                SPEED_NORMAL = 1000;
                SPEED_FAST = 100;
                break;
            }
            case "Normal": {
                SPEED_NORMAL = 500;
                SPEED_FAST = 50;
                break;
            }
            case "Fast": {
                SPEED_NORMAL = 250;
                SPEED_FAST = 25;
                break;
            }
        }

        TextView textView = (TextView) findViewById(R.id.game_over_textview);
        textView.setVisibility(View.INVISIBLE);
        TextView textView2 = (TextView) findViewById(R.id.game_over_textview2);
        textView2.setVisibility(View.INVISIBLE);

        bitmap = Bitmap.createBitmap(BOARD_WIDTH, BOARD_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        linearLayout = (LinearLayout) findViewById(R.id.game_board);
        score = 0;
        currentShapeAlive = false;

        gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);

        ShapesInit();

        GameInit();
    }
}
