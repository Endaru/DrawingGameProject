package com.example.ellilim.drawinggameproject.activities.parentActivities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.dbObjects.Monster;
import com.example.ellilim.drawinggameproject.dbObjects.UserAccount;
import com.example.ellilim.drawinggameproject.logicalComponents.EnumSuccesCodes;

import java.util.List;

public abstract class McaptureActivity extends AppCompatActivity implements View.OnClickListener {

    public void requestedUser(UserAccount user){
        Log.i("INFORMATION","username: " + user.returnUsername());
        Log.i("INFORMATION","lvl: " + user.returnLVL());
    }

    public void requestedMonster(){
        //TODO: add data
    }

    public void requestedMonsterList(List<Monster> monsters){
        //TODO: add data
    }

    public void requestedJob(boolean JobSuccesful, EnumSuccesCodes code){
        Log.i("INFORMATION","JOB: " + JobSuccesful);
        Log.i("INFORMATION","CODE: " + code);
    }

    public void returnedStatement(String message, boolean lengthLong){
        if(lengthLong){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        //Do Something, Just here so i dont have to call it in its children.
    }
}
