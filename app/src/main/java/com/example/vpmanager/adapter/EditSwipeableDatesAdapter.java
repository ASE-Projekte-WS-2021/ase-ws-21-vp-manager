package com.example.vpmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.models.DateModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class EditSwipeableDatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    private ArrayList<DateModel> editDatesList;

    private DateModel mRecentlyDeletedDate;
    private int mRecentlyDeletedDatePosition;
    private View mFragmentView;

    public EditSwipeableDatesAdapter(Context context, ArrayList<DateModel> studyDates, View fragmentView) {
        mContext = context;
        editDatesList = studyDates;
        mFragmentView = fragmentView;
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
        ((CustomViewHolder) holder).dateDate.setText(editDatesList.get(position).getDate());
        //((CustomViewHolder) holder).dateOtherInfos.setText(editDatesList.get(position).getXYZ());
    }

    @Override
    public int getItemCount() {
        return editDatesList.size();
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

    public void deleteItem(int position){
        mRecentlyDeletedDate = editDatesList.get(position);
        mRecentlyDeletedDatePosition = position;
        editDatesList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackBar();
    }

    private void showUndoSnackBar(){
        View view = mFragmentView.findViewById(R.id.edit_study_dates_layout);
        Snackbar snackbar = Snackbar.make(view, "1 Termin gelöscht",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Rückgängig", v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete(){
        editDatesList.add(mRecentlyDeletedDatePosition, mRecentlyDeletedDate);
        notifyItemInserted(mRecentlyDeletedDatePosition);
    }
}
