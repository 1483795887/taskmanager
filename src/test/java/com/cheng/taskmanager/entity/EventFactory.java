package com.cheng.taskmanager.entity;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class EventFactory {
    public static Event getCurrentEvent(int type) {
        Event event = getBasicEvent();
        event.setType(type);
        event.setFinished(false);
        event.setClosed(false);
        return event;
    }

    public static Event getClosedEvent(int type) {
        Event event = getBasicEvent();
        event.setType(type);
        event.setFinished(false);
        event.setClosed(true);
        return event;
    }

    public static Event getFinishedEvent(int type) {
        Event event = getBasicEvent();
        event.setType(type);
        event.setFinished(true);
        event.setClosed(false);
        return event;
    }

    private static Event getBasicEvent() {
        Event eventToAdd = new Event();
        eventToAdd.setTargetProgress(100);
        eventToAdd.setName("test");

        return eventToAdd;
    }
}
