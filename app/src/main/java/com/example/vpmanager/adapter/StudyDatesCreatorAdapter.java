package com.example.vpmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.models.DateModel;

import java.util.ArrayList;

public class StudyDatesCreatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<DateModel> mStudyDates;
    private OnDateClickListener mOnDateClickListener;



    //Parameter: context, studyDates, onDareCLickListener
    //Return values:
    //Sets variables
    public StudyDatesCreatorAdapter(Context context, ArrayList<DateModel> studyDates, OnDateClickListener onDateClickListener) {
        mContext = context;
        mStudyDates = DateModel.sortByDate(studyDates);
        mOnDateClickListener = onDateClickListener;
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
        ((CustomViewHolder) holder).participated = mStudyDates.get(position).getParticipation();

        ((CustomViewHolder) holder).setParticipationButtons(((CustomViewHolder) holder).participated);

        if (((CustomViewHolder) holder).dateUserId.getText().toString().isEmpty()) {
            ((CustomViewHolder) holder).stateLayout.setVisibility(View.GONE);
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

        TextView participatedButton;
        CardView border_participated;

        private boolean participated;


        //Parameter: itemView, onStudyItemClickListener
        //Return values:
        //Sets items and Listeners for the CustomViewHolder
        public CustomViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            dateDate = itemView.findViewById(R.id.dateCreatorItemDate);
            dateUserId = itemView.findViewById(R.id.dateCreatorItemUserId);
            stateLayout = itemView.findViewById(R.id.dateStateLayout);
            participatedButton = itemView.findViewById(R.id.dateParticipatedButton);
            border_participated = itemView.findViewById(R.id.dateCreatorBorderCard_participated);

            participatedButton.setOnClickListener(v -> {

                participated = !participated;

                if (participated) {
                    participatedButton.setTextColor(context.getResources().getColor(R.color.green_dark));
                    participatedButton.setText(R.string.participated);
                    border_participated.setCardBackgroundColor(context.getResources().getColor(R.color.green_dark));
                } else {
                    participatedButton.setTextColor(context.getResources().getColor(R.color.heatherred_dark));
                    participatedButton.setText(R.string.notParticipated);
                    border_participated.setCardBackgroundColor(context.getResources().getColor(R.color.heatherred_dark));
                }
                PA_ExpandableListDataPump.setDateState(mStudyDates.get(getAdapterPosition()).getDateId(), participated);
            });
        }


        //Parameter: participation
        //Return values:
        //Checks participation status
        public void setParticipationButtons(boolean participation) {
            if (participation) {
                border_participated.setCardBackgroundColor(context.getResources().getColor(R.color.green_dark));
                participatedButton.setText(R.string.participated);
                participatedButton.setTextColor(context.getResources().getColor(R.color.green_dark));

            } else {
                border_participated.setCardBackgroundColor(context.getResources().getColor(R.color.heatherred_dark));
                participatedButton.setTextColor(context.getResources().getColor(R.color.heatherred_dark));
                participatedButton.setText(R.string.notParticipated);
            }
        }

        @Override
        public void onClick(View view) {
        }
    }

    public interface OnDateClickListener {
        void onDateClick(String dateId);
    }
}
