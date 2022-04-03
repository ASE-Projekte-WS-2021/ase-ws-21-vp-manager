package com.example.vpmanager.models;


import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean isDateInPast(DateModel date) {

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String currentDate = day + "." + (month + 1) + "." + year;
        //
        Date currentTime = Calendar.getInstance().getTime();

        Pattern pattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
        Matcher matcher = pattern.matcher(currentTime.toString());
        if (matcher.find()) {
            currentDate += " " + matcher.group(0);
            currentDate = currentDate.substring(0, currentDate.lastIndexOf(":"));
        }

        String testDate = date.getDate().substring(date.getDate().indexOf(",") + 2);
        testDate = testDate.replaceAll("um", "");
        testDate = testDate.replaceAll("Uhr", "");

        Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        try {

            Date c_Date = (Date) format.parseObject(currentDate);
            Date t_Date = (Date) format.parseObject(testDate);

            if (t_Date.before(c_Date)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

}
