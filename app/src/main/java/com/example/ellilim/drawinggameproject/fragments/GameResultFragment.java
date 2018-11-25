package com.example.ellilim.drawinggameproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ellilim.drawinggameproject.R;

public class GameResultFragment extends Fragment implements View.OnClickListener{

    private boolean mGameWon;
    private String mMonsterName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mMonsterName = bundle.getString("monsterName");
        mGameWon = bundle.getBoolean("gameWon",false);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_result_screen,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(mGameWon){
            TextView resultText = (TextView) view.findViewById(R.id.ResultText);
            resultText.setText("You Win!!!");
        }else{
            TextView resultText = (TextView) view.findViewById(R.id.ResultText);
            resultText.setText("You lose, " + mMonsterName + " got away!!!");
        }

        FloatingActionButton returnButton = (FloatingActionButton) view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnButton:
                getView().setVisibility(View.GONE);
                break;
        }
    }
}
