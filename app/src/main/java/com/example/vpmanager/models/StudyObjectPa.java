package com.example.vpmanager.models;

public class StudyObjectPa {

    private String title, vps, studyId, date;
    private int color;

    public StudyObjectPa(String _title, String _vps, String _studyId, String _date, int _color) {
        this.title = _title;
        this.vps = _vps;
        this.studyId = _studyId;
        this.date = _date;
        this.color = _color;
    }

    public StudyObjectPa(){

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