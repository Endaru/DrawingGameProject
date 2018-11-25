package com.example.ellilim.drawinggameproject.activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.activities.parentActivities.McaptureActivity;
import com.example.ellilim.drawinggameproject.dbObjects.Monster;
import com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions;

import java.util.List;

public class MonsterActivity extends McaptureActivity implements View.OnClickListener {

    public DBFunctions DBFunctions;
    FloatingActionButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster);

        DBFunctions = new DBFunctions(this);
        DBFunctions.UserLoggedInCheck();
        DBFunctions.getMonsters();

        returnButton = (FloatingActionButton) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = MonsterActivity.this;
        switch (v.getId()) {
            case R.id.returnButton:
                finish();
                break;
        }
    }

    @Override
    public void requestedMonsterList(List<Monster> monsters) {
        Log.i("INFORMATION","" + monsters);
    }
}
