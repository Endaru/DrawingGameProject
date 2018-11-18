package com.example.ellilim.drawinggameproject.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.mapsParts.MarkerObject;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,View.OnClickListener{

    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    List<MarkerObject> mMarkerList = new ArrayList<MarkerObject>();
    Circle mCircle;
    float[] mDistance = new float[2];
    Boolean testvalue = true;

    FloatingActionButton UserButton;
    FloatingActionButton MonsterButton;
    FloatingActionButton GameButton;
    FloatingActionButton Logout;

    private com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions DBFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //DBFunctions = new DBFunctions(this);
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        UserButton = (FloatingActionButton) findViewById(R.id.userButton);
        MonsterButton = (FloatingActionButton) findViewById(R.id.MonsterButton);
        Logout = (FloatingActionButton) findViewById(R.id.LogoutButton);
        GameButton = (FloatingActionButton) findViewById(R.id.GameButton);

        UserButton.setOnClickListener(this);
        MonsterButton.setOnClickListener(this);
        Logout.setOnClickListener(this);
        GameButton.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // 1 minute interval
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        float[] mDistance = new float[2];

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
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
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                if(testvalue){
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if(mCircle != null){
                        mCircle.remove();
                    }
                    mCircle = mMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(50)
                            .strokeColor(Color.RED));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }

                Randomizer random = new Randomizer();
                MarkerOptions randomOptions = random.createMarkerAtLocation(location);
                mMarkerList.add(new MarkerObject(mMap.addMarker(randomOptions)));

                for(MarkerObject m: mMarkerList){
                    if(m.isTimerDone()){
                        if(mMarkerList.equals(m)){
                            mMarkerList.remove(m);
                        }
                    }
                }

                //move map camera
            }
        }
    };

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
            Class gameActivity = GameActivity.class;
            Intent startGaming = new Intent(MapsActivity.this,gameActivity);
            startActivity(startGaming);
            return true;
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
                startActivity(new Intent(context, GameActivity.class));
                break;
        }
    }
}