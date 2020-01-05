package com.cheng.taskmanager.entity;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class EventFactory {
    public static Event getCurrentEvent(int type){
        Event event = getBasicEvent();
        event.setType(type);
        event.setIsFinished(false);
        event.setIsClosed(false);
        return event;
    }

    public static Event getClosedEvent(int type){
        Event event = getBasicEvent();
        event.setType(type);
        event.setIsFinished(false);
        event.setIsClosed(true);
        return event;
    }

    public static Event getFinishedEvent(int type){
        Event event = getBasicEvent();
        event.setType(type);
        event.setIsFinished(true);
        event.setIsClosed(false);
        return event;
    }

    private static Event getBasicEvent(){
        Event eventToAdd = new Event();
        eventToAdd.setCurrentProgress(0);
        eventToAdd.setTargetProgress(100);
        eventToAdd.setName("test");
        Date date = null;
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            date = new Date(simpleDateFormat.parse("2019-12-29").getTime());
        }catch (Exception e) {
            e.printStackTrace();
        }
        eventToAdd.setStartDate(date);
        eventToAdd.setLastModifiedDate(date);

        return eventToAdd;
    }
}
