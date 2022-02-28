package com.example.vpmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class studyListAdapter extends BaseAdapter {

    findStudyActivity findStudyActivity;
    ArrayList<String> studyNamesAndVps;
    Animation animation;







    public studyListAdapter(findStudyActivity findStudyActivity, ArrayList<String> studyNamesAndVps) {

//        LocalBroadcastManager.getInstance().registerReceiver(mReceiver, new IntentFilter("testintent"));

        this.findStudyActivity = findStudyActivity;
        this.studyNamesAndVps = studyNamesAndVps;

    }

  /*  private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedHexColor = intent.getStringArrayExtra(studylist);
        }
    };*/


    @Override
    public int getCount() {
        return studyNamesAndVps.toArray().length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String test = studyNamesAndVps.get(position);

        convertView = LayoutInflater.from(findStudyActivity).inflate(R.layout.studylist_cart, parent, false);
        animation = AnimationUtils.loadAnimation(findStudyActivity, R.anim.animation1);

        TextView textView;

        /*LinearLayout ll_item;
        ll_item = convertView.findViewById(R.id.ll_item);*/
        textView = convertView.findViewById(R.id.textview);




        //hover effect for cards!
        //add nav drawer background (in xml unten)




        textView.setAnimation(animation);
        textView.setText(test);






        return convertView;
    }
}
