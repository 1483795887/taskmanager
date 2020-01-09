package com.cheng.taskmanager.entity;

import java.sql.Date;

public class Achievement {
    private Event event;
    private Date date;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
