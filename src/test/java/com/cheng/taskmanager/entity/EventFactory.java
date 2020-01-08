package com.cheng.taskmanager.entity;

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
}
