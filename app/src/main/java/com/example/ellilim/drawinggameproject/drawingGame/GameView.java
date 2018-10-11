package com.example.ellilim.drawinggameproject.drawingGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.CaptureLine;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterData.smallMonster;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    private boolean mRunning;
    private Thread mGameThread = null;
    private CaptureLine mCaptureLine;
    private SurfaceHolder mSurfaceHolder;
    private monsterObject mMonsterObject;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSurfaceHolder = getHolder();
        mCaptureLine = new CaptureLine();
        mMonsterObject = new smallMonster(5,"Piko",0,150,150,1000,500);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Bitmap mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    }

    public void run() {
        Canvas canvas;
        while (mRunning) {

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
                    canvas.drawColor(Color.WHITE);
                    updateFrame(canvas);
                    if (mCaptureLine != null) {
                        canvas.drawPath(mCaptureLine, mCaptureLine.mPaint);
                    }

                    if(mMonsterObject != null){
                        mMonsterObject.draw(canvas);
                    }

                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void updateFrame(Canvas canvas) {
        if(canvas != null){
            mMonsterObject.update(canvas);
        }
        if(mCaptureLine.checkCaptureLineHitbox()){
            mCaptureLine.resetCaptureLine();
        }
        if(mMonsterObject.checkCollisionWithCaptureLine(mCaptureLine.returnPoints())){
            mCaptureLine.resetCaptureLine();
        }
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
        mCaptureLine.captureLineStart(x,y);
    }

    private void moveTouch(float x, float y){
        mCaptureLine.captureLineMove(x,y);
    }

    private void upTouch(){
        mCaptureLine.captureLineUp();
    }
}
