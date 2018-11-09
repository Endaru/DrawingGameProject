package com.example.ellilim.drawinggameproject;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.loginForms.EditTextWithValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signIn;
    private ImageButton returnButton;

    private EditTextWithValidation mail;
    private EditTextWithValidation password;
    private EditTextWithValidation password_check;

    private CRUD DBFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        DBFunctions = new CRUD(this);

        signIn = (Button) findViewById(R.id.button_signin);
        returnButton = (ImageButton) findViewById(R.id.returnButton);

        mail = (EditTextWithValidation) findViewById(R.id.editText_Email);
        password = (EditTextWithValidation) findViewById(R.id.editText_Password);
        password_check = (EditTextWithValidation) findViewById(R.id.editText_Password_Check);

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
        return goodMail && goodPass && goodPassCheck && goodPassMatch;
    }
}
