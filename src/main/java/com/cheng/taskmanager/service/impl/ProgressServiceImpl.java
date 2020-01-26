package com.cheng.taskmanager.service.impl;

import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.DateRegion;
import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Event;
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
    public List<EventInfo> getProgresses(DateAndTypeBean inputBean) {
        Date startDate = inputBean.getStartDate();
        Date endDate = inputBean.getEndDate();
        int type = inputBean.getType();
        DateRegion region = new DateRegion(startDate, endDate);
        region.checkOrder();
        List<Progress> progressList = mapper.getProgresses(region);
        List<EventInfo> infos = new ArrayList<>();

        for (Progress progress : progressList) {
            EventInfo info = new EventInfo();
            Event event = mapper.getEventById(progress.getEid());
            if (type != Event.ALL && event.getType() != type)
                continue;
            info.setEvent(EventBean.getFromEvent(event));
            info.setProgress(progress);
            infos.add(info);
        }

        return infos;
    }

}
