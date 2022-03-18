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
import java.util.List;

public class CustomListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<StudyObject> objects;
    private Activity activity;
    NavController navController;


    private class ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView vpsTextView;
        View colorView;

    }

    public CustomListViewAdapter(Context context, Activity activity, NavController nav) {
        super();
        inflater = LayoutInflater.from(context);
        navController = nav;
        this.objects = transformLists();
        this.activity = activity;
    }

    private ArrayList<StudyObject> transformLists() {
        ArrayList<StudyObject> list = new ArrayList<>();
        HashMap<String, List<String>> dataList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL;

        for (String key : dataList.keySet()) {
            List<String> tempList = dataList.get(key);

            for (int i = 0; i < (tempList != null ? tempList.size() : 0); i++) {
                String[] values = tempList.get(i).split(",");
                StudyObject object = null;

                switch (key) {
                    case "Vergangene Studien":
                        object = new StudyObject(values[0], values[1], values[3], values[2], R.color.pieChartParticipation);
                        break;
                    case "Geplante Studien":
                        object = new StudyObject(values[0], values[1], values[3], values[2], R.color.pieChartPlanned);
                        break;
                    case "Abgeschlossene Studien":
                        object = new StudyObject(values[0], values[1], values[3], values[2], R.color.pieChartSafe);
                        break;
                    default:
                        object = new StudyObject(values[0], values[1], values[3], values[2], R.color.heatherred_dark);
                        break;
                }
                if (object != null) {
                    list.add(object);
                }
            }
        }
        return list;
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
            convertView = inflater.inflate(R.layout.pa_listview_item, null); //NOT SURE

            titleView = convertView.findViewById(R.id.listviewItemTitle);
            titleView.setText(objects.get(position).title);

            vpsView = convertView.findViewById(R.id.pa_VP_view);
            vpsView.setText(objects.get(position).vps);

            dateView = convertView.findViewById(R.id.pa_date_view);
            dateView.setText(objects.get(position).date);

            colorView = convertView.findViewById(R.id.pa_listView_colorTag);
            colorView.setBackgroundResource(objects.get(position).color);


            convertView.setTag(objects);

            holder.titleTextView = titleView;
            holder.dateTextView = dateView;
            holder.vpsTextView = vpsView;
            holder.colorView = colorView;
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
class StudyObject
{
    String title;
    String vps;
    String studyId;
    String date;
    int color;

    public StudyObject(String _title, String _vps, String _studyId, String _date, int _color)
    {
        title = _title;
        vps = _vps;
        studyId = _studyId;
        date = _date;
        color = _color;
    }

    public String getTitle()
    {
        return title;
    }
    public String getDate()
    {
        return date;
    }
    public String getVps()
    {
        return vps;
    }
    public String getStudyId()
    {
        return studyId;
    }
    public int getColor()
    {
        return color;
    }
}