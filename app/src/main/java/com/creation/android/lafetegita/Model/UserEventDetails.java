package com.creation.android.lafetegita.Model;

public class UserEventDetails {
    String event_name, event_date;

    public UserEventDetails(String event_name, String event_date) {
        this.event_name = event_name;
        this.event_date = event_date;
    }

    public UserEventDetails() {
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }
}
