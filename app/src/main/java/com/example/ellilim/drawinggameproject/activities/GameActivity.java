package com.example.ellilim.drawinggameproject.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.activities.parentActivities.McaptureGameActivity;
import com.example.ellilim.drawinggameproject.dbObjects.UserAccount;
import com.example.ellilim.drawinggameproject.drawingGame.GameView;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;
import com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions;
import com.example.ellilim.drawinggameproject.logicalComponents.EnumSuccesCodes;

public class GameActivity extends McaptureGameActivity {

    private GameView mGameView;
    private LinearLayout mSurface;
    private ProgressBar mHealth;

    private DBFunctions DBFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DBFunctions = new DBFunctions(this);
        DBFunctions.UserLoggedInCheck();

        mHealth = (ProgressBar) findViewById(R.id.progressBar);
        mSurface =(LinearLayout) findViewById(R.id.surface);
        mGameView = new GameView(this, this);
        mSurface.addView(mGameView);

        FloatingActionButton returnButton = (FloatingActionButton) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(this);

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
    public void gameFinished(final boolean gameWon, final monsterObject monster) {
        final boolean GameWon = gameWon;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(gameWon){
                    DBFunctions.getUserAccount();
                    DBFunctions.addMonster(monster);
                }
                Intent data = new Intent();
                data.putExtra("gameWon",gameWon);
                data.putExtra("monsterName",monster.monsterName);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

    @Override
    public boolean lineHit() {
        if(mHealth.getProgress() == 0){
            return true;
        }else{
            mHealth.setProgress(mHealth.getProgress() - 1);
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnButton:
                finish();
                break;
        }
    }

    @Override
    public void requestedJob(boolean JobSuccesful, EnumSuccesCodes code) { }
}
