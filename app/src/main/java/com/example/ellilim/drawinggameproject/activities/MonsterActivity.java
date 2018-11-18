package com.example.ellilim.drawinggameproject.activities;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ellilim.drawinggameproject.R;

public class MonsterActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster);

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
}
