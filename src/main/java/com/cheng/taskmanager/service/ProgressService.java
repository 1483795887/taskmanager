package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventInfo;

import java.sql.Date;
import java.util.List;

public interface ProgressService {
    List<EventInfo> getProgresses(Date startDate, Date endDate, int type);

    int getSumRecord(List<EventInfo> eventInfos);
}
