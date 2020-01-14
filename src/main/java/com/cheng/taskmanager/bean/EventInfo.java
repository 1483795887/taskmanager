package com.cheng.taskmanager.bean;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;

public class EventInfo {
    private Event event;
    private Progress lastProgress;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Progress getLastProgress() {
        return lastProgress;
    }

    public void setLastProgress(Progress lastProgress) {
        this.lastProgress = lastProgress;
    }
}
