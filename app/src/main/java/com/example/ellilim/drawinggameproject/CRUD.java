package com.example.ellilim.drawinggameproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ellilim.drawinggameproject.loginForms.EditTextWithValidation;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CRUD {
    private FirebaseAuth mAuth;
    private Activity callingActivity;
    private FirebaseUser user;
    private FirebaseFirestore DB;
    private String Username;

    private static final int RC_SIGN_IN = 0;
    private static final int RESULT_OK = -1;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    CRUD(Activity activity){
        callingActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        DB = FirebaseFirestore.getInstance();
    }

    protected void FirebaseSignIn(String Email, String Password){
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(callingActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(callingActivity, "Authentication Succesfull.", Toast.LENGTH_SHORT).show();
                    user = mAuth.getCurrentUser();
                    checkAccount();
                } else {
                    Toast.makeText(callingActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void FirebaseCreateAccount(String Email, String Password){
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(callingActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = mAuth.getCurrentUser();
                    firstLogin();
                } else {
                    Toast.makeText(callingActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void GoogleSignIn(){
        callingActivity.startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),RC_SIGN_IN);
    }

    protected FirebaseUser FirebaseGetUser(){
        return user;
    }

    protected void checkAccount(){
        DocumentReference docRef = DB.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(callingActivity, "Authentication Succesfull.", Toast.LENGTH_SHORT).show();

                        //Finish Builder
                        toMap();
                    } else {
                        firstLogin();
                    }
                } else {
                    Log.i("INFORMATION", "get failed with ", task.getException());
                }
            }
        });
    }

    protected void firstLogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(callingActivity,R.style.AlertDialogCustom));
        final EditTextWithValidation input = new EditTextWithValidation(callingActivity);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Username");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(validation(input.getText().toString())){
                    Map<String, Object> data = new HashMap<>();
                    data.put("username",input.getText().toString());
                    data.put("lvl",1);
                    DB.collection("users").document(user.getUid()).set(data, SetOptions.merge());
                    Toast.makeText(callingActivity, "Authentication Succesfull.", Toast.LENGTH_SHORT).show();

                    //Finish Builder
                    toMap();
                }
            }

            private boolean validation(String name){
                boolean goodName = false;
                if(name.trim().length() >= 2){
                    goodName = true;
                }
                return goodName;
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().setLayout(100,50);
        alert.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                user = FirebaseAuth.getInstance().getCurrentUser();
                checkAccount();
            } else {
                Toast.makeText(callingActivity, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void toMap(){
        Class mapsActivity = MapsActivity.class;
        Intent startMaps = new Intent(callingActivity,mapsActivity);
        callingActivity.startActivity(startMaps);
    }
}
