package com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterData;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.EnumMovement;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

import java.util.ArrayList;

//TODO 1.0. : Add Collosion to monster \/
//TODO 2.0. : Allow Monster to move \/
//TODO 3.0. : Monster can Attack CaptureLine
public class smallMonster extends monsterObject {

    public smallMonster(int points, String name, int id, int width, int height, int y, int x) {
        super(points, name, id, width, height, y, x);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(mX,mY,(mX + monsterWidth),(mY + monsterHeight),mPaint);
    }

    @Override
    public Boolean checkCollisionWithCaptureLine(ArrayList<Point> points) {
        if(points == null || points.size() <= 2){
            return false;
        }

        int length = points.size();

        for(int i = 1; i < length; i++){
            Point line = points.get(i);
            if(mRectangle.contains(line.x,line.y)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(Canvas canvas) {
        movement();
        canvas.drawRect(mX,mY,(mX + monsterWidth),(mY + monsterHeight),mPaint);
        mRectangle = new Rect(mX,mY,(mX + monsterWidth),(mY + monsterHeight));
    }

    @Override
    public void movement() {
        if(zero == 0){
            ammountOfRandom = (int)(Math.random() * 50 + 1);
            randomMove = (int )(Math.random() * 8 + 1);
            Log.i("INFORMATION","" + randomMove);
        }
        zero++;
        if(zero > ammountOfRandom){
            zero = 0;
        }
        switch (EnumMovement.values()[randomMove]){
            case UP:
                mY--;
                break;
            case DOWN:
                mY++;
                break;
            case LEFT:
                mX--;
                break;
            case RIGHT:
                mX++;
                break;
            case UPRIGHT:
                mY--;
                mX++;
                break;
            case UPLEFT:
                mY--;
                mX--;
                break;
            case DOWNRIGHT:
                mY++;
                mX++;
                break;
            case DOWNLEFT:
                mY++;
                mX--;
                break;
            case NONE:
                break;
        }
    }
}
