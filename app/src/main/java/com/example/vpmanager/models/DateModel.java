package com.example.vpmanager.models;

public class DateModel {

    private String dateId, date, studyId, userId;
    boolean participated;

    public DateModel(String dateId, String date, String studyId, String userId, boolean participated) {
        this.dateId = dateId;
        this.date = date;
        this.studyId = studyId;
        this.userId = userId;
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

    public boolean getParticipation() {return participated;}

    public void setParticipation(boolean participated) { this.participated = participated;}
}
