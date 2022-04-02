package com.example.vpmanager.models;

public class StudyDetailModel {

    private String id, name, description, vps;
    private String contactOne, contactTwo, contactThree, contactFour, contactFive;
    private String category, executionType;

    private String remotePlatformOne, remotePlatformTwo;
    private String location, street, room;
    private boolean studyState;


    public StudyDetailModel(String id, String name, String description, String vps,
                            String contactOne, String contactTwo, String contactThree, String contactFour, String contactFive,
                            String category, String executionType,
                            String remotePlatformOne, String remotePlatformTwo,
                            String location, String street, String room, boolean studyState) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.vps = vps;

        this.contactOne = contactOne;
        this.contactTwo = contactTwo;
        this.contactThree = contactThree;
        this.contactFour = contactFour;
        this.contactFive = contactFive;

        this.category = category;
        this.executionType = executionType;

        this.remotePlatformOne = remotePlatformOne;
        this.remotePlatformTwo = remotePlatformTwo;

        this.location = location;
        this.street = street;
        this.room = room;
        this.studyState = studyState;
    }

    public StudyDetailModel(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVps() {
        return vps;
    }


    public String getContactOne() {
        return contactOne;
    }

    public String getContactTwo() {
        return contactTwo;
    }

    public String getContactThree() {
        return contactThree;
    }

    public String getContactFour() {
        return contactFour;
    }

    public String getContactFive() {
        return contactFive;
    }


    public String getCategory() {
        return category;
    }

    public String getExecutionType() {
        return executionType;
    }

    public String getRemotePlatformOne() {
        return remotePlatformOne;
    }

    public String getRemotePlatformTwo() {
        return remotePlatformTwo;
    }

    public String getLocation() {
        return location;
    }

    public String getStreet() {
        return street;
    }

    public String getRoom() {
        return room;
    }

    public boolean getStudyState() {
        return studyState;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVps(String vps) {
        this.vps = vps;
    }

    public void setContactOne(String contactOne) {
        this.contactOne = contactOne;
    }

    public void setContactTwo(String contactTwo) {
        this.contactTwo = contactTwo;
    }

    public void setContactThree(String contactThree) {
        this.contactThree = contactThree;
    }

    public void setContactFour(String contactFour) {
        this.contactFour = contactFour;
    }

    public void setContactFive(String contactFive) {
        this.contactFive = contactFive;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }


    public void setRemotePlatformOne(String remotePlatformOne) {
        this.remotePlatformOne = remotePlatformOne;
    }

    public void setRemotePlatformTwo(String remotePlatformTwo) {
        this.remotePlatformTwo = remotePlatformTwo;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setStudyState(boolean studyState) {
        this.studyState = studyState;
    }

}
