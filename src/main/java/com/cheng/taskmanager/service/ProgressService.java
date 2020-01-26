package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.EventInfo;

import java.util.List;

public interface ProgressService {
    List<EventInfo> getProgresses(DateAndTypeBean inputBean);
}
