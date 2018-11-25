package com.example.ellilim.drawinggameproject.mapsParts;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MarkerObject {
    public Marker mMarker;
    public MarkerOptions mMarkerOptions;
    private int mTimer;

    public MarkerObject(){
        Random rand = new Random();
        mTimer = rand.nextInt(20);
        mTimer++;
    }

    public MarkerObject(int timer){
        mTimer = timer;
        mTimer++;
    }

    public void setmMarker(Marker m){
        mMarker = m;
    }

    public  void setmMarkerOptions(LatLng latLng, BitmapDescriptor icon){
        mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(latLng);
        mMarkerOptions.icon(icon);
    }

    public boolean isTimerDone(){
        if(mTimer <= 0){
            mMarker.remove();
            return true;
        }else{
            mTimer--;
            return false;
        }
    }
}
