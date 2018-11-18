package com.example.ellilim.drawinggameproject.drawingGame.gameComponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class CursorPoint {

    private Paint mPaint;
    private int mRadius;
    private float mX,mY;
    private boolean mHasBeenDrawn;

    public CursorPoint(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4.5f);

        mRadius = 65;
        mHasBeenDrawn = false;
    }

    public void createCursorPoint(Canvas canvas){
        if(checkHasBeenDrawn()){
            canvas.drawCircle(mX, mY, mRadius, mPaint);
        }
    }

    public void update(float x, float y){
        if(!checkHasBeenDrawn()){
            mX = x;
            mY = y;
            mHasBeenDrawn = true;
        }
    }

    public void resetCursorPoint(){
        mHasBeenDrawn = false;
    }

    private boolean checkHasBeenDrawn(){
        return mHasBeenDrawn;
    }

    public boolean isInsideofCircle(float xTouch, float yTouch){
        float touchRadius = (float) Math.sqrt(Math.pow(xTouch - mX, 2) + Math.pow(yTouch - mY, 2));
        return(touchRadius < mRadius);
    }
}
