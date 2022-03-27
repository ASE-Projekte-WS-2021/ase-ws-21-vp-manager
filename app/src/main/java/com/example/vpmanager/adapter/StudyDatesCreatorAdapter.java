package com.example.vpmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.models.DateModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

public class StudyDatesCreatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DateModel> mStudyDates;
    private OnDateClickListener mOnDateClickListener;

    public StudyDatesCreatorAdapter(Context context, ArrayList<DateModel> studyDates, OnDateClickListener onDateClickListener) {
        mContext = context;
        mStudyDates = studyDates;
        mOnDateClickListener = onDateClickListener;
        Log.d("StudyDatesAdapter", "constructor was called");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_creator_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view, mContext);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //other textViews can be filled with data here
        ((CustomViewHolder) holder).dateDate.setText(mStudyDates.get(position).getDate());
        ((CustomViewHolder) holder).dateUserId.setText(mStudyDates.get(position).getUserId());

        if( ((CustomViewHolder) holder).dateUserId.getText().toString().isEmpty())
        {  ((CustomViewHolder) holder).stateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mStudyDates.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;

        TextView dateDate;
        TextView dateUserId;
        LinearLayout stateLayout;

        TextView participatedButton, notParticipatedButton;
        CardView border_participated, border_not_participated;

        private boolean participated, notParticipated;

        public CustomViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            dateDate = itemView.findViewById(R.id.dateCreatorItemDate);
            dateUserId = itemView.findViewById(R.id.dateCreatorItemUserId);
            stateLayout = itemView.findViewById(R.id.dateStateLayout);
            participatedButton = itemView.findViewById(R.id.dateParticipatedButton);
            notParticipatedButton = itemView.findViewById(R.id.dateNotParticipatedButton);
            border_participated = itemView.findViewById(R.id.dateCreatorBorderCard_participated);
            border_not_participated = itemView.findViewById(R.id.dateCreatorBorderCard_notParticipated);

            participated = false;
            notParticipated = false;

            participatedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /** Push to database **/
                    notParticipated = false;
                    participated = !participated;
                    if(participated)
                    {
                        border_participated.setCardBackgroundColor(context.getResources().getColor(R.color.green_dark));
                        participatedButton.setTextColor(context.getResources().getColor(R.color.green_dark));
                        border_not_participated.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
                        notParticipatedButton.setTextColor(context.getResources().getColor(R.color.grey));
                    }
                    else
                    {
                        border_participated.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
                        participatedButton.setTextColor(context.getResources().getColor(R.color.grey));
                    }
                }
            });

            notParticipatedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /** Push to database**/
                    notParticipated = !notParticipated;
                    participated = false;
                    if(notParticipated)
                    {
                        border_participated.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
                        participatedButton.setTextColor(context.getResources().getColor(R.color.grey));
                        border_not_participated.setCardBackgroundColor(context.getResources().getColor(R.color.heatherred_dark));
                        notParticipatedButton.setTextColor(context.getResources().getColor(R.color.heatherred_dark));
                    }
                    else
                    {
                        border_not_participated.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
                        notParticipatedButton.setTextColor(context.getResources().getColor(R.color.grey));
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnDateClickListener {
        void onDateClick(String dateId);
    }
}
