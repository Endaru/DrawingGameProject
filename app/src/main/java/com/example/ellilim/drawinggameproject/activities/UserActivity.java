package com.example.ellilim.drawinggameproject.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.activities.parentActivities.McaptureActivity;
import com.example.ellilim.drawinggameproject.dbObjects.UserAccount;
import com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions;
import com.example.ellilim.drawinggameproject.logicalComponents.EnumSuccesCodes;

public class UserActivity extends McaptureActivity {

    public DBFunctions DBFunctions;
    private ConstraintLayout userLayout;
    private View usernameFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DBFunctions = new DBFunctions(this);
        DBFunctions.UserLoggedInCheck();
        DBFunctions.getUserAccount();

        userLayout = (ConstraintLayout) findViewById(R.id.userLayout);
        usernameFragment = (View) findViewById(R.id.fragment_username_login);

        Button nameChange = (Button) findViewById(R.id.usernameChange);
        Button lvlButton = (Button) findViewById(R.id.lvlReset);
        Button deleteButton = (Button) findViewById(R.id.DeleteAccount);
        FloatingActionButton returnButton = (FloatingActionButton) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(this);
        nameChange.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        lvlButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnButton:
                finish();
                break;
            case R.id.usernameChange:
                userLayout.setVisibility(View.GONE);
                usernameFragment.setVisibility(View.VISIBLE);
                break;
            case R.id.lvlReset:
                DBFunctions.setLvl(1);
                break;
            case R.id.DeleteAccount:
                DBFunctions.deleteUserAccount();
                break;
        }
    }

    @Override
    public void requestedUser(UserAccount user) {
        TextView username = (TextView) findViewById(R.id.username);
        TextView lvlText = (TextView) findViewById(R.id.lvlNumber);
        username.setText(user.returnUsername());
        lvlText.setText("" + (int)user.returnLVL());
    }

    @Override
    public void onBackPressed() {
        if(usernameFragment.getVisibility() == View.VISIBLE){
            userLayout.setVisibility(View.VISIBLE);
            usernameFragment.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void requestedJob(boolean JobSuccesful, EnumSuccesCodes code) {
        switch (code) {
            case SETUSERNAME:
                if (JobSuccesful) {
                    userLayout.setVisibility(View.VISIBLE);
                    usernameFragment.setVisibility(View.GONE);
                    DBFunctions.getUserAccount();
                }
                break;
            case SETLVL:
                if(JobSuccesful){
                    DBFunctions.getUserAccount();
                }
                break;
            case DELETEUSER:
                Class logActivity = LoginActivity.class;
                Intent startLogin = new Intent(UserActivity.this,logActivity);
                startLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startLogin);
                break;
        }
    }
}
