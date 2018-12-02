package com.example.ellilim.drawinggameproject;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ellilim.drawinggameproject.firebaseFiles.Monster;
import com.example.ellilim.drawinggameproject.mCaptureFragments.MonsterFragment;
import com.example.ellilim.drawinggameproject.firebaseFiles.DBFunctions;
import com.example.ellilim.drawinggameproject.mCaptureEnums.EnumSuccesCodes;
import com.example.ellilim.drawinggameproject.mCaptureAdapters.MonsterAdapter;
import com.example.ellilim.drawinggameproject.mCaptureExtensions.McaptureActivity;

import java.util.List;

public class MonsterActivity extends McaptureActivity implements View.OnClickListener, MonsterAdapter.MonsterAdapterOnClickHandler{

    public DBFunctions DBFunctions;
    private MonsterAdapter mMonsterAdapter;
    private ConstraintLayout monsterLayout;
    private View monsterNameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster);

        DBFunctions = new DBFunctions(this);
        DBFunctions.UserLoggedInCheck();
        DBFunctions.getMonsters();

        monsterNameFragment = (View) findViewById(R.id.fragment_monster_name);
        monsterLayout = (ConstraintLayout) findViewById(R.id.monsterLayout);

        FloatingActionButton returnButton = (FloatingActionButton) findViewById(R.id.returnButton);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.monster_recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setHasFixedSize(true);

        mMonsterAdapter = new MonsterAdapter(this);
        mRecyclerView.setAdapter(mMonsterAdapter);
        returnButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnButton:
                finish();
                break;
        }
    }

    @Override
    //Add monsters to list and notify the adapter that the data changed.
    public void requestedMonsterList(List<Monster> monsters) {
        mMonsterAdapter.addMonsters(monsters);
        mMonsterAdapter.notifyDataSetChanged();
    }

    @Override
    //If we clicked on a monster check which button was clicked and start functionality associated with it.
    public void onMonsterItemClick(Monster clicked, Boolean release) {
        if(!release){
            Bundle bundle = new Bundle();
            bundle.putString("firebaseId",clicked.returnFirebaseId());

            Fragment newFragment = new MonsterFragment();
            newFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_monster_name, newFragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();

            monsterLayout.setVisibility(View.GONE);
            monsterNameFragment.setVisibility(View.VISIBLE);
        } else {
            DBFunctions.deleteMonster(clicked.returnFirebaseId());
        }
    }

    @Override
    public void requestedJob(boolean JobSuccesful, EnumSuccesCodes code) {
        switch (code){
            case MONSTERNICK:
                if(JobSuccesful){
                    monsterLayout.setVisibility(View.VISIBLE);
                    monsterNameFragment.setVisibility(View.GONE);
                    mMonsterAdapter.resetList();
                    DBFunctions.getMonsters();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(monsterNameFragment.getVisibility() == View.VISIBLE){
            monsterLayout.setVisibility(View.VISIBLE);
            monsterNameFragment.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}
