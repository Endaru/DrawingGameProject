package com.example.ellilim.drawinggameproject.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.drawingGame.GameView;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

public class GameActivity extends McaptureGameActivity {

    private GameView mGameView;
    private LinearLayout mSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mSurface =(LinearLayout) findViewById(R.id.surface);
        mGameView = new GameView(this, this);
        mSurface.addView(mGameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameView.resume();
    }

    @Override
    public void gameFinished(boolean gameWon, monsterObject monster) {
        Log.i("INFORMATION","" + monster);
        finish();
    }

    @Override
    public boolean lineHit() {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
