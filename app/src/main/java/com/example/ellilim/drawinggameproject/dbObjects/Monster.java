package com.example.ellilim.drawinggameproject.dbObjects;

public class Monster {
    private String firebaseId;
    private int id;

    private String monsterName;

    public Monster(String firebaseId, int id, String monsterName){
        this.firebaseId = firebaseId;
        this.id = id;
        this.monsterName = monsterName;
    }

    public String returnFirebaseId(){ return firebaseId; }

    public int returnId(){ return id; }

    public String returnMonsterName(){ return monsterName; }
}
