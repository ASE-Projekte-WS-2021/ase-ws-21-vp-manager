package com.example.vpmanager.adapter;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.navigation.NavController;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomListViewAdapterAppointments extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<StudyObject> objects;
    private Activity activity;
    NavController navController;


    private class ViewHolder {
        TextView titleTextView;
        TextView dateTextView;

    }

    public CustomListViewAdapterAppointments(Context context, Activity activity, NavController nav, ArrayList<String> list, HashMap<String, String> getStudyIdByName) {
        super();
        inflater = LayoutInflater.from(context);
        navController = nav;
        this.objects = transformUpcomingAppointments(list, getStudyIdByName);
        this.activity = activity;
    }

    private ArrayList<StudyObject> transformUpcomingAppointments(ArrayList<String> list, HashMap<String, String> getStudyIdByName) {
        ArrayList<StudyObject> returnList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++)
        {
            String[] values = list.get(i).split("\t\t");//0 name 1 date
            String id = getStudyIdByName.get(values[0]);
            assert id != null;
            if(!id.equals("")) {
                StudyObject object = new StudyObject(values[0],null, id, values[1], 0);
                returnList.add(object);
            }
        }
        return returnList;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public StudyObject getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TextView titleView = null, vpsView = null, dateView = null;
        View colorView = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.pa_upcoming_listview_item, null); //NOT SURE

            titleView = convertView.findViewById(R.id.upcoming_listviewItemTitle);
            titleView.setText(objects.get(position).title);

            dateView = convertView.findViewById(R.id.pa_upcoming_date_view);
            dateView.setText(objects.get(position).date);

            convertView.setTag(objects);

            holder.titleTextView = titleView;
            holder.dateTextView = dateView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    StudyObject study = objects.get(position);
                    args.putString("studyId", study.getStudyId());
                    if (PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, study.getStudyId())) {
                        navController.navigate(R.id.action_homeFragment_to_studyCreatorFragment, args);
                    } else {
                        navController.navigate(R.id.action_homeFragment_to_studyFragment, args);
                    }
                }
            });
        }
        return convertView;
    }
}
