package com.example.vpmanager.adapter;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.content.Context;
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

public class CustomListViewAdapterAppointments extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<StudyObjectPa> objects;
    //private Activity activity;
    NavController navController;
    private String sourceFragment;

    private class ViewHolder {
        TextView titleTextView;
        TextView dateTextView;

    }


    //Parameter: context, nav, list, Hashmap<String, getStudyIdByName, source
    //Return values:
    //Sets the required adapter elements
    public CustomListViewAdapterAppointments(Context context, NavController nav, ArrayList<String> list, HashMap<String, String> getStudyIdByName, String source) {
        super();
        inflater = LayoutInflater.from(context);
        navController = nav;
        this.objects = transformUpcomingAppointments(list, getStudyIdByName);
        //this.activity = activity;
        sourceFragment = source;
    }


    //Parameter: list, Hashmap<String, getStudyIdByName
    //Return values: ArrayList<StudyObjectPa>
    //Fills the ArrayList for upcoming appointments
    private ArrayList<StudyObjectPa> transformUpcomingAppointments(ArrayList<String> list, HashMap<String, String> getStudyIdByName) {
        ArrayList<StudyObjectPa> returnList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String[] values = list.get(i).split("\t\t");//0 name 1 date
            String id = getStudyIdByName.get(values[0]);
            assert id != null;
            if (!id.equals("")) {
                StudyObjectPa object = new StudyObjectPa(values[0], null, id, values[1], 0);

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
            convertView = inflater.inflate(R.layout.pa_upcoming_listview_item, null); //NOT SURE

            titleView = convertView.findViewById(R.id.upcoming_listviewItemTitle);

            titleView.setText(objects.get(position).getTitle()); //title

            dateView = convertView.findViewById(R.id.pa_upcoming_date_view);
            dateView.setText(objects.get(position).getDate()); //date

            convertView.setTag(objects);

            holder.titleTextView = titleView;
            holder.dateTextView = dateView;

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StudyObjectPa study = objects.get(position);
                    PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, study.getStudyId(), sourceFragment, navController);
                }
            });
        }
        return convertView;
    }
}
