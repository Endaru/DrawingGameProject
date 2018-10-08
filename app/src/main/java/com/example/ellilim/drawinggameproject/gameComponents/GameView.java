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
import com.example.ellilim.drawinggameproject.monsterComponents.MonsterClass;
import com.example.ellilim.drawinggameproject.monsterComponents.monsters.TestMonster;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private boolean mRunning;
    private Thread mGameThread = null;
    private CaptureLine mCaptureLine;
    private MonsterClass mTestMonster;

    private Context mContext;
    private Paint mPaint;
    private Paint monsterPaint;
    private Bitmap mBitmap;
    private int mBitmapX,mBitmapY,mViewWidth,mViewHeight;
    private SurfaceHolder mSurfaceHolder;
    private float mX,mY;
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

        monsterPaint = new Paint();
        monsterPaint.setColor(Color.RED);
        monsterPaint.setStyle(Paint.Style.STROKE);
        monsterPaint.setStrokeJoin(Paint.Join.ROUND);
        monsterPaint.setStrokeWidth(20f);

        mCaptureLine = new CaptureLine();

        mTestMonster = new TestMonster("test",1,0,300,300);
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
                // lockHardwareCanvas().
                // See https://developer.android.com/reference/android/view/
                // SurfaceHolder.html#lockHardwareCanvas()
                canvas = mSurfaceHolder.lockCanvas();
                // Fill the canvas with white and draw the bitmap.
                if(canvas != null) {
                    canvas.clipPath(mTestMonster,Region.Op.DIFFERENCE);
                    canvas.drawColor(Color.WHITE);

                    if (mCaptureLine != null) {

                        canvas.drawPath(mCaptureLine, mPaint);
                    }

                    if (mTestMonster != null){
                        canvas.drawRect(300, 300, 300, 300, monsterPaint);
                    }

                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void updateFrame() {
        if(mCaptureLine.update()){
            clearCanvas();
        }
    }

    public void clearCanvas(){
        mCaptureLine.mPoints.clear();
        mCaptureLine.reset();
        mCaptureLine.moveTo(mX, mY);
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
        mCaptureLine.mPoints.add(new Point((int)x,(int)y));
        mCaptureLine.reset();
        mCaptureLine.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        mCaptureLine.mPoints.add(new Point((int)x,(int)y));

        if(dx >= TOLERANCE || dy >= TOLERANCE) {
            mCaptureLine.quadTo(mX,mY,(x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void upTouch(){
        mCaptureLine.lineTo(mX,mY);
    }
}
