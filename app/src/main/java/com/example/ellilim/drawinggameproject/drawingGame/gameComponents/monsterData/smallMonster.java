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
        movement(canvas);
        canvas.drawRect(mX,mY,(mX + monsterWidth),(mY + monsterHeight),mPaint);
        mRectangle = new Rect(mX,mY,(mX + monsterWidth),(mY + monsterHeight));
    }

    @Override
    public void movement(Canvas canvas) {
        if(zero == 0){
            ammountOfRandom = (int)(Math.random() * 50 + 1);
            randomMove = (int )(Math.random() * 8 + 1);
        }
        zero++;
        if(zero > ammountOfRandom){
            zero = 0;
        }
        if(checkBounds(canvas)){
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

    public Boolean checkCapture(ArrayList<Point> points){
        int length = points.size();

        for (int i = 1; i < length; i++) {
            Point lineA = points.get(i);

            for (int j = i + 1; j < length; j++) {
                Point lineB = points.get(j);

                boolean left = lineline(lineA.x,lineA.y,lineB.x,lineB.y,mX,mY,mX,mY+monsterHeight);
                boolean right = lineline(lineA.x,lineA.y,lineB.x,lineB.y,mX + monsterWidth,mY, mX+monsterWidth,mY+monsterHeight);
                boolean top = lineline(lineA.x,lineA.y,lineB.x,lineB.y, mX,mY, mX+monsterWidth,mY);
                boolean bottom = lineline(lineA.x,lineA.y,lineB.x,lineB.y, mX,mY+monsterHeight, mX+monsterWidth,mY+monsterHeight);

                if(left || right || top || bottom){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean lineline(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

        return uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1;
    }
}
