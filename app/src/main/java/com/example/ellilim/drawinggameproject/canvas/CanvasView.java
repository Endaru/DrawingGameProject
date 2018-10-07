package com.example.ellilim.drawinggameproject.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context mContext;
    private Paint mPaint;
    private float mX;
    private float mY;
    private static final float TOLERANCE = 5;
    ArrayList<Point> mPoints = new ArrayList<>();

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
        mPaint.setStrokeWidth(20f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    private void startTouch(float x, float y){
        mPoints.add(new Point((int)x,(int)y));
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        mPoints.add(new Point((int)x,(int)y));

        if(dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX,mY,(x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas(){
        mPath.reset();
        invalidate();
    }

    private void upTouch(){
        mPath.lineTo(mX,mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }

    static Boolean isPathComplex(List<Point> path) {

        if (path == null || path.size() <= 2) {
            return false;
        }

        int len = path.size();

        for (int i = 1; i < len; i++) {
            Point lineAStart = path.get(i - 1);
            Point lineAEnd = path.get(i);

            for (int j = i + 1; j < len; j++) {
                Point lineBStart = path.get(j - 1);
                Point lineBEnd = path.get(j);
                if (lineSegmentsDoIntersect(lineAStart.x,lineAStart.y,
                        lineAEnd.x,lineAEnd.y,
                        lineBStart.x,lineBStart.y,
                        lineBEnd.x,lineBEnd.y)) {
                    return true;
                }

            } // inner loop

        } // outer loop

        return false;
    }

    static boolean lineSegmentsDoIntersect(
            float Ax, float Ay
            , float Bx, float By
            , float Cx, float Cy
            , float Dx, float Dy) {

        // two line segments: AB and CD
        // segment AB intersects segment CD
        // if  A and B on different sides of line through C and D
        // AND C and D on different sides of line through A and B

        // note the difference between line and  segment!

        if ( ! pointsOnDifferentSidesOfLineThrough(Ax, Ay, Bx, By, Cx, Cy, Dx, Dy) )
            return false;

        if ( ! pointsOnDifferentSidesOfLineThrough(Cx, Cy, Dx, Dy, Ax, Ay, Bx, By) )
            return false;

        return true;
    }

    static boolean pointsOnDifferentSidesOfLineThrough(
            float Ax, float Ay
            , float Bx, float By
            , float x1, float y1
            , float x2, float y2) {

        // determine equation of line through C and D

        // y = ax + b
        // a = (y2-y1)/(x2-x1)   but.. ( x2-x1) not equal to zero
        // y-y1 = a (x-x1)
        // y = a (x-x1) + y1
        // y = ax -ax1 + y1
        // b = -ax1 + y1

        //but.. (x2-x1) not 0

        if ( x2==x1)
        {
            if ( Ax > x1 && Bx > x1 )
                return false;
            if ( Ax < x1 && Bx < x1 )
                return false;

            return true;
        }

        float a = (y2-y1)/(x2-x1);
        float b = -a * x1 + y1;

        float yA = a * Ax + b;
        float yB = a * Bx + b;

        if ( yA > Ay && yB > By )
            return false;
        if ( yA < Ay && yB < By )
            return false;

        return true;
    }

}
