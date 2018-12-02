package com.example.ellilim.drawinggameproject.firebaseFiles;

public class Monster {
    private String firebaseId;
    private int id;

    private String monsterName;
    private String captureDate;

    public Monster(String firebaseId, int id, String monsterName, String captureDate){
        this.firebaseId = firebaseId;
        this.id = id;
        this.monsterName = monsterName;
        this.captureDate = captureDate;
    }

    public String returnFirebaseId(){ return firebaseId; }

    public int returnId(){ return id; }

    public String returnMonsterName(){ return monsterName; }

    public String returnCaptureDate(){ return captureDate; }
}
