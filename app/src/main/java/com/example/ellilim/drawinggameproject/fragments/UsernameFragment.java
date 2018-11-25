package com.example.ellilim.drawinggameproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ellilim.drawinggameproject.activities.parentActivities.McaptureActivity;
import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.logicalComponents.DBFunctions;
import com.example.ellilim.drawinggameproject.loginForms.EditTextWithValidation;

public class UsernameFragment extends Fragment implements View.OnClickListener{

    private EditTextWithValidation username;
    public DBFunctions DBFunctions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.username_fragment,container,false);

        McaptureActivity act = (McaptureActivity) getActivity();
        DBFunctions = new DBFunctions(act);
        DBFunctions.UserLoggedInCheck();

        Button correctButton = (Button) view.findViewById(R.id.button_correct);
        username = (EditTextWithValidation) view.findViewById(R.id.editText_Username);

        correctButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(validation(username.getText().toString().trim())){
            DBFunctions.setUsername(username.getText().toString().trim());
        }
    }

    private boolean validation(String name){
        boolean goodName = false;
        if(name.trim().length() >= 2){
            goodName = true;
        }
        return goodName;
    }
}
