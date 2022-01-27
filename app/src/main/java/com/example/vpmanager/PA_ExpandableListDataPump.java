package com.example.vpmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PA_ExpandableListDataPump {


    //Parameters
    //Return Values: Returns a HashMap with test data for the personal account view
    //this function generates an ArrayList for each category which is displayed in the overview in the personal Account
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> plannedStudies = new ArrayList<String>();
        plannedStudies.add("Spielverhalten während Corona und soziale Interaktion, 0.5 VP, 28.01.2022 15 Uhr");
        plannedStudies.add("Zoom Interview zu Wünschen für eine App für Hundebesitzer, 1 VP, 28.01.2022 12 Uhr");

        List<String> participatedStudies = new ArrayList<String>();
        participatedStudies.add("Fragebogen zur Infrastruktur der Medieninformatik, 1.5 VP, 22.01.2022 13 Uhr");
        participatedStudies.add(" Umfrage bzgl. eines Softwareprototyps - Sketchable Interaction (SI), 2 VP, 24.01.2022 17 Uhr");

        List<String> completedStudies = new ArrayList<String>();
        completedStudies.add("Testen einer Digital Wellbeing App, 1 VP, 06.01.2022 17 Uhr");
        completedStudies.add("Umfrage bezüglich des Vertrauens der verschiedenen Generationen in die Sprachassistenten, 3 VP, 01.01.2022 12 Uhr");
        completedStudies.add("Studie mit Computerspiel, 1 VP, 05.01.2022 13 Uhr");

        List<String> ownStudies = new ArrayList<String>();
        ownStudies.add("Testen des VP-Managers, 1 VP, 28.01.2022 17 Uhr");
        ownStudies.add("Testen des VP-Managers, 1 VP, 29.01.2022 13 Uhr");
        ownStudies.add("Testen des VP-Managers, 1 VP, 30.01.2022 12 Uhr");
        ownStudies.add("Evaluation der Self-Fulfilling Prophecy, 1,5 VP, 28.01.2022 15:30 Uhr");
        ownStudies.add("Testen des VP-Managers, 1 VP, 31.01.2022 11 Uhr");

        expandableListDetail.put("Geplante Studien", plannedStudies);
        expandableListDetail.put("Teilgenommene Studien", participatedStudies);
        expandableListDetail.put("Abgeschlossene Studien", completedStudies);
        expandableListDetail.put("Eigene Studien", ownStudies);
        return expandableListDetail;
    }
}
