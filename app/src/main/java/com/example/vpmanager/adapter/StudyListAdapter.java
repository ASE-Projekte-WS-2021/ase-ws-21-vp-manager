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
import java.util.List;

//this adapter adapts the individual recyclerView layouts to the big recyclerView container
public class StudyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<StudyMetaInfoModel> mStudyMetaInfos = new ArrayList<>();
    private OnStudyItemClickListener mOnStudyItemClickListener;

    public StudyListAdapter(Context context, List<StudyMetaInfoModel> studyMetaInfos,
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

        ((CustomViewHolder)holder).studyTitle.setText(mStudyMetaInfos.get(position).getName());
        //holder.studyTitle.setText(mStudyTitle.get(position));

        ((CustomViewHolder)holder).studyVps.setText(mStudyMetaInfos.get(position).getVps());
        //holder.studyVps.setText(mStudyVps.get(position));

        /*
        ((ViewHolder)holder).studyListParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle click on a study item
                //gets the position of the clicked item
                int currentPosition = holder.getAdapterPosition();
                Log.d("Adapter CurrentPosition", String.valueOf(currentPosition));
                String studyId = mStudyMetaInfos.get(currentPosition).getId();
                Log.d("Adapter StudyId", studyId);
                //findStudyFragment.navigateToStudyDetail(studyId);
            }
        });
         */
    }

    //Tells the adapter how many items are in the list
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
            String studyId = mStudyMetaInfos.get(getAdapterPosition()).getId();
            Log.d("StudyListAdapter", "studyId that was clicked:" + studyId);
            onStudyItemClickListener.onStudyClick(studyId);
        }
    }

    public interface OnStudyItemClickListener {
        void onStudyClick(String studyId); //int position ganz normal wenn id erst in findStudyFragment geholt wird!
    }
}
