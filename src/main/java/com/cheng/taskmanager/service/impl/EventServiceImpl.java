package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Achievement;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.mapper.AchievementMapper;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.EventService;
import com.cheng.taskmanager.utils.DateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private EventMapper eventMapper;
    private AchievementMapper achievementMapper;

    @Autowired
    public EventServiceImpl(EventMapper eventMapper, AchievementMapper achievementMapper) {
        this.eventMapper = eventMapper;
        this.achievementMapper = achievementMapper;
    }

    @Override
    public void addEvent(Event event) {
        event.setStartDate(null);   //增加时是自动添加的日期不需要前台传入
        eventMapper.addEvent(event);
        Progress progress = new Progress();
        progress.setProgress(0);
        progress.setEid(event.getId());
        progress.setDate(DateFactory.getToday());
        progress.setRecord(0);
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
        Achievement achievement = new Achievement();
        achievement.setEid(event.getId());
        achievement.setDate(DateFactory.getToday());
        achievementMapper.addAchievement(achievement);
    }

    private void addProgress(Event event, int p, int r) {
        int eid = event.getId();
        Progress progress = new Progress();
        progress.setDate(DateFactory.getToday());
        progress.setProgress(p);
        progress.setRecord(r);
        progress.setEid(eid);
        eventMapper.addProgress(progress);
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
        int r = p - currentProgress;
        addProgress(event, p, r);
    }

    @Override
    public Event getEventById(int eid) {
        return eventMapper.getEventById(eid);
    }

    @Override
    public List<EventInfo> getCurrentEvents() {
        List<Event> events = eventMapper.getCurrentEvents();
        List<EventInfo> eventInfos = new ArrayList<>();
        for (Event event : events) {
            EventInfo info = new EventInfo();
            Progress progress = event.getProgressList().get(0);
            event.setProgressList(null);
            info.setEvent(event);
            info.setLastProgress(progress);
            eventInfos.add(info);
        }
        return eventInfos;
    }
}
