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


    //Parameter: context, studyDates, fragmentView
    //Return values:
    //Sets variables
    public EditSwipeableDatesAdapter(Context context, ArrayList<DateModel> studyDates, View fragmentView) {
        mContext = context;
        editDatesList = DateModel.sortByDate(studyDates);
        mFragmentView = fragmentView;
        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                editDatesList = DateModel.sortByDate(studyDates);
            }
        });
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


    //Parameter: position
    //Return values:
    //Deletes associated date item
    public void deleteItem(int position) {
        mRecentlyDeletedDate = editDatesList.get(position);
        if(mRecentlyDeletedDate.getSelected() && mRecentlyDeletedDate.getUserId() != null)
        {
            View view = mFragmentView.findViewById(R.id.edit_study_dates_layout);
            Snackbar snackbar = Snackbar.make(view, R.string.removeAppointmentnotPossible,
                    Snackbar.LENGTH_LONG);
            snackbar.show();
            notifyDataSetChanged();
        }
        else {
            mRecentlyDeletedDatePosition = position;
            editDatesList.remove(position);
            notifyItemRemoved(position);
            showUndoSnackBar();
        }
    }


    //Parameter:
    //Return values:
    //Sets snack bar for cancelling appointments
    private void showUndoSnackBar() {
        View view = mFragmentView.findViewById(R.id.edit_study_dates_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.removeAppointmentAlert,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.cancelAction, v -> undoDelete());
        snackbar.show();
    }


    //Parameter:
    //Return values:
    //Manages deleted dates
    private void undoDelete() {
        editDatesList.add(mRecentlyDeletedDatePosition, mRecentlyDeletedDate);
        notifyItemInserted(mRecentlyDeletedDatePosition);
    }
}
