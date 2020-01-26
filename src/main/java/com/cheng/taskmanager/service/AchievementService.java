package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.EventInfo;

import java.util.List;

public interface AchievementService {
    List<EventInfo> getAchievements(DateAndTypeBean bean);
}
