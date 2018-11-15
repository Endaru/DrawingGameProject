package com.example.ellilim.drawinggameproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.loginForms.EditTextWithValidation;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signIn;
    private Button signUp;
    private FloatingActionButton google;

    private EditTextWithValidation mail;
    private EditTextWithValidation password;

    private CRUD DBFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBFunctions = new CRUD(this);

        signIn = (Button) findViewById(R.id.button_signin);
        signUp = (Button) findViewById(R.id.signup);
        google = (FloatingActionButton) findViewById(R.id.googleButton);

        mail = (EditTextWithValidation) findViewById(R.id.editText_Email);
        password = (EditTextWithValidation) findViewById(R.id.editText_Password);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        google.setOnClickListener(this);
    }

    //Need to call this to allow Google to use signIn, While having most functionality inside
    //Of the CRUD class.
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
}
