package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Event;

import java.util.List;

public interface EventService {
    void addEvent(Event event);

    void updateEvent(Event event);

    void updateProgress(int eid, int progress);

    Event getEventById(int eid);

    List<EventInfo> getCurrentEvents();
}
