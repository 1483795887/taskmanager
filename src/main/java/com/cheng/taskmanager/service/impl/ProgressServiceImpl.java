package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressServiceImpl implements ProgressService {

    private EventMapper mapper;

    @Autowired
    public ProgressServiceImpl(EventMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<EventInfo> getProgresses(Date startDate, Date endDate) {
        List<Progress> progressList = mapper.getProgresses(startDate, endDate);
        List<EventInfo> infos = new ArrayList<>();
        return infos;
    }
}
