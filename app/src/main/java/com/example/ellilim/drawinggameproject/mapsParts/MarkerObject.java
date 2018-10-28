package com.example.ellilim.drawinggameproject.mapsParts;

import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.Random;

public class MarkerObject {
    int mTimer;
    Marker mMarker;

    public MarkerObject(Marker m){
        mMarker = m;
        Random rand = new Random();
        mTimer = rand.nextInt(9);
        mTimer++;
        Log.i("INFORMATION",""+ mTimer);
    }

    public boolean isTimerDone(){
        if(mTimer <= 0){
            Log.i("INFORMATION","Removed: "+ mMarker);
            mMarker.remove();
            return true;
        }else{
            mTimer--;
            return false;
        }
    }
}
