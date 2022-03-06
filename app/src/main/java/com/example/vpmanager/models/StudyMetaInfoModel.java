package com.example.vpmanager.models;

public class StudyMetaInfoModel {

    private String id, name, vps;

    public StudyMetaInfoModel(String id, String name, String vps) {
        this.id = id;
        this.name = name;
        this.vps = vps;
    }

    public StudyMetaInfoModel(){
        //empty constructor required?
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVps() {
        return vps;
    }

    public void setVps(String vps) {
        this.vps = vps;
    }
}
