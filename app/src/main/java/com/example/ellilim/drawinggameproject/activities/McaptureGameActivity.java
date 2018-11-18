package com.example.ellilim.drawinggameproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

public abstract class McaptureGameActivity extends AppCompatActivity implements View.OnClickListener{

    public abstract void gameFinished(boolean gameWon, monsterObject monster);
    public abstract boolean lineHit();
}
