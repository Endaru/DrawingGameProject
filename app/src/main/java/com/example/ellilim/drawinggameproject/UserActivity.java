package com.example.ellilim.drawinggameproject;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        returnButton = (FloatingActionButton) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = UserActivity.this;
        switch (v.getId()) {
            case R.id.returnButton:
                finish();
                break;
        }
    }
}
