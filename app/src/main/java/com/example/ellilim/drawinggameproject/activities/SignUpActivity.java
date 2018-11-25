package com.example.ellilim.drawinggameproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.activities.parentActivities.McaptureActivity;
import com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions;
import com.example.ellilim.drawinggameproject.logicalComponents.EnumSuccesCodes;
import com.example.ellilim.drawinggameproject.loginForms.EditTextWithValidation;

public class SignUpActivity extends McaptureActivity implements View.OnClickListener{

    private EditTextWithValidation mail;
    private EditTextWithValidation password;
    private EditTextWithValidation password_check;
    private EditTextWithValidation username;

    private com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions DBFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        DBFunctions = new DBFunctions(this);

        Button signIn = (Button) findViewById(R.id.button_signin);
        ImageButton returnButton = (ImageButton) findViewById(R.id.returnButton);

        mail = (EditTextWithValidation) findViewById(R.id.editText_Email);
        password = (EditTextWithValidation) findViewById(R.id.editText_Password);
        password_check = (EditTextWithValidation) findViewById(R.id.editText_Password_Check);
        username = (EditTextWithValidation) findViewById(R.id.editText_Username);

        signIn.setOnClickListener(this);
        returnButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_signin:
                if(validation()){
                    DBFunctions.FirebaseCreateAccount(mail.getText().toString().trim(),password.getText().toString().trim());
                }
                break;
            case R.id.returnButton:
                finish();
                break;
        }
    }

    private boolean validation(){
        boolean goodMail = false;
        boolean goodPass = false;
        boolean goodPassCheck = false;
        boolean goodPassMatch = false;
        boolean goodName = false;

        if(mail.getText().toString().trim().length() >= 2){
            goodMail = true;
        }
        if(password.getText().toString().trim().length() >= 2){
            goodPass = true;
        }
        if(password_check.getText().toString().trim().length() >= 2){
            goodPassCheck = true;
        }
        if(goodPass && goodPassCheck){
            if(password.getText().toString().trim().equals(password_check.getText().toString().trim())){
                goodPassMatch = true;
            }
        }
        if(username.getText().toString().trim().length() >= 2){
            goodName = true;
        }
        return goodMail && goodPass && goodPassCheck && goodPassMatch && goodName;
    }

    @Override
    public void requestedJob(boolean JobSuccesful, EnumSuccesCodes code) {
        switch (code){
            case CREATEACCOUNT:
                if(JobSuccesful){
                    DBFunctions.setUsername(username.getText().toString().trim());
                }
                break;
            case SETUSERNAME:
                if(JobSuccesful){
                    DBFunctions.setLvl(1);
                }
                break;
            case SETLVL:
                if(JobSuccesful){
                    Class mapsActivity = MapsActivity.class;
                    Intent startMaps = new Intent(SignUpActivity.this,mapsActivity);
                    startActivity(startMaps);
                }
                break;
        }
    }
}
