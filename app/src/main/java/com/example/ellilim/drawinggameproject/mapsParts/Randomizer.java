package com.example.ellilim.drawinggameproject.mapsParts;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

import java.util.Random;

//Randomizer class for where to put a random marker
public class Randomizer {
    public LatLng createMarkerAtLocation(Location l){
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

        return new LatLng(Lat, Long);
    }
}
