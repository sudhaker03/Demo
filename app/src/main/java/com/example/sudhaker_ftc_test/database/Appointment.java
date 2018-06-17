package com.example.sudhaker_ftc_test.database;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sudhaker on 1/6/2018.
 */

public class Appointment implements Serializable {
    private int appointmentId;
    private String title;
    private String description;
    private String time;
    private String remindBefore;
    private String commaSepratedParticipants;

    private ArrayList<String> participants = new ArrayList<>();

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getCommaSepratedParticipants() {
        return commaSepratedParticipants;
    }

    public void setCommaSepratedParticipants(String commaSepratedParticipants) {
        this.commaSepratedParticipants = commaSepratedParticipants;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemindBefore() {
        return remindBefore;
    }

    public void setRemindBefore(String remindBefore) {
        this.remindBefore = remindBefore;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }
    ///Table

    public static final String TBL_APPOINTMENT = "tbl_Appointment";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TIME = "time";
    public static final String REMINDMEBEFORE = "remindBefore";
    public static final String PARTICIPANTS = "participants";

}
