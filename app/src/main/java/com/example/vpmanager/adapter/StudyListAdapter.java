package com.example.vpmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public ArrayList<StudyMetaInfoModel> mStudyMetaInfos;
    private OnStudyItemClickListener mOnStudyItemClickListener;

    //Parameter: context, studyMetaInfos, onStudyItemCLickListener
    //Return values:
    //Sets variables
    public StudyListAdapter(Context context, ArrayList<StudyMetaInfoModel> studyMetaInfos,
                            OnStudyItemClickListener onStudyItemClickListener) {
        mContext = context;
        mStudyMetaInfos = studyMetaInfos;
        mOnStudyItemClickListener = onStudyItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studylist, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view, mOnStudyItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((CustomViewHolder) holder).studyTitle.setText(mStudyMetaInfos.get(position).getName());

        if (mStudyMetaInfos.get(position).getVps() != null && mStudyMetaInfos.get(position).getVps().contains("null"))
            mStudyMetaInfos.get(position).setVps("0 VP-Stunden");

        if (mStudyMetaInfos.get(position).getVps() != null && !mStudyMetaInfos.get(position).getVps().contains("VP-Stunden"))
            mStudyMetaInfos.get(position).setVps(mStudyMetaInfos.get(position).getVps() + " VP-Stunden");

        ((CustomViewHolder) holder).studyVps.setText(mStudyMetaInfos.get(position).getVps());
        ((CustomViewHolder) holder).studyCat.setText(mStudyMetaInfos.get(position).getCategory());
        ((CustomViewHolder) holder).setIcon(mStudyMetaInfos.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return mStudyMetaInfos.size();
    }


    private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView studyTitle, studyVps, studyCat, studyTag;
        ImageView locationIcon, remoteIcon;
        LinearLayout studyItemParentLayout;
        OnStudyItemClickListener onStudyItemClickListener;


        //Parameter: itemView, onStudyItemClickListener
        //Return values:
        //Sets items for the CustomViewHolder
        public CustomViewHolder(@NonNull View itemView, OnStudyItemClickListener onStudyItemClickListener) {
            super(itemView);
            this.onStudyItemClickListener = onStudyItemClickListener;

            studyTitle = itemView.findViewById(R.id.textviewName);

            studyVps = itemView.findViewById(R.id.textviewVP);

            studyTag = itemView.findViewById(R.id.textviewTag);

            studyCat = itemView.findViewById(R.id.textviewCat);

            locationIcon = itemView.findViewById(R.id.localSymbolStudyFragment);

            remoteIcon = itemView.findViewById(R.id.remoteSymbolStudyFragment);

            studyItemParentLayout = itemView.findViewById(R.id.ll_item);
            studyItemParentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Get the id of the item that was clicked
            String studyId = mStudyMetaInfos.get(getAdapterPosition()).getId();
            onStudyItemClickListener.onStudyClick(studyId);
        }


        //Parameter: type
        //Return values:
        //Sets the icon depending on the study type
        public void setIcon(String type) {
            if (type.equals("Präsenz")) {
                locationIcon.setVisibility(View.VISIBLE);
                remoteIcon.setVisibility(View.INVISIBLE);
            } else {
                remoteIcon.setVisibility(View.VISIBLE);
                locationIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    //Parameter:
    //Return values:
    //interface to get studyId
    public interface OnStudyItemClickListener {
        void onStudyClick(String studyId);
    }

}

