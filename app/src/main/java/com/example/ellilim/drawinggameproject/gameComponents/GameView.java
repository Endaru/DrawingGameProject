package com.example.ellilim.drawinggameproject.gameComponents;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.R;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private boolean mRunning;
    private Thread mGameThread = null;
    private Path mPath;

    private Context mContext;
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mBitmapX,mBitmapY,mViewWidth,mViewHeight;
    private SurfaceHolder mSurfaceHolder;
    private float mX,mY;
    private ArrayList<Point> mPoints = new ArrayList<>();
    private static final float TOLERANCE = 5;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(20f);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmapX = w;
        mBitmapY = h;
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
    }

    public void run() {
        Canvas canvas;
        while (mRunning) {

            updateFrame();
            // If we can obtain a valid drawing surface...
            if (mSurfaceHolder.getSurface().isValid()) {

                // Lock the canvas. Note that in a more complex app, with
                // more threads, you need to put this into a try/catch block
                // to make sure only one thread is drawing to the surface.
                // Starting with O, you can request a hardware surface with
                //    lockHardwareCanvas().
                // See https://developer.android.com/reference/android/view/
                //    SurfaceHolder.html#lockHardwareCanvas()
                canvas = mSurfaceHolder.lockCanvas();
                // Fill the canvas with white and draw the bitmap.
                if(canvas != null) {
                    canvas.drawColor(Color.WHITE);

                    if (mPath != null) {

                        canvas.drawPath(mPath, mPaint);
                    }

                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void updateFrame() {
        if(isPathComplex(mPoints)){
            Log.i("INFORMATION","Yes");
            clearCanvas();
        }
    }

    public void clearCanvas(){
        mPoints.clear();
        mPath.reset();
        mPath.moveTo(mX, mY);
    }

    private void setUpBitMap(){
        mBitmapX = (int) Math.floor(
                Math.random() * (mViewWidth - mBitmap.getWidth()));
        mBitmapY = (int) Math.floor(
                Math.random() * (mViewHeight - mBitmap.getHeight()));
    }

    public void pause() {
        mRunning = false;
        try {
            mGameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        mRunning = true;
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                break;
        }
        return true;
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

    private void upTouch(){
        mPath.lineTo(mX,mY);
    }

    static Boolean isPathComplex(List<Point> path) {

        if (path == null || path.size() <= 2) {
            return false;
        }

        int len = path.size();

        for (int i = 1; i < len; i++) {
            Point lineAStart = path.get(i - 1);
            Point lineAEnd = path.get(i);

            for (int j = i + 5; j < len; j++) {
                Point lineBStart = path.get(j - 1);
                Point lineBEnd = path.get(j);
                if (lineSegmentsIntersect(lineAStart.x,lineAStart.y,
                        lineAEnd.x,lineAEnd.y,
                        lineBStart.x,lineBStart.y,
                        lineBEnd.x,lineBEnd.y)) {
                    return true;
                }

            } // inner loop

        } // outer loop

        return false;
    }

    static Boolean lineSegmentsIntersect(float p0_x, float p0_y, float p1_x, float p1_y,
                                         float p2_x, float p2_y, float p3_x, float p3_y) {
        float s1_x, s1_y, s2_x, s2_y;
        s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
        s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

        float s, t;
        s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected

            return true;
        }

        return false; // No collision
    }
}
