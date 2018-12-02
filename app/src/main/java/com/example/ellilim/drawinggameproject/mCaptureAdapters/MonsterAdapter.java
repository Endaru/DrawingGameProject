package com.example.ellilim.drawinggameproject.mCaptureAdapters;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ellilim.drawinggameproject.R;
import com.example.ellilim.drawinggameproject.firebaseFiles.Monster;

import java.util.ArrayList;
import java.util.List;

//Adapter that is used to fill the RecyclerView with monsters.
public class MonsterAdapter extends RecyclerView.Adapter<MonsterAdapter.MonsterAdapterViewHolder>{

    private MonsterAdapterOnClickHandler mClickHandler;
    private List<Monster> mMonsterList;

    //Interface that needs to be implemented when the MonsterAdapter is called
    public interface MonsterAdapterOnClickHandler{
        void onMonsterItemClick(Monster clicked,Boolean release);
    }

    //Call when a new MonsterAdapter is created.
    public MonsterAdapter(MonsterAdapterOnClickHandler param){
        mClickHandler = param;
        mMonsterList = new ArrayList<>();
    }

    //Functionality to add monsters to the list.
    public void addMonsters(List<Monster> monsters){
        mMonsterList.addAll(monsters);
    }

    //Empty the list, for example when a monster is deleted.
    public void resetList(){
        mMonsterList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MonsterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIdForListItem = R.layout.monster_list_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new MonsterAdapterViewHolder(inflater.inflate(layoutIdForListItem,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MonsterAdapterViewHolder monsterAdapterViewHolder, int i) {
        monsterAdapterViewHolder.mMonsterTextView.setText(mMonsterList.get(i).returnMonsterName());

        //Cut of the date to just extend to Date + Time
        String[] stringParts = mMonsterList.get(i).returnCaptureDate().split("(?=G)");
        monsterAdapterViewHolder.mDateTextView.setText(stringParts[0]);
    }

    @Override
    public int getItemCount() {
        return mMonsterList.size();
    }

    //Create the adapterViewHolder inside the adapter, mostly because code is very minimal and we wont be using it anywhere else.
    public class MonsterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView mMonsterTextView;
        private final TextView mDateTextView;

        private MonsterAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mMonsterTextView = (TextView) itemView.findViewById(R.id.tv_monster_data);
            mDateTextView = (TextView) itemView.findViewById(R.id.tv_capture_date);
            ImageButton mEditImageButton = (ImageButton) itemView.findViewById(R.id.edit_monster);
            ImageButton mReleaseImageButton = (ImageButton) itemView.findViewById(R.id.release_monster);

            mEditImageButton.setOnClickListener(this);
            mReleaseImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edit_monster:
                    mClickHandler.onMonsterItemClick(mMonsterList.get(getAdapterPosition()),false);
                    break;
                case R.id.release_monster:
                    mClickHandler.onMonsterItemClick(mMonsterList.get(getAdapterPosition()),true);
                    mMonsterList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}
