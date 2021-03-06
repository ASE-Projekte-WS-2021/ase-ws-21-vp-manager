package com.example.vpmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.helper.AccessDatabaseHelper;
import com.example.vpmanager.R;
import com.example.vpmanager.models.StudyObjectPa;
import com.example.vpmanager.views.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//this adapter adapts the individual recyclerView layouts to the recyclerView container
public class CustomStudyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public ArrayList<StudyObjectPa> mStudyMetaInfos;
    private NavController navController;
    private Animation animation;


    //Parameter: context, navController, studyMetaInfos
    //Return values:
    //Sets the required adapter elements
    public CustomStudyListAdapter(Context context, NavController navController, ArrayList<StudyObjectPa> studyMetaInfos) {
        mContext = context;
        mStudyMetaInfos = studyMetaInfos;
        this.navController = navController;
    }

    public CustomStudyListAdapter(Context context, NavController navController, HashMap<String, List<String>> studyMetaInfos) {
        mContext = context;
        mStudyMetaInfos = transformLists(studyMetaInfos);
        this.navController = navController;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pa_listview, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((CustomViewHolder) holder).studyTitle.setText(mStudyMetaInfos.get(position).getTitle());

        if (mStudyMetaInfos.get(position).getVps() != null && mStudyMetaInfos.get(position).getVps().contains("null"))
            mStudyMetaInfos.get(position).setVps("0 VP-Stunden");

        if (mStudyMetaInfos.get(position).getVps() != null && !mStudyMetaInfos.get(position).getVps().contains("VP-Stunden"))
            mStudyMetaInfos.get(position).setVps(mStudyMetaInfos.get(position).getVps() + " VP-Stunden");

        ((CustomViewHolder) holder).studyVps.setText(mStudyMetaInfos.get(position).getVps());
        ((CustomViewHolder) holder).studyDate.setText(mStudyMetaInfos.get(position).getDate());
        ((CustomViewHolder) holder).colorView.setBackgroundResource(mStudyMetaInfos.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return mStudyMetaInfos.size();
    }


    private class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView studyTitle, studyVps, studyDate;
        CardView studyItemParentLayout;
        View colorView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            animation = AnimationUtils.loadAnimation(mContext, R.anim.animation);

            studyTitle = itemView.findViewById(R.id.listviewItemTitle);

            studyVps = itemView.findViewById(R.id.pa_VP_view);

            studyDate = itemView.findViewById(R.id.pa_date_view);

            colorView = itemView.findViewById(R.id.pa_listView_colorTag);

            studyItemParentLayout = itemView.findViewById(R.id.pa_item_card_parent);

            studyItemParentLayout.setOnClickListener(v -> {
                String studyId = mStudyMetaInfos.get(getAdapterPosition()).getStudyId();
                AccessDatabaseHelper.navigateToStudyCreatorFragment(MainActivity.uniqueID, studyId, "OwnStudyFragment", navController);
            });
        }
    }
}

