package com.example.vpmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.models.StudyMetaInfoModel;

import java.util.ArrayList;

//this adapter adapts the individual recyclerView layouts to the recyclerView container
public class StudyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StudyMetaInfoModel> mStudyMetaInfos; //= new ArrayList<>()
    private Context mContext;
    private OnStudyItemClickListener mOnStudyItemClickListener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.study_list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view, mOnStudyItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CustomViewHolder) holder).studyTitle.setText(mStudyMetaInfos.get(position).getName());
        ((CustomViewHolder) holder).studyVps.setText(mStudyMetaInfos.get(position).getVps());
    }

    @Override
    public int getItemCount() {
        return mStudyMetaInfos.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView studyTitle, studyVps;
        RelativeLayout studyListParentLayout;
        OnStudyItemClickListener onStudyItemClickListener;

        public CustomViewHolder(@NonNull View itemView, OnStudyItemClickListener onStudyItemClickListener) {
            super(itemView);
            this.onStudyItemClickListener = onStudyItemClickListener;

            studyTitle = itemView.findViewById(R.id.studyListStudyTitle);
            studyVps = itemView.findViewById(R.id.studyListStudyVps);
            studyListParentLayout = itemView.findViewById(R.id.studyListParentLayout);

            studyListParentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Get the id of the item that was clicked
            String studyId = mStudyMetaInfos.get(getAdapterPosition()).getId();
            Log.d("StudyListAdapter", "studyId that was clicked:" + studyId);
            onStudyItemClickListener.onStudyClick(studyId);
        }
    }

    public interface OnStudyItemClickListener {
        void onStudyClick(String studyId);
    }
}
