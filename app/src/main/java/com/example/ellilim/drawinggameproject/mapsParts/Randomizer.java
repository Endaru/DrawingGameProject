package com.example.ellilim.drawinggameproject.mapsParts;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class Randomizer {

    public MarkerOptions createMarkerAtLocation(Location l){
        MarkerOptions markerOptions = new MarkerOptions();
        Random rand = new Random();

        int firstValueLong = rand.nextInt(9);
        int secondValueLong = rand.nextInt(9);
        int thirdValueLong = rand.nextInt(9);

        int firstValueLat = rand.nextInt(9);
        int secondValueLat = rand.nextInt(9);
        int thirdValueLat = rand.nextInt(9);

        String generateNumberLong =  "" + 0.00 + 0 + 0 + firstValueLong + secondValueLong + thirdValueLong;
        String generateNumberLat =  "" + 0.00 + 0 + 0 + firstValueLat + secondValueLat + thirdValueLat;

        int selectDirectionLong = rand.nextInt(10);
        int selectDirectionLat = rand.nextInt(10);

        double Long;
        double Lat;
        if(selectDirectionLong >= 5){
            Long = l.getLongitude() + Float.parseFloat(generateNumberLong);
        }else{
            Long = l.getLongitude() - Float.parseFloat(generateNumberLong);
        }

        if(selectDirectionLat >= 5){
            Lat = l.getLatitude() + Float.parseFloat(generateNumberLat);
        }else{
            Lat = l.getLatitude() - Float.parseFloat(generateNumberLat);
        }
        LatLng latLng = new LatLng(Lat, Long);
        markerOptions.position(latLng);
        markerOptions.title("test");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        Log.i("INFORMATION",""+ latLng);
        return markerOptions;
    }
}
