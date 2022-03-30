package com.example.vpmanager.models;

public class DateModel {

    private String dateId, date, studyId, userId;
    private Boolean selected;

    public DateModel(String dateId, String date, String studyId, String userId, Boolean selected) {
        this.dateId = dateId;
        this.date = date;
        this.studyId = studyId;
        this.userId = userId;
        this.selected = selected;
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
}
