package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private EventMapper eventMapper;

    @Autowired
    public EventServiceImpl(EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    @Override
    public void addEvent(Event event) {
        eventMapper.addEvent(event);
    }
}
