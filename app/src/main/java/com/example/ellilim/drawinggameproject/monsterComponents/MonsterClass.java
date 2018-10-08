package com.example.ellilim.drawinggameproject.monsterComponents;

import android.graphics.Path;

public class MonsterClass extends Path {
    public int monsterID;
    public String monsterName;

    public int monsterHealth;
    public int mX;
    public int mY;
    public int mRadius;

    public MonsterClass(String name, int health, int id, int x, int y) {
        monsterID = id;
        monsterName = name;
        monsterHealth = health;
        mX = x;
        mY = y;
        mRadius = (200);
    }
}
