package com.example.ellilim.drawinggameproject.drawingGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.ellilim.drawinggameproject.activities.McaptureGameActivity;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.CaptureLine;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.CursorPoint;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterData.smallMonster;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {
    private boolean mRunning;
    private Thread mGameThread = null;
    private CaptureLine mCaptureLine;
    private SurfaceHolder mSurfaceHolder;
    private monsterObject mMonsterObject;
    private CursorPoint mCursorPoint;
    private float mX,mY;
    private McaptureGameActivity mRequestedActivity;
    private boolean filledPath;

    public GameView(Context context, McaptureGameActivity activity) { this(context, activity, null); }

    public GameView(Context context, McaptureGameActivity activity, AttributeSet attrs) {
        super(context, attrs);
        mRequestedActivity = activity;
        mSurfaceHolder = getHolder();
        mCaptureLine = new CaptureLine();
        mCursorPoint = new CursorPoint();
        mMonsterObject = new smallMonster(5,"Piko",0,150,150,1000,500);
        filledPath = false;
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
            mCursorPoint.createCursorPoint(canvas);

            if(filledPath){
                if(mMonsterObject.checkCapture(mCaptureLine.returnPoints())){
                    if(mMonsterObject.capturePoints == 0){
                        mRunning = false;
                        mRequestedActivity.gameFinished(true,mMonsterObject);
                    }else{
                        mMonsterObject.capturePoints--;
                        Log.i("INFORMATION:", "" + mMonsterObject.capturePoints);
                        mCaptureLine.resetCaptureLine();
                        mCursorPoint.resetCursorPoint();
                        filledPath = false;
                    }
                }
            }
        }
        if(mCaptureLine.checkCaptureLineHitbox()){
            mCaptureLine.resetCaptureLine();
            mCursorPoint.resetCursorPoint();
        }
        if(mMonsterObject.checkCollisionWithCaptureLine(mCaptureLine.returnPoints())){
            mCaptureLine.resetCaptureLine();
            mCursorPoint.resetCursorPoint();
            if(mRequestedActivity.lineHit()){
                mRunning = false;
                mRequestedActivity.gameFinished(false,mMonsterObject);
            }
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

        mCursorPoint.update(x,y);

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
        mCursorPoint.resetCursorPoint();
    }

    private void moveTouch(float x, float y) {
        mCaptureLine.captureLineMove(x,y);
        if(mCursorPoint.isInsideofCircle(x,y) && mCaptureLine.returnPoints().size() > 28){
            mCaptureLine.fillCaptureLine();
            filledPath = true;
        }
    }

    private void upTouch(){ mCaptureLine.captureLineUp();}
}
