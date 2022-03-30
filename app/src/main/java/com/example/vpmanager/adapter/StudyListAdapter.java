package com.example.vpmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.models.StudyMetaInfoModel;

import java.util.ArrayList;

//this adapter adapts the individual recyclerView layouts to the recyclerView container
public class StudyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<StudyMetaInfoModel> mStudyMetaInfos;
    private OnStudyItemClickListener mOnStudyItemClickListener;
    //maybe cancel animation?
    private Animation animation;


    public StudyListAdapter(Context context, ArrayList<StudyMetaInfoModel> studyMetaInfos,
                            OnStudyItemClickListener onStudyItemClickListener) {
        mContext = context;
        mStudyMetaInfos = studyMetaInfos;
        mOnStudyItemClickListener = onStudyItemClickListener;
        Log.d("StudyListAdapter", "constructor was called");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studylist_cart, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view, mOnStudyItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CustomViewHolder) holder).studyTitle.setText(mStudyMetaInfos.get(position).getName());
        ((CustomViewHolder) holder).studyVps.setText(mStudyMetaInfos.get(position).getVps());
        ((CustomViewHolder) holder).studyCat.setText(mStudyMetaInfos.get(position).getCategory());
        ((CustomViewHolder)holder).setIcon(mStudyMetaInfos.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return mStudyMetaInfos.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView studyTitle, studyVps, studyCat, studyTag;
        String studyType;
        ImageView locationIcon, remoteIcon;
        View separator;
        LinearLayout studyItemParentLayout;
        OnStudyItemClickListener onStudyItemClickListener;

        public CustomViewHolder(@NonNull View itemView, OnStudyItemClickListener onStudyItemClickListener) {
            super(itemView);
            this.onStudyItemClickListener = onStudyItemClickListener;
            //Animation
            animation = AnimationUtils.loadAnimation(mContext, R.anim.animation);

            studyTitle = itemView.findViewById(R.id.textviewName);
            studyTitle.setAnimation(animation);

            studyVps = itemView.findViewById(R.id.textviewVP);
            studyVps.setAnimation(animation);

            studyTag = itemView.findViewById(R.id.textviewTag);
            studyTag.setAnimation(animation);

            studyCat = itemView.findViewById(R.id.textviewCat);
            studyCat.setAnimation(animation);

            locationIcon = itemView.findViewById(R.id.locationSymbolStudyFragment);
            locationIcon.setAnimation(animation);

            locationIcon = itemView.findViewById(R.id.remoteSymbolStudyFragment);
            remoteIcon.setAnimation(animation);



            /*separator = itemView.findViewById(R.id.separator);
            separator.setAnimation(animation);*/

            studyItemParentLayout = itemView.findViewById(R.id.ll_item);

            studyItemParentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Get the id of the item that was clicked
            String studyId = mStudyMetaInfos.get(getAdapterPosition()).getId();
            Log.d("StudyListAdapter", "studyId that was clicked:" + studyId);
            onStudyItemClickListener.onStudyClick(studyId);
        }

        public void setIcon(String type) {
            if(type.equals("Präsenz")){
                locationIcon.setVisibility(View.VISIBLE);
            } else{
                remoteIcon.setVisibility(View.VISIBLE);
        }
        }



    }

    public interface OnStudyItemClickListener {
        void onStudyClick(String studyId);
    }


}

