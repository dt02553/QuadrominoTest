package com.example.quadrominotest;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity {

    int NUM_ROWS = 26;
    int NUM_COLUMNS = 16;
    final int BOARD_HEIGHT = 800;
    final int BOARD_WIDTH = 400;
    final Handler handler = new Handler();
    final Shape[] shapes = new Shape[11];
    final int RIGHT_DIRECTION = 1;
    final int DOWN_DIRECTION = 2;
    final int LEFT_DIRECTION = 3;
    int SPEED_NORMAL = 500;
    int SPEED_FAST = 50;
    String difficulty, speed;
    int score;
    boolean gameInProgress, gamePaused, fastSpeedState, currentShapeAlive;
    final int dx[] = {-1, 0, 1, 0};
    final int dy[] = {0, 1, 0, -1};
    private GestureDetectorCompat gestureDetector;
    Random random = new Random();
    BoardCell[][] gameMatrix;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    LinearLayout linearLayout;
    Shape currentShape;

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

    @Override
    protected void onStop() {
        super.onStop();
        if (gameInProgress) {
            gamePaused = true;
            PaintMatrix();
        }
    }

    private void ShapesInit() {
        int[][] a = new int[5][5];

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                a[i][j] = 0;
            }
        }

        a[1][2] = a[1][3] = a[2][3] = a[3][3] = 1;
        shapes[0] = new Shape(a, Color.rgb(255, 165, 0), BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[1][3] = a[2][3] = a[3][3] = 0;

        a[2][1] = a[2][2] = a[3][2] = a[3][3] = 1;
        shapes[1] = new Shape(a, Color.RED, BoardCell.BEHAVIOR_IS_FALLING);
        a[2][1] = a[2][2] = a[3][2] = a[3][3] = 0;

        a[1][2] = a[2][2] = a[3][2] = a[4][2] = 1;
        shapes[2] = new Shape(a, Color.CYAN, BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[2][2] = a[3][2] = a[4][2] = 0;

        a[2][2] = a[2][3] = a[3][2] = a[3][3] = 1;
        shapes[3] = new Shape(a, Color.YELLOW, BoardCell.BEHAVIOR_IS_FALLING, false);
        a[2][2] = a[2][3] = a[3][2] = a[3][3] = 0;

        a[1][2] = a[2][2] = a[2][3] = a[3][2] = 1;
        shapes[4] = new Shape(a, Color.rgb(139, 0, 139), BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[2][2] = a[2][3] = a[3][2] = 0;

        a[1][2] = a[2][2] = a[2][3] = a[3][3] = 1;
        shapes[5] = new Shape(a, Color.rgb(0, 255, 0), BoardCell.BEHAVIOR_IS_FALLING);
        a[1][2] = a[2][2] = a[2][3] = a[3][3] = 0;

        a[1][3] = a[2][3] = a[3][2] = a[3][3] = 1;
        shapes[6] = new Shape(a, Color.BLUE, BoardCell.BEHAVIOR_IS_FALLING);
        a[1][3] = a[2][3] = a[3][2] = a[3][3] = 0;

        a[2][2] = 1;
        shapes[7] = new Shape(a, Color.WHITE, BoardCell.BEHAVIOR_IS_FALLING, false);
        a[2][2] = 0;

        a[1][2] = a[2][1] = a[2][2] = a[2][3] = a[3][2] = 1;
        shapes[8] = new Shape(a, Color.GRAY, BoardCell.BEHAVIOR_IS_FALLING, false);
        a[1][2] = a[2][1] = a[2][2] = a[2][3] = a[3][2] = 0;

        for (int i = 1; i <= 4; ++i) for (int j = 1; j <= 4; ++j) a[i][j] = 1;
        shapes[9] = new Shape(a, Color.rgb(117, 101, 57), BoardCell.BEHAVIOR_IS_FALLING, false);
        for (int i = 1; i <= 4; ++i) for (int j = 1; j <= 4; ++j) a[i][j] = 0;

        for (int i = 1; i <= 4; ++i) for (int j = 1; j <= 4; ++j) a[i][j] = 1;
        a[1][2] = a[1][3] = a[4][2] = a[4][3] = 0;
        shapes[10] = new Shape(a, Color.rgb(128, 158, 73), BoardCell.BEHAVIOR_IS_FALLING);
        for (int i = 1; i <= 4; ++i) for (int j = 1; j <= 4; ++j) a[i][j] = 0;
    }

    private void CopyMatrix(BoardCell[][] A, BoardCell[][] B) {
        for (int i = 0; i < NUM_ROWS; ++i) {
            for (int j = 0; j < NUM_COLUMNS; ++j) {
                B[i][j] = new BoardCell(A[i][j].getState(), A[i][j].getColor(), A[i][j].getBehavior());
            }
        }
    }

    private void FixGameMatrix() {
        for (int i = 3; i < NUM_ROWS - 3; ++i) {
            for (int j = 3; j < NUM_COLUMNS - 3; ++j) {
                if (gameMatrix[i][j].getState() == 0) {
                    gameMatrix[i][j].setColor(Color.BLACK);
                    gameMatrix[i][j].setBehavior(BoardCell.BEHAVIOR_NOTHING);
                    continue;
                }
                if (gameMatrix[i][j].getBehavior() == BoardCell.BEHAVIOR_IS_FIXED)
                    continue;
                if (gameMatrix[i][j].getBehavior() == BoardCell.BEHAVIOR_IS_FALLING) {
                    int ind, jnd, ii, jj;
                    for (ind = 1, ii = currentShape.x; ind <= 4; ++ind, ++ii) {
                        for (jnd = 1, jj = currentShape.y; jnd <= 4; ++jnd, ++jj) {
                            if (ii == i && jj == j) {
                                if (currentShape.mat[ind][jnd].getState() == 0) {
                                    gameMatrix[i][j] = new BoardCell();
                                }
                            }
                        }
                    }
                    continue;
                }
                if (gameMatrix[i][j].getBehavior() == BoardCell.BEHAVIOR_NOTHING) {
                    int ind, jnd, ii, jj;
                    for (ind = 1, ii = currentShape.x; ind <= 4; ++ind, ++ii) {
                        for (jnd = 1, jj = currentShape.y; jnd <= 4; ++jnd, ++jj) {
                            if (ii == i && jj == j) {
                                if (currentShape.mat[ind][jnd].getState() == 1) {
                                    gameMatrix[i][j] = currentShape.mat[ind][jnd];
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
