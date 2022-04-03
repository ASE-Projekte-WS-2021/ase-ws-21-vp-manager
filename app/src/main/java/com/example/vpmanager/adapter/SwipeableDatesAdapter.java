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
import com.google.android.material.snackbar.Snackbar;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
