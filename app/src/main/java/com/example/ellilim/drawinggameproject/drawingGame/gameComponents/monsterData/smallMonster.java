package com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterData;

import android.graphics.Canvas;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

//TODO 1.0. : Add Collosion to monster
//TODO 2.0. : Allow Monster to move
//TODO 3.0. : Monster can Attack CaptureLine
public class smallMonster extends monsterObject {
    public smallMonster(int points, String name, int id, int width, int height) {
        super(points, name, id, width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(monsterWidth,monsterWidth,monsterHeight,monsterHeight,mPaint);
    }

    @Override
    public void update() {

    }
}
