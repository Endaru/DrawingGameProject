package com.example.ellilim.drawinggameproject.drawingGame.gameComponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public abstract class monsterObject {
    public int capturePoints;
    public String monsterName;
    public int monsterID;
    public int monsterWidth;
    public int monsterHeight;

    public Paint mPaint;

    public monsterObject(int points,String name,int id, int width, int height){
        capturePoints = points;
        monsterName = name;
        monsterID = id;
        monsterHeight = height;
        monsterWidth = width;

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(3);
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();
}
