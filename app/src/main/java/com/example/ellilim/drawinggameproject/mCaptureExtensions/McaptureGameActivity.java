package com.example.ellilim.drawinggameproject.mCaptureExtensions;

import android.view.View;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

public abstract class McaptureGameActivity extends McaptureActivity implements View.OnClickListener{

    public abstract void gameFinished(boolean gameWon, monsterObject monster);
    public abstract boolean lineHit();
}
