package com.example.ellilim.drawinggameproject.drawingGame.gameComponents;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

//TODO 1.0. : Add endpoint to start of path, when user touches it circle is complete
//TODO 2.0. : Create a filled circle
//TODO 3.0. : Check if another object is inside of the CaptureLine

/*
*#CaptureLine
*CaptureLine is an extension of the Path class, this way its possible
*to add the logic we need to the class itself instead of throwing it all in the GameView.
*/
public class CaptureLine extends Path {

    private static final float TOLERANCE = 5;
    //These Points are used to determine if the #CaptureLine hit itself.
    protected ArrayList<Point> mPoints = new ArrayList<>();
    private float mX,mY;
    public Paint mPaint;
    private boolean mBounds = true;

    //If no paint is given then #CaptureLine is basic
    public CaptureLine(){
        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(20f);
    }

    //Possibility to change the #CaptureLine
    public CaptureLine(Paint paint){
        mPaint = paint;
    }

    public ArrayList<Point> returnPoints(){
        return mPoints;
    }

    //Start movement of the #CaptureLine
    public void captureLineStart(float x, float y){
        mBounds = false;
        mPoints.clear();
        mPoints.add(new Point((int)x,(int)y));
        reset();
        moveTo(x, y);
        mX = x;
        mY = y;
    }

    //Move the #CaptureLine
    public void captureLineMove(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        mPoints.add(new Point((int)x,(int)y));

        if(dx >= TOLERANCE || dy >= TOLERANCE) {
            quadTo(mX,mY,(x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    //If the finger is lifted from the screen
    public void captureLineUp(){
        lineTo(mX,mY);
    }

    //Reset the #CaptureLine
    public void resetCaptureLine(){
        mBounds = false;
        mPoints.clear();
        reset();
        moveTo(mX, mY);
    }

    public void fillCaptureLine(){
        mBounds = true;
    }

    //Here we check if the path we are drawing is complex, complex being short for hitting itself
    public Boolean checkCaptureLineHitbox(){
        if ((mPoints == null || mPoints.size() <= 3) || mBounds) {
            return false;
        }

        int length = mPoints.size();

        for (int i = 1; i < length; i++) {
            Point lineAStart = mPoints.get(i - 1);
            Point lineAEnd = mPoints.get(i);

            for (int j = i + 2; j < length; j++) {
                Point lineBStart = mPoints.get(j - 1);
                Point lineBEnd = mPoints.get(j);
                if (lineSegmentsIntersect(lineAStart.x,lineAStart.y,
                        lineAEnd.x,lineAEnd.y,
                        lineBStart.x,lineBStart.y,
                        lineBEnd.x,lineBEnd.y)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Check if a line intersects with another
    static private Boolean lineSegmentsIntersect(float p0_x, float p0_y, float p1_x, float p1_y, float p2_x, float p2_y, float p3_x, float p3_y) {
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
        s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

        float s, t;
        s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            return true;
        }

        return false;
    }
}
