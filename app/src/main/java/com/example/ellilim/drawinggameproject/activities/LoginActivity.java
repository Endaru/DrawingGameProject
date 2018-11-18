package com.example.ellilim.drawinggameproject.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions;
import com.example.ellilim.drawinggameproject.logicalComponents.EnumSuccesCodes;
import com.example.ellilim.drawinggameproject.loginForms.EditTextWithValidation;

public class LoginActivity extends McaptureActivity {

    private EditTextWithValidation mail;
    private EditTextWithValidation password;
    private DBFunctions DBFunctions;
    private ConstraintLayout loginLayout;
    private View usernameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBFunctions = new DBFunctions(this);

        mail = (EditTextWithValidation) findViewById(R.id.editText_Email);
        password = (EditTextWithValidation) findViewById(R.id.editText_Password);
        loginLayout = (ConstraintLayout) findViewById(R.id.loginLayout);
        usernameFragment = (View) findViewById(R.id.fragment_username_login);

        Button signIn = (Button) findViewById(R.id.button_signin);
        Button signUp = (Button) findViewById(R.id.signup);
        FloatingActionButton google = (FloatingActionButton) findViewById(R.id.googleButton);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        google.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        DBFunctions.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_signin:
                if(validation()){
                    DBFunctions.FirebaseSignIn(mail.getText().toString().trim(),password.getText().toString().trim());
                }
                break;
            case R.id.googleButton:
                DBFunctions.GoogleSignIn();
                break;
            case R.id.signup:
                Class signUpActivity = SignUpActivity.class;
                Intent startSignUp = new Intent(LoginActivity.this,signUpActivity);
                startActivity(startSignUp);
                break;
        }
    }

    //Validation for the selected fields
    private boolean validation(){
        boolean goodMail = false;
        boolean goodPass = false;

        if(mail.getText().toString().trim().length() >= 2){
            goodMail = true;
        }
        if(password.getText().toString().trim().length() >= 2){
            goodPass = true;
        }
        return goodMail && goodPass;
    }

    @Override
    public void requestedJob(boolean JobSuccesful, EnumSuccesCodes code) {
        switch (code){
            case FIREBASELOGIN:
                if(JobSuccesful){
                    DBFunctions.checkAccount();
                }
                break;
            case GOOGLELOGIN:
                if(JobSuccesful){
                    DBFunctions.UserLoggedInCheck();
                    DBFunctions.checkAccount();
                }
                break;
            case CHECKUSERACCOUNT:
                if(JobSuccesful){
                    userLoggedIn();
                } else {
                    loginLayout.setVisibility(View.GONE);
                    usernameFragment.setVisibility(View.VISIBLE);
                }
                break;
            case SETUSERNAME:
                if(JobSuccesful){
                    DBFunctions.setLvl(1);
                }
                break;
            case SETLVL:
                if(JobSuccesful){
                    userLoggedIn();
                }
                break;
        }
    }

    private void userLoggedIn(){
        Class mapsActivity = MapsActivity.class;
        Intent startMaps = new Intent(LoginActivity.this,mapsActivity);
        startActivity(startMaps);
    }

    @Override
    public void onBackPressed() {
        if(usernameFragment.getVisibility() == View.VISIBLE){
            if(usernameFragment.getVisibility() == View.VISIBLE){
                loginLayout.setVisibility(View.VISIBLE);
                usernameFragment.setVisibility(View.GONE);
            }
            loginLayout.setVisibility(View.VISIBLE);
            usernameFragment.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}
