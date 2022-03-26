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

import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.views.createStudy.createStudyFragment_StepFive;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SwipeableDatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    private ArrayList<String> preselectedDates; //DateModel //only stringList not modelList!

    //new with swipeToDelete
    private String mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View mFragmentView;

    public SwipeableDatesAdapter(Context context, ArrayList<String> studyDates, View fragmentView) {
        mContext = context;
        preselectedDates = studyDates;
        mFragmentView = fragmentView;
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

    //new with swipeToDelete
    public void deleteItem(int position){
        mRecentlyDeletedItem = preselectedDates.get(position);
        mRecentlyDeletedItemPosition = position;
        preselectedDates.remove(position);
        notifyItemRemoved(position);
        showUndoSnackBar();
    }

    //new with swipeToDelete
    private void showUndoSnackBar(){
        View view = mFragmentView.findViewById(R.id.create_step_five_layout);
        Snackbar snackbar = Snackbar.make(view, "1 Termin gelöscht",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Rückgängig", v -> undoDelete());
        snackbar.show();
    }

    //new with swipeToDelete
    private void undoDelete(){
        preselectedDates.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}
