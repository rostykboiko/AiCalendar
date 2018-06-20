package com.nulp.rostykboiko.aicalendar.utils.firebase.dao;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Event {
    private String eventID;
    private String eventTitle;
    private ArrayList<String> participantsList = new ArrayList<>();
    private boolean eventSingleOption;
    private String duration;
    private String durationValue;
    private int participantsCount;
    private String creatorId;

    public Event() {
    }

    public Event(String eventID,
                  String eventTitle,
                  ArrayList<Option> eventOptionList,
                  ArrayList<String> participantsList,
                  boolean eventSingleOption,
                  String duration,
                  int participantsCount,
                  String creatorId) {
        this.eventID = eventID;
        this.eventTitle = eventTitle;
        this.participantsList = participantsList;
        this.eventSingleOption = eventSingleOption;
        this.duration = duration;
        this.participantsCount = participantsCount;
        this.creatorId = creatorId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public ArrayList<String> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<String> participantsList) {
        this.participantsList = participantsList;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public int getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(int participantsCount) {
        this.participantsCount = participantsCount;
    }

    public boolean iseventSingleOption() {
        return eventSingleOption;
    }

    public void seteventSingleOption(boolean eventSingleOption) {
        this.eventSingleOption = eventSingleOption;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(eventID);
    }

    @Override
    public boolean equals(Object obj) {
        return eventID.equals(((Event) obj).eventID);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationValue() {
        return durationValue;
    }

    public void setDurationValue(String durationValue) {
        this.durationValue = durationValue;
    }
}
