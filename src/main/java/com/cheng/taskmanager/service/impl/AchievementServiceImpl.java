package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Achievement;
import com.cheng.taskmanager.mapper.AchievementMapper;
import com.cheng.taskmanager.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    private AchievementMapper mapper;

    @Autowired
    public AchievementServiceImpl(AchievementMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<EventInfo> getAchievements(Date startDate, Date endDate, int type) {
        List<Achievement> achievementList = mapper.getAchievements(startDate, endDate);
        List<EventInfo> eventInfoList = new ArrayList<>();
        return eventInfoList;
    }
}
