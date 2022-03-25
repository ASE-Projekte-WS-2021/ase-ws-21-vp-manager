package com.example.vpmanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;

import java.util.ArrayList;

public class SwipeableDatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> preselectedDates; //DateModel //only stringList not modelList!

    public SwipeableDatesAdapter(Context context, ArrayList<String> studyDates) {
        mContext = context;
        preselectedDates = studyDates;
        Log.d("SwipeableDatesAdapter", "constructor was called");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //other textViews can be filled with data here
        ((CustomViewHolder) holder).dateDate.setText(preselectedDates.get(position)); //.getDate()
        //((CustomViewHolder) holder).dateOtherInfos.setText(preselectedDates.get(position).getXYZ());
    }

    @Override
    public int getItemCount() {
        return preselectedDates.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView dateDate;
        TextView dateOtherInfos;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            dateDate = itemView.findViewById(R.id.dateItemDate);
            dateOtherInfos = itemView.findViewById(R.id.dateItemLayoutProposal);
        }
    }

}
