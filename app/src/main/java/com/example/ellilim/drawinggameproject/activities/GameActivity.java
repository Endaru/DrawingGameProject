package com.example.ellilim.drawinggameproject.activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.drawingGame.GameView;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

import org.w3c.dom.Text;

public class GameActivity extends McaptureGameActivity {

    private GameView mGameView;
    private LinearLayout mSurface;
    private ProgressBar mHealth;
    private TextView mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mHealth = (ProgressBar) findViewById(R.id.progressBar);
        mSurface =(LinearLayout) findViewById(R.id.surface);
        mMessage = (TextView) findViewById(R.id.message_text);
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
    public void gameFinished(final boolean gameWon, monsterObject monster) {
        final boolean GameWon = gameWon;
        final String monsterName = monster.monsterName;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(GameWon){
                    //Do Stuff if person wins
                    mMessage.setText("You Won");
                }else{
                    //Do Stuff if person loses
                    mMessage.setText("You Lost, " + monsterName + " ran away!!!");
                }
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

    }
}
