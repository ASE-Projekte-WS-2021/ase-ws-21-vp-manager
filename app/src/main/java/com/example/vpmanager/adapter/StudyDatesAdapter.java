package com.example.vpmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.models.DateModel;

import java.util.ArrayList;

public class StudyDatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DateModel> mStudyDates;
    private OnDateClickListener mOnDateClickListener;

    public StudyDatesAdapter(Context context, ArrayList<DateModel> studyDates, OnDateClickListener onDateClickListener) {
        mContext = context;
        mStudyDates = studyDates;
        mOnDateClickListener = onDateClickListener;
        Log.d("StudyDatesAdapter", "constructor was called");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view, mOnDateClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //other textViews can be filled with data here
        ((CustomViewHolder) holder).dateDate.setText(mStudyDates.get(position).getDate());
        //((CustomViewHolder) holder).dateOtherInfos.setText(mStudyDates.get(position).getXYZ());
    }

    @Override
    public int getItemCount() {
        return mStudyDates.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateDate;
        TextView dateOtherInfos;
        LinearLayout dateItemParentLayout;
        OnDateClickListener onDateClickListener;

        public CustomViewHolder(@NonNull View itemView, OnDateClickListener onDateClickListener) {
            super(itemView);
            this.onDateClickListener = onDateClickListener;

            dateDate = itemView.findViewById(R.id.dateItemDate);
            dateOtherInfos = itemView.findViewById(R.id.dateItemLayoutProposal);

            if (this.onDateClickListener != null) {
                dateItemParentLayout = itemView.findViewById(R.id.dateItemParentLayout);
                dateItemParentLayout.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            //Get the id of the date that was clicked on
            String dateId = mStudyDates.get(getAdapterPosition()).getDateId();
            Log.d("StudyDatesAdapter", "dateId that was clicked on:" + dateId);
            onDateClickListener.onDateClick(dateId);
        }
    }

    public interface OnDateClickListener {
        void onDateClick(String dateId);
    }
}
