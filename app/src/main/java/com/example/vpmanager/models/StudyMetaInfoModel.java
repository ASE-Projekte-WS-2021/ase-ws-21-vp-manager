package com.example.vpmanager.models;

public class StudyMetaInfoModel {

    private String id, name, vps, category, type;


    //Parameter: id, name, vps, category, type
    //Return values:
    //Sets variables
    public StudyMetaInfoModel(String id, String name, String vps, String category, String type) {
        this.id = id;
        this.name = name;
        this.vps = vps;
        this.category = category;
        this.type = type;
    }


    //Parameter:
    //Return values: String
    //Get study ID
    public String getId() {
        return id;
    }

    //Parameter: id
    //Return values:
    //Set study ID
    public void setId(String id) {
        this.id = id;
    }

    //Parameter:
    //Return values: String
    //Get study name
    public String getName() {
        return name;
    }

    //Parameter: name
    //Return values:
    //set study name
    public void setName(String name) {
        this.name = name;
    }

    //Parameter:
    //Return values: String
    //Get study vps
    public String getVps() {
        return vps;
    }

    //Parameter: vps
    //Return values:
    //Set study vp value
    public void setVps(String vps) {
        this.vps = vps;
    }

    //Parameter:
    //Return values: String
    //Get study category
    public String getCategory() {
        return category;
    }

    //Parameter: category
    //Return values:
    //set study category
    public void setCategory(String category) {
        this.category = category;
    }

    //Parameter:
    //Return values: String
    //Get study type
    public String getType() {
        return type;
    }

    //Parameter: type
    //Return values:
    //Set study Type
    public void setType(String type) {
        this.type = type;
    }
}
