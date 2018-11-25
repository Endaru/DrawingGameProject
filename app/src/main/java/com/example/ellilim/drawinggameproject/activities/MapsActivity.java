package com.example.ellilim.drawinggameproject.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.fragments.GameResultFragment;
import com.example.ellilim.drawinggameproject.mapsParts.MarkerObject;
import com.example.ellilim.drawinggameproject.mapsParts.McaptureMarkerFactory;
import com.example.ellilim.drawinggameproject.mapsParts.Randomizer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,View.OnClickListener,GoogleMap.OnMapClickListener{

    private static final int MAPS_ACTIVITY_CONTEXT = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private McaptureMarkerFactory mMarkerFactory;

    private Circle mCircle;
    private float[] mDistance = new float[2];

    private Boolean DebugMode = false;
    private Boolean DebugModeRadius = false;
    private Boolean DebugModeMarker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton UserButton = (FloatingActionButton) findViewById(R.id.userButton);
        FloatingActionButton MonsterButton = (FloatingActionButton) findViewById(R.id.MonsterButton);
        FloatingActionButton Logout = (FloatingActionButton) findViewById(R.id.LogoutButton);
        FloatingActionButton GameButton = (FloatingActionButton) findViewById(R.id.GameButton);

        Switch DebugModeButton = (Switch) findViewById(R.id.DebugMode);
        Switch RadiusModeButton = (Switch) findViewById(R.id.RadiusMode);
        Switch MarkerModeButton = (Switch) findViewById(R.id.MarkerMode);

        UserButton.setOnClickListener(this);
        MonsterButton.setOnClickListener(this);
        Logout.setOnClickListener(this);
        GameButton.setOnClickListener(this);

        DebugModeButton.setOnClickListener(this);
        RadiusModeButton.setOnClickListener(this);
        MarkerModeButton.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.zoomBy(15));
        mLocationRequest = new LocationRequest();
        mMarkerFactory = new McaptureMarkerFactory(mMap);
        getLocation();
    }

    //Set that we want the location and when it needs to update.
    public void getLocation(){
        if(mMap != null){
            mLocationRequest.setInterval(6000);
            mLocationRequest.setFastestInterval(6000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            checkPermission();
        }
    }

    //Check if we have permission to use the GPS to get our current location? if not ask for it.
    private void checkPermission(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        new AlertDialog.Builder(this)
                                .setTitle("Location Permission Needed")
                                .setMessage("This app needs the Location permission, please accept to use location functionality")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION );
                                    }
                                })
                                .create()
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION );
                    }
                }
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if(!DebugMode){
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {
                    Location location = locationList.get(locationList.size() - 1);

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if(mCircle != null){
                        mCircle.remove();
                    }
                    mCircle = mMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(50)
                            .strokeColor(Color.RED));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    mMarkerFactory.updateLocation(location);
                    mMarkerFactory.perhapsCreateMarker();
                    mMarkerFactory.checkMarkerTimers();
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Location.distanceBetween(marker.getPosition().latitude,marker.getPosition().longitude,mCircle.getCenter().latitude,mCircle.getCenter().longitude,mDistance);

        if(mDistance[0] > mCircle.getRadius()){
            Toast.makeText(getBaseContext(),"Outside",Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(getBaseContext(),"Inside",Toast.LENGTH_LONG).show();
            mMarkerFactory.removeMarker(marker);
            Class gameActivity = GameActivity.class;
            Intent startGaming = new Intent(MapsActivity.this,gameActivity);
            startActivityForResult(startGaming,MAPS_ACTIVITY_CONTEXT);
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == MAPS_ACTIVITY_CONTEXT){
            if(resultCode == RESULT_OK){
                Bundle bundle = new Bundle();
                bundle.putBoolean("gameWon",data.getBooleanExtra("gameWon",false));
                bundle.putString("monsterName",data.getStringExtra("monsterName"));

                Fragment newFragment = new GameResultFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.result_screen, newFragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Context context = MapsActivity.this;
        switch (v.getId()) {
            case R.id.userButton:
                startActivity(new Intent(context, UserActivity.class));
                break;
            case R.id.MonsterButton:
                startActivity(new Intent(context, MonsterActivity.class));
                break;
            case R.id.LogoutButton:
                finish();
                break;
            case R.id.GameButton:
                startActivityForResult(new Intent(context, GameActivity.class),MAPS_ACTIVITY_CONTEXT);
                break;
            case R.id.DebugMode:
                setDebugMode();
                break;
            case R.id.MarkerMode:
                setDebugModeMarker();
                break;
            case R.id.RadiusMode:
                setDebugModeRadius();
                break;
        }
    }

    public void setDebugMode(){
        Switch RadiusModeButton = (Switch) findViewById(R.id.RadiusMode);
        Switch MarkerModeButton = (Switch) findViewById(R.id.MarkerMode);
        if(DebugMode){
            DebugMode = false;
            DebugModeRadius = false;
            DebugModeMarker = false;
            RadiusModeButton.setChecked(DebugModeRadius);
            RadiusModeButton.setVisibility(View.GONE);
            MarkerModeButton.setChecked(DebugModeMarker);
            MarkerModeButton.setVisibility(View.GONE);
        }else{
            DebugMode = true;
            RadiusModeButton.setVisibility(View.VISIBLE);
            MarkerModeButton.setVisibility(View.VISIBLE);
        }
    }

    public void setDebugModeMarker(){
        Switch RadiusModeButton = (Switch) findViewById(R.id.RadiusMode);
        if(DebugModeMarker){
            DebugModeMarker = false;
        }else{
            if(DebugModeRadius){
                DebugModeRadius = false;
                RadiusModeButton.setChecked(DebugModeRadius);
            }
            DebugModeMarker = true;
        }
    }

    public void setDebugModeRadius() {
        Switch MarkerModeButton = (Switch) findViewById(R.id.MarkerMode);
        if(DebugModeRadius){
            DebugModeRadius = false;
        }else{
            if(DebugModeMarker){
                DebugModeMarker = false;
                MarkerModeButton.setChecked(DebugModeMarker);
            }
            DebugModeRadius = true;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(DebugMode){
            if(DebugModeMarker){
                mMarkerFactory.createMarkerAtLocation(latLng);
            }
            if(DebugModeRadius){
                if(mCircle != null){
                    mCircle.remove();
                }
                mCircle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(50)
                        .strokeColor(Color.RED));
            }
        }
    }
}