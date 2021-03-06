package com.example.ellilim.drawinggameproject.mCaptureFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.mCaptureExtensions.McaptureActivity;
import com.example.ellilim.drawinggameproject.firebaseFiles.DBFunctions;
import com.example.ellilim.drawinggameproject.mCaptureExtensions.EditTextWithValidation;

public class MonsterFragment extends Fragment implements View.OnClickListener{

    private EditTextWithValidation monsterName;
    private String monsterId;
    public DBFunctions DBFunctions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        monsterId = bundle.getString("firebaseId");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.monster_fragment,container,false);

        McaptureActivity act = (McaptureActivity) getActivity();
        DBFunctions = new DBFunctions(act);
        DBFunctions.UserLoggedInCheck();

        Button correctButton = (Button) view.findViewById(R.id.button_correct);
        monsterName = (EditTextWithValidation) view.findViewById(R.id.editText_Username);

        correctButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(validation(monsterName.getText().toString().trim())){
            DBFunctions.changeMonsterName(monsterName.getText().toString().trim(),monsterId);
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
