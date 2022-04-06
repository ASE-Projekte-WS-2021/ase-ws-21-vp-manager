package com.example.vpmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SwipeableDatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context mContext;
    private ArrayList<String> preselectedDates;
    private String mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View mFragmentView;


    //Parameter: context, studyDates, fragmentView
    //Return values:
    //Sets variables
    public SwipeableDatesAdapter(Context context, ArrayList<String> studyDates, View fragmentView) {
        mContext = context;
        preselectedDates = studyDates;
        mFragmentView = fragmentView;
        this.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                preselectedDates = sortByDate(studyDates);
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

    //Parameter:
    //Return values:
    //Removes date item
    public void deleteItem(int position){
        mRecentlyDeletedItem = preselectedDates.get(position);
        mRecentlyDeletedItemPosition = position;
        preselectedDates.remove(position);
        notifyItemRemoved(position);
        showUndoSnackBar();
    }

    //Parameter:
    //Return values:
    //Sets snack bar for cancelling appointments
    private void showUndoSnackBar(){
        View view = mFragmentView.findViewById(R.id.create_step_five_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.removeAppointmentAlert,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.cancelAction, v -> undoDelete());
        snackbar.show();
    }

    //Parameter:
    //Return values:
    //Manages deleted dates
    private void undoDelete(){
        preselectedDates.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }


    //Parameter: toSort
    //Return values: ArrayList<String>
    //Sorts the dates list chronologically
    private ArrayList<String> sortByDate(ArrayList<String> toSort) {
        ArrayList<String> list = new ArrayList<>();

        String[] studyList = new String[toSort.size()];
        for (int i = 0; i < toSort.size(); i++) {
            studyList[i] = toSort.get(i);
        }

        for (int i = 0; i < studyList.length; i++) {
            for (int k = 0; k < studyList.length - 1; k++) {

                String date1 = studyList[k].substring(studyList[k].indexOf(",") + 2);
                String date2 = studyList[k + 1].substring(studyList[k + 1].indexOf(",") + 2);

                date1 = date1.replaceAll("um", "");
                date1 = date1.replaceAll("Uhr", "");
                date2 = date2.replaceAll("um", "");
                date2 = date2.replaceAll("Uhr", "");

                Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                try {

                    Date d1_Date = (Date) format.parseObject(date1);
                    Date d2_Date = (Date) format.parseObject(date2);

                    if (d2_Date.before(d1_Date)) {
                        String tempStudy = studyList[k];
                        studyList[k] = studyList[k + 1];
                        studyList[k + 1] = tempStudy;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        list.clear();
        for (String ob : studyList) {
            list.add(ob);
        }
        return list;
    }

}
