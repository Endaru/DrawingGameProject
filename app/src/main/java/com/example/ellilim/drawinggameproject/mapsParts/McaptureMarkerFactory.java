package com.example.ellilim.drawinggameproject.mapsParts;

import android.location.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

//Factory that handels marker creation
public class McaptureMarkerFactory {
    private List<MarkerObject> mMarkerList = new ArrayList<MarkerObject>();
    private GoogleMap mMap;
    private Randomizer random;
    private Location lastKnownLocation;

    public McaptureMarkerFactory(GoogleMap map){
        mMap = map;
        random = new Randomizer();
    }

    public void updateLocation(Location location){
        lastKnownLocation = location;
    }

    public void perhapsCreateMarker(){
        Random rand = new Random();
        if(rand.nextInt(20) < 10){
            createMarker();
        }
    }

    //Create marker with basic icon
    public void createMarker(){
        MarkerObject newMarkerObject = new MarkerObject();
        newMarkerObject.setmMarkerOptions(random.createMarkerAtLocation(lastKnownLocation), BitmapDescriptorFactory.defaultMarker());
        newMarkerObject.setmMarker(mMap.addMarker(newMarkerObject.mMarkerOptions));
        mMarkerList.add(newMarkerObject);
    }

    //Create marker with a set icon
    public void createMarker(BitmapDescriptor icon){
        MarkerObject newMarkerObject = new MarkerObject();
        newMarkerObject.setmMarkerOptions(random.createMarkerAtLocation(lastKnownLocation), icon);
        newMarkerObject.setmMarker(mMap.addMarker(newMarkerObject.mMarkerOptions));
        mMarkerList.add(newMarkerObject);
    }

    //Create marker at a location with basic icon
    public void createMarkerAtLocation(LatLng latLng){
        MarkerObject newMarkerObject = new MarkerObject();
        newMarkerObject.setmMarkerOptions(latLng, BitmapDescriptorFactory.defaultMarker());
        newMarkerObject.setmMarker(mMap.addMarker(newMarkerObject.mMarkerOptions));
        mMarkerList.add(newMarkerObject);
    }
    //Create marker at a location with a set icon
    public void createMarkerAtLocation(LatLng latLng, BitmapDescriptor icon){
        MarkerObject newMarkerObject = new MarkerObject();
        newMarkerObject.setmMarkerOptions(latLng, icon);
        newMarkerObject.setmMarker(mMap.addMarker(newMarkerObject.mMarkerOptions));
        mMarkerList.add(newMarkerObject);
    }

    //Check the timers of the existing markers
    public void checkMarkerTimers(){
        for(MarkerObject m:mMarkerList){
            if(m.isTimerDone()){
                if(mMarkerList.equals(m)){
                    mMarkerList.remove(m);
                }
            }
        }
    }

    //Remove a marker
    public void removeMarker(Marker m){
        Iterator<MarkerObject> iterator = mMarkerList.iterator();
        while(iterator.hasNext()){
            MarkerObject marker = iterator.next();
            if(marker.mMarker.equals(m)){
                iterator.remove();
                marker.mMarker.remove();
            }
        }
    }
}
