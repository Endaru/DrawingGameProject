package com.example.ellilim.drawinggameproject.drawingGame.gameComponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import java.util.ArrayList;

//Generic monsterObject
public abstract class monsterObject {
    public int capturePoints;
    public String monsterName;
    public int monsterID;
    protected int monsterWidth;
    protected int monsterHeight;
    protected int mY;
    protected int mX;

    protected Paint mPaint;
    protected Rect mRectangle;
    protected int randomMove;
    protected int ammountOfRandom;
    protected int zero;

    //Create a monsterObject
    public monsterObject(int points,String name,int id, int width, int height, int y, int x){
        capturePoints = points;
        monsterName = name;
        monsterID = id;
        monsterHeight = height;
        monsterWidth = width;
        mY = y;
        mX = x;

        mPaint = new Paint();
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(3);

        mRectangle = new Rect(mX,mY,(mX + monsterWidth),(mY + monsterHeight));
    }
    protected boolean checkBounds(Canvas canvas){
        int cx0 = 50;
        int cx1 = canvas.getWidth() - 50;
        int cy0 = 50;
        int cy1 = canvas.getHeight() - 50;

        int rx0 = mX;
        int rx1 = mX + monsterWidth;
        int ry0 = mY;
        int ry1 = mY + monsterHeight;

        return (rx0 >= cx0 && rx1 <  cx1 && ry0 >= cy0 && ry1 <  cy1);
    }

    //Abstracts that need to be used in a child
    public abstract void draw(Canvas canvas);
    public abstract Boolean checkCollisionWithCaptureLine(ArrayList<Point> points);
    public abstract void update(Canvas canvas);
    public abstract void movement(Canvas canvas);
    public abstract Boolean checkCapture(ArrayList<Point> points);
}
