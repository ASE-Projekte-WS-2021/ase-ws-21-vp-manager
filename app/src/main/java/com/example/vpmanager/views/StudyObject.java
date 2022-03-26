package com.example.vpmanager.views;

public class StudyObject {
    String title;
    String vps;
    String studyId;
    String date;
    int color;

    public StudyObject(String _title, String _vps, String _studyId, String _date, int _color) {
        title = _title;
        vps = _vps;
        studyId = _studyId;
        date = _date;
        color = _color;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getVps() {
        return vps;
    }

    public String getStudyId() {
        return studyId;
    }

    public int getColor() {
        return color;
    }
}