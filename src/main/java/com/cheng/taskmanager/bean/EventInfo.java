package com.cheng.taskmanager.bean;

import com.cheng.taskmanager.entity.Progress;

public class EventInfo {
    private EventBean event;
    private Progress progress;

    public EventBean getEvent() {
        return event;
    }

    public void setEvent(EventBean event) {
        this.event = event;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
