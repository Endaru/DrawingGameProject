package com.example.ellilim.drawinggameproject.mCaptureExtensions;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.firebaseFiles.Monster;
import com.example.ellilim.drawinggameproject.firebaseFiles.UserAccount;
import com.example.ellilim.drawinggameproject.mCaptureEnums.EnumSuccesCodes;

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
        Log.i("INFORMATION","MONSTERS: " + monsters);
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
