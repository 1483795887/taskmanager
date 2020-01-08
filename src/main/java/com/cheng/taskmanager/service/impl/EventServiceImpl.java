package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.entity.ReadRecord;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.mapper.ReadRecordMapper;
import com.cheng.taskmanager.service.EventService;
import com.cheng.taskmanager.utils.DateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private EventMapper eventMapper;
    private ReadRecordMapper readRecordMapper;

    @Autowired
    public EventServiceImpl(EventMapper eventMapper, ReadRecordMapper readRecordMapper) {
        this.eventMapper = eventMapper;
        this.readRecordMapper = readRecordMapper;
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

    private boolean isEventRunning(Event event) {
        if (event == null)
            return false;
        if (!event.getRunning())
            return false;
        return true;
    }

    private boolean isFinished(Event event, int p) {
        return p >= event.getTargetProgress();
    }

    private void finish(Event event) {
        eventMapper.finish(event.getId());
    }

    private void addProgress(Event event, int p) {
        int eid = event.getId();
        Progress progress = new Progress();
        progress.setDate(DateFactory.getToday());
        progress.setProgress(p);
        progress.setEid(eid);
        eventMapper.addProgress(progress);
    }

    private void addReadRecord(Event event, int cp, int p) {
        if (event.getType() == Event.BOOK) {
            ReadRecord readRecord = new ReadRecord();
            readRecord.setDate(DateFactory.getToday());
            readRecord.setRecord(p - cp);
            readRecordMapper.addReadRecord(readRecord);
        }
    }

    @Override
    public void updateProgress(int eid, int p) {
        Event event = eventMapper.getEventById(eid);
        if (!isEventRunning(event))
            return;
        if (isFinished(event, p)) {
            finish(event);
            p = event.getTargetProgress();
        }

        int currentProgress = event.getProgressList().get(0).getProgress();
        if (currentProgress == p) //没有变动就不要添加了
            return;
        addProgress(event, p);
        addReadRecord(event, currentProgress, p);
    }

    @Override
    public Event getEventById(int eid) {
        return eventMapper.getEventById(eid);
    }

    @Override
    public List<Event> getCurrentEvents() {
        return eventMapper.getCurrentEvents();
    }
}
