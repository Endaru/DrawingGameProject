package com.example.ellilim.drawinggameproject.gameComponents;

import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class CaptureLine extends Path {
    protected ArrayList<Point> mPoints = new ArrayList<>();

    protected Boolean update(){
        return isPathComplex(mPoints);
    }

    static private Boolean isPathComplex(List<Point> path) {
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
            }
        }
        return false;
    }

    static private Boolean lineSegmentsIntersect(float p0_x, float p0_y, float p1_x, float p1_y,
                                         float p2_x, float p2_y, float p3_x, float p3_y) {
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
