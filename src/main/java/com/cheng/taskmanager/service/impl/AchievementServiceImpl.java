package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Achievement;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.mapper.AchievementMapper;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    private AchievementMapper achievementMapper;
    private EventMapper eventMapper;

    @Autowired
    public AchievementServiceImpl(AchievementMapper achievementMapper, EventMapper eventMapper) {
        this.achievementMapper = achievementMapper;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventInfo> getAchievements(Date startDate, Date endDate, int type) {
        if (startDate.after(endDate)) {
            Date temp = endDate;
            endDate = startDate;
            startDate = temp;
        }
        List<Achievement> achievementList = achievementMapper.getAchievements(startDate, endDate);
        List<EventInfo> eventInfoList = new ArrayList<>();
        for (Achievement achievement : achievementList) {
            Event event = eventMapper.getEventById(achievement.getEid());
            EventInfo info = new EventInfo();
            EventBean bean = EventBean.getFromEvent(event);
            Progress progress = new Progress();
            progress.setRecord(bean.getTargetProgress());
            progress.setProgress(bean.getTargetProgress());

            info.setEvent(bean);
            info.setProgress(progress);
            eventInfoList.add(info);
        }
        return eventInfoList;
    }
}
