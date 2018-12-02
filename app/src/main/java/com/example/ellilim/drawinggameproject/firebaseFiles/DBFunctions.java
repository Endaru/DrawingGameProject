package com.example.ellilim.drawinggameproject.firebaseFiles;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.mCaptureEnums.EnumSuccesCodes;
import com.example.ellilim.drawinggameproject.mCaptureExtensions.McaptureActivity;
import com.example.ellilim.drawinggameproject.firebaseFiles.Monster;
import com.example.ellilim.drawinggameproject.firebaseFiles.UserAccount;
import com.example.ellilim.drawinggameproject.drawingGame.gameComponents.monsterObject;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBFunctions {

    //States we can expect to get back.
    private static final int RC_SIGN_IN = 0;
    private static final int RESULT_OK = -1;

    //List of providers
    private List<AuthUI.IdpConfig> providers = Collections.singletonList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    private FirebaseAuth mAuth;
    private McaptureActivity requestedActivity;
    private FirebaseFirestore DB;
    private FirebaseUser user;

    //Need activity to return certain results.
    public DBFunctions(McaptureActivity activity){
        requestedActivity = activity;
        mAuth = FirebaseAuth.getInstance();
        DB = FirebaseFirestore.getInstance();
    }

    //SignIn with an email and password
    public void FirebaseSignIn(String Email, String Password){
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(requestedActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    requestedActivity.returnedStatement(requestedActivity.getString(R.string.authentication_succes),false);
                    user = mAuth.getCurrentUser();
                } else {
                    requestedActivity.returnedStatement(requestedActivity.getString(R.string.authentication_failed),false);
                }
                requestedActivity.requestedJob(task.isSuccessful(), EnumSuccesCodes.FIREBASELOGIN);
            }
        });
    }

    //SignIn with Google
    public void GoogleSignIn(){
        requestedActivity.startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),RC_SIGN_IN);
    }


    //Create a Firebase Account
    public void FirebaseCreateAccount(String Email, String Password){
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(requestedActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = mAuth.getCurrentUser();
                    requestedActivity.requestedJob(task.isSuccessful(), EnumSuccesCodes.CREATEACCOUNT);
                } else {
                    requestedActivity.returnedStatement("Failed: "+ task.getException(),false);
                }
            }
        });
    }

    //Check if the account already has an entry in the FireStore, If Yes continue, if no create it.
    public void checkAccount(){
        DocumentReference docRef = DB.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        requestedActivity.returnedStatement(requestedActivity.getString(R.string.authentication_succes),false);
                    }
                    requestedActivity.requestedJob(document != null && document.exists(),EnumSuccesCodes.CHECKUSERACCOUNT);
                } else {
                    requestedActivity.returnedStatement("Failed: "+ task.getException(),false);
                }
            }
        });
    }

    //Check if user is logged in or not
    public void UserLoggedInCheck(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            user = FirebaseAuth.getInstance().getCurrentUser();
        }
    }

    //Get the userAccount
    public void getUserAccount(){
        if(user != null){
            DocumentReference userRef = DB.collection("users").document(user.getUid());
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        UserAccount User = new UserAccount((String)doc.get("username"),(long)doc.get("lvl"));
                        requestedActivity.requestedUser(User);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    //Sets LVL in Firebase for logged in User
    public void setLvl(int lvl){
        Map<String, Object> data = new HashMap<>();
        data.put("lvl",lvl);
        DB.collection("users").document(user.getUid()).set(data, SetOptions.merge());
        requestedActivity.requestedJob(true,EnumSuccesCodes.SETLVL);
    }

    //Sets username in Firebase for logged in User
    public void setUsername(String username){
        Map<String, Object> data = new HashMap<>();
        data.put("username",username);
        DB.collection("users").document(user.getUid()).set(data, SetOptions.merge());
        requestedActivity.requestedJob(true,EnumSuccesCodes.SETUSERNAME);
    }

    //Delete the account from the user
    public void deleteUserAccount(){
        if(user != null){
            DocumentReference userRef = DB.collection("users").document(user.getUid());
            Map<String, Object> data = new HashMap<>();
            data.put("username", FieldValue.delete());
            data.put("lvl", FieldValue.delete());
            userRef.update(data);

            DB.collection("users").document(user.getUid()).delete();
            user.delete();
            FirebaseAuth.getInstance().signOut();
            requestedActivity.requestedJob(true,EnumSuccesCodes.DELETEUSER);
        }
    }

    //When activity for Google Signin is resolved
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                user = FirebaseAuth.getInstance().getCurrentUser();
                requestedActivity.requestedJob(true,EnumSuccesCodes.GOOGLELOGIN);
            } else {
                Toast.makeText(requestedActivity, requestedActivity.getString(R.string.authentication_succes), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Add a monster to the user
    public void addMonster(monsterObject monster){
        Date currentTime = Calendar.getInstance().getTime();
        Map<String, Object> data = new HashMap<>();
        data.put("id",monster.monsterID);
        data.put("name",monster.monsterName);
        data.put("date",currentTime);

        DB.collection("users").document(user.getUid()).collection("monsters").add(data);
        requestedActivity.requestedJob(true,EnumSuccesCodes.ADDMONSTER);
    }

    //Give a monster a nickname
    public void changeMonsterName(String nickName, String monsterId){
        Map<String, Object> data = new HashMap<>();
        data.put("name",nickName);
        DB.collection("users").document(user.getUid()).collection("monsters").document(monsterId).set(data, SetOptions.merge());
        requestedActivity.requestedJob(true,EnumSuccesCodes.MONSTERNICK);
    }

    //Delete monster from user
    public void deleteMonster(String monsterId){
        DB.collection("users").document(user.getUid()).collection("monsters").document(monsterId).delete();
        requestedActivity.requestedJob(true,EnumSuccesCodes.DELETEMONSTER);
    }

    //Get all monsters
    public void getMonsters(){
        CollectionReference monsterRef = DB.collection("users").document(user.getUid()).collection("monsters");
        monsterRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        List<Monster> monsterList = new ArrayList<Monster>();
                        monsterList.add(new Monster(
                                document.getId(),
                                Integer.parseInt(document.getData().get("id").toString()),
                                document.getData().get("name").toString(),
                                document.getData().get("date").toString()
                        ));
                        requestedActivity.requestedMonsterList(monsterList);
                    }
                }
            }
        });
    }
}
