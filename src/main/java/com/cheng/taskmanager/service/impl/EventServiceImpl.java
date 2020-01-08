package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.EventService;
import com.cheng.taskmanager.utils.DateFactory;
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
        event.setStartDate(null);   //增加时是自动添加的日期不需要前台传入
        eventMapper.addEvent(event);
        Progress progress = new Progress();
        progress.setProgress(0);
        progress.setEid(event.getId());
        progress.setDate(DateFactory.getToday());
        eventMapper.addProgress(progress);
    }

    @Override
    public void updateEvent(Event event) {
        eventMapper.update(event);
    }

    private boolean isEventRunning(Event event){
        if(event == null)
            return false;
        if(event.getClosed())
            return false;
        if(event.getFinished())
            return false;
        return true;
    }

    @Override
    public void updateProgress(int eid, int p) {
        Event event = eventMapper.getEventById(eid);
        if(!isEventRunning(event))
            return;
        Progress progress = new Progress();
        progress.setDate(DateFactory.getToday());
        progress.setProgress(p);
        progress.setEid(eid);

        eventMapper.addProgress(progress);
    }
}
