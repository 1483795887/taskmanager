package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventInfo;

import java.sql.Date;
import java.util.List;

public interface AchievementService {
    List<EventInfo> getAchievements(Date startDate, Date endDate, int type);
}
