package com.cheng.taskmanager.service.impl;

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
    public List<EventInfo> getProgresses(Date startDate, Date endDate, int type) {
        if (startDate.after(endDate)) {   ///如果时间顺序相反则交换顺序
            Date temp = endDate;
            endDate = startDate;
            startDate = temp;
        }
        List<Progress> progressList = mapper.getProgresses(startDate, endDate);
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

    @Override
    public int getSumRecord(List<EventInfo> eventInfos) {
        int sum = 0;
        for (EventInfo eventInfo : eventInfos) {
            sum += eventInfo.getProgress().getRecord();
        }
        return sum;
    }
}
