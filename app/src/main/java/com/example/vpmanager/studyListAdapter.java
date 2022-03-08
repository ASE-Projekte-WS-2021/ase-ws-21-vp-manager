package com.example.vpmanager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class studyListAdapter extends BaseAdapter {

    findStudyActivity findStudyActivity;
    ArrayList<String> studyNamesAndVps;
    ArrayList<String> nameList;
    ArrayList<String> vpList;
    ArrayList<String> categoryList;
    String studyName;
    String studyVP;
    String category;

    Animation animation;

    /*DocumentReference studyRef;
    FirebaseFirestore db;*/

    /*Class class = new Class();
    class.publicMethod();*/



    public studyListAdapter(findStudyActivity findStudyActivity, ArrayList<String> studyNamesAndVps) {


        this.findStudyActivity = findStudyActivity;
        this.studyNamesAndVps = studyNamesAndVps;


    }


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

        nameList = findStudyActivity.getStudyNames();
        studyName = nameList.get(position);

        vpList = findStudyActivity.getStudyVPs();
        studyVP = vpList.get(position);


        categoryList = findStudyActivity.getStudyCats();
        category = categoryList.get(position);


        /*if (category.equals("AR")) {
            convertView = LayoutInflater.from(findStudyActivity).inflate(R.layout.studylist_cart_ar, parent, false);
        } else {
            convertView = LayoutInflater.from(findStudyActivity).inflate(R.layout.studylist_cart, parent, false);
        }*/

        convertView = LayoutInflater.from(findStudyActivity).inflate(R.layout.studylist_cart, parent, false);

        animation = AnimationUtils.loadAnimation(findStudyActivity, R.anim.animation1);

        TextView nameText;
        TextView vpText;
        TextView categoryText;
        View seperater;


        /*LinearLayout ll_item;
        ll_item = convertView.findViewById(R.id.ll_item);*/
        nameText = convertView.findViewById(R.id.textviewName);
        vpText = convertView.findViewById(R.id.textviewVP);
        categoryText = convertView.findViewById(R.id.textviewCat);

        seperater = convertView.findViewById(R.id.seperater);


        nameText.setAnimation(animation);
        nameText.setText(studyName);

        vpText.setAnimation(animation);
        vpText.setText(studyVP);

        categoryText.setAnimation(animation);
        categoryText.setText(category);

        seperater.setAnimation(animation);


        return convertView;
    }


    private void setupCategoryList() {

    }

}


//hover effect for cards?