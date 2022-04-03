package com.example.vpmanager.models;

import com.example.vpmanager.adapter.CustomListViewAdapter;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class DateModel {

    private String dateId, date, studyId, userId;
    private Boolean selected, participated;

    public DateModel(String dateId, String date, String studyId, String userId, Boolean selected, Boolean participated) {

        this.dateId = dateId;
        this.date = date;
        this.studyId = studyId;
        this.userId = userId;
        this.selected = selected;
        this.participated = participated;

    }

    public DateModel(){

    }

    public String getDateId() {
        return dateId;
    }

    public String getDate() {
        return date;
    }

    public String getStudyId() {
        return studyId;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getSelected(){
        return selected;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSelected(Boolean selected){
        this.selected = selected;
    }

    public boolean getParticipation() { return participated; }

    public void setParticipation(boolean participated) { this.participated = participated;}

    public static ArrayList<DateModel> sortByDate(ArrayList<DateModel> toSort) {
        ArrayList<DateModel> list = new ArrayList<>();

        DateModel[] dateList = new DateModel[toSort.size()];
        for (int i = 0; i < toSort.size(); i++) {
            dateList[i] = toSort.get(i);
        }

        for (int i = 0; i < dateList.length; i++) {
            for (int k = 0; k < dateList.length - 1; k++) {

                String date1 = dateList[k].getDate().substring(dateList[k].getDate().indexOf(",") + 2);
                String date2 = dateList[k + 1].getDate().substring(dateList[k + 1].getDate().indexOf(",") + 2);

                date1 = date1.replaceAll("um", "");
                date1 = date1.replaceAll("Uhr", "");
                date2 = date2.replaceAll("um", "");
                date2 = date2.replaceAll("Uhr", "");

                Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                try {

                    Date d1_Date = (Date) format.parseObject(date1);
                    Date d2_Date = (Date) format.parseObject(date2);

                    if (d2_Date.before(d1_Date)) {
                        DateModel tempDate = dateList[k];
                        dateList[k] = dateList[k + 1];
                        dateList[k + 1] = tempDate;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for (DateModel dm : dateList) {
            list.add(dm);
        }
        return list;
    }

}
