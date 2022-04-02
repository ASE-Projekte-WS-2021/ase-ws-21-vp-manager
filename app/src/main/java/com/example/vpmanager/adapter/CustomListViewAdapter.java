package com.example.vpmanager.adapter;

import static com.example.vpmanager.views.mainActivity.uniqueID;

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
import com.example.vpmanager.models.StudyObjectPa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    public ArrayList<StudyObjectPa> objects;
    //private Activity activity;
    private NavController navController;


    private class ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView vpsTextView;
        View colorView;

    }

    public CustomListViewAdapter(Context context,  NavController nav, HashMap<String, List<String>> list) { //Activity activity,
        super();
        inflater = LayoutInflater.from(context);
        navController = nav;
        this.objects = transformLists(list);
        //this.activity = activity;
    }

    public CustomListViewAdapter(Context context,  NavController nav, ArrayList<StudyObjectPa> list) { //Activity activity,
        super();
        inflater = LayoutInflater.from(context);
        navController = nav;
        this.objects = list;
        //this.activity = activity;
    }

    private ArrayList<StudyObjectPa> transformLists(HashMap<String, List<String>> hashMapList) {
        ArrayList<StudyObjectPa> list = new ArrayList<>();
        HashMap<String, List<String>> dataList = hashMapList;

        for (String key : dataList.keySet()) {
            List<String> tempList = dataList.get(key);

            for (int i = 0; i < (tempList != null ? tempList.size() : 0); i++) {
                String[] values = tempList.get(i).split(";");
                StudyObjectPa object = null;

                switch (key) {
                    case "Teilgenommene Studien":
                        object = new StudyObjectPa(values[0], values[1], values[3], values[2], R.color.pieChartParticipation);
                        break;
                    case "Geplante Studien":
                        object = new StudyObjectPa(values[0], values[1], values[3], values[2], R.color.pieChartPlanned);
                        break;
                    case "Abgeschlossene Studien":
                        object = new StudyObjectPa(values[0], values[1], values[3], values[2], R.color.pieChartSafe);
                        break;
                    default:
                        object = new StudyObjectPa(values[0], values[1], values[3], values[2], R.color.heatherred_dark);
                        break;
                }
                if (object != null) {
                    list.add(object);
                }
            }
        }
        return list;
    }
    public ArrayList<StudyObjectPa> getObjects()
    {
        return objects;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public StudyObjectPa getItem(int position) {
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
            convertView = inflater.inflate(R.layout.pa_listview_item, null); //NOT SURE

            titleView = convertView.findViewById(R.id.listviewItemTitle);
            titleView.setText(objects.get(position).getTitle());

            vpsView = convertView.findViewById(R.id.pa_VP_view);
            vpsView.setText(objects.get(position).getVps());

            dateView = convertView.findViewById(R.id.pa_date_view);
            dateView.setText(objects.get(position).getDate());

            colorView = convertView.findViewById(R.id.pa_listView_colorTag);
            colorView.setBackgroundResource(objects.get(position).getColor());


            convertView.setTag(objects.get(position));

            holder.titleTextView = titleView;
            holder.dateTextView = dateView;
            holder.vpsTextView = vpsView;
            holder.colorView = colorView;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    StudyObjectPa study = objects.get(position);
                    args.putString("studyId", study.getStudyId());
                    PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, study.getStudyId(), "OwnStudyFragment", navController);
                }});
        }
        return convertView;
    }
}
