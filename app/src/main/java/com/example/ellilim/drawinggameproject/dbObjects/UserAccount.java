package com.example.ellilim.drawinggameproject.dbObjects;

public class UserAccount {
    private String username;
    private long lvl;

    public UserAccount(String username, long lvl){
        this.username = username;
        this.lvl = lvl;
    }

    public String returnUsername(){
        return username;
    }

    public long returnLVL(){
        return lvl;
    }
}
