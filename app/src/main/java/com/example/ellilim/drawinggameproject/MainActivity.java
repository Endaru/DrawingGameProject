package com.example.ellilim.drawinggameproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mDebugButton1;
    private Button mDebugButton2;
    private Button mDebugButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDebugButton1 = (Button) findViewById(R.id.debugItem1);
        mDebugButton2 = (Button) findViewById(R.id.debugItem2);
        mDebugButton3 = (Button) findViewById(R.id.debugItem3);

        mDebugButton1.setOnClickListener(this);
        mDebugButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = MainActivity.this;
        switch (v.getId()){
            case R.id.debugItem1:
                Class mapsActivity = MapsActivity.class;
                Intent startMaps = new Intent(context,mapsActivity);
                startActivity(startMaps);
                break;
            case R.id.debugItem2:
                Class gameActivity = GameActivity.class;
                Intent startGaming = new Intent(context,gameActivity);
                startActivity(startGaming);
                break;
            case R.id.debugItem3:
                break;
        }
    }
}
