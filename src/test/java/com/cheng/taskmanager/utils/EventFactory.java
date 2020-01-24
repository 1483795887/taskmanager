package com.cheng.taskmanager.utils;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class EventFactory {
    public static Event getCurrentEvent(int type) {
        Event event = getBasicEvent();
        event.setType(type);
        event.setRunning(true);
        return event;
    }

    public static Event getFinishedEvent(int type) {
        Event event = getBasicEvent();
        event.setType(type);
        event.setRunning(false);
        return event;
    }

    private static Event getBasicEvent() {
        Event eventToAdd = new Event();
        eventToAdd.setTargetProgress(100);
        eventToAdd.setName("test");

        return eventToAdd;
    }

    public static void addProgress(Event event, int p, int r, Date date) {
        Progress progress = new Progress();
        progress.setRecord(r);
        progress.setDate(date);
        progress.setProgress(p);
        progress.setEid(event.getId());
        List<Progress> progresses = new ArrayList<>();
        progresses.add(progress);
        progresses.addAll(event.getProgressList());
        event.setProgressList(progresses);
    }
}
