package com.example.vpmanager.models;

public class StudyMetaInfoModel {

    private String id, name, vps, category;

    public StudyMetaInfoModel(String id, String name, String vps, String category) {
        this.id = id;
        this.name = name;
        this.vps = vps;
        this.category = category;
    }

    public StudyMetaInfoModel() {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
