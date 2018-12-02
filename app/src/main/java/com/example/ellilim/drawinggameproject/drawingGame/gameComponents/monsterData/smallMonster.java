package com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterData;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.ellilim.drawinggameproject.mCaptureEnums.EnumMovement;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;

import java.util.ArrayList;

public class smallMonster extends monsterObject {

    //Create a smal monster
    public smallMonster(int points, String name, int id, int width, int height, int y, int x) {
        super(points, name, id, width, height, y, x);
    }

    @Override
    //draw monster to the canvas
    public void draw(Canvas canvas) {
        canvas.drawRect(mX,mY,(mX + monsterWidth),(mY + monsterHeight),mPaint);
    }

    @Override
    //Check if we collide with the captureline
    public Boolean checkCollisionWithCaptureLine(ArrayList<Point> points) {
        if(points == null || points.size() <= 2){
            return false;
        }

        int length = points.size();

        //Do this for every point.
        for(int i = 1; i < length; i++){
            Point line = points.get(i);
            if(mRectangle.contains(line.x,line.y)){
                return true;
            }
        }
        return false;
    }

    @Override
    //Update the location on the canvas
    public void update(Canvas canvas) {
        movement(canvas);
        canvas.drawRect(mX,mY,(mX + monsterWidth),(mY + monsterHeight),mPaint);
        mRectangle = new Rect(mX,mY,(mX + monsterWidth),(mY + monsterHeight));
    }

    @Override
    //Movement of a smallMonster
    public void movement(Canvas canvas) {
        //Randomize so monster keeps walking in same direction
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

    //Check if monster is captured
    public Boolean checkCapture(ArrayList<Point> points){
        int length = points.size();

        for (int i = 1; i < length; i++) {
            Point lineA = points.get(i);

            for (int j = i + 1; j < length; j++) {
                Point lineB = points.get(j);

                //Check if one of the lines goes through the monster by using its size
                boolean left = lineline(lineA.x,lineA.y,lineB.x,lineB.y,mX,mY,mX,mY+monsterHeight);
                boolean right = lineline(lineA.x,lineA.y,lineB.x,lineB.y,mX + monsterWidth,mY, mX+monsterWidth,mY+monsterHeight);
                boolean top = lineline(lineA.x,lineA.y,lineB.x,lineB.y, mX,mY, mX+monsterWidth,mY);
                boolean bottom = lineline(lineA.x,lineA.y,lineB.x,lineB.y, mX,mY+monsterHeight, mX+monsterWidth,mY+monsterHeight);

                //If any are true then its captured
                if(left || right || top || bottom){
                    return true;
                }
            }
        }
        return false;
    }

    //Calculate the line and check if it intersects with a line between two points
    private boolean lineline(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
        float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

        return uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1;
    }
}
