package com.example.ellilim.drawinggameproject.gameComponents;

public class FlashLightCone {
    private int mX,mY,mRadius;

    public FlashLightCone(int viewWidth,int viewHeight){
        mX = viewHeight / 2;
        mY = viewWidth / 2;
        mRadius = ((viewWidth <= viewHeight) ? mX / 3 : mY / 3);
    }

    public void update(int x, int y){
        mX = x;
        mY = y;
    }

    public int getX(){
        return mX;
    }

    public int getY(){
        return mY;
    }

    public int getRadius(){
        return mRadius;
    }
}
