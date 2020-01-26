package com.cheng.taskmanager.utils;

import com.cheng.taskmanager.bean.EventInfo;

import java.util.List;

public class EventInfoSummer {
    public static int getSumRecord(List<EventInfo> infoList) {
        int sum = 0;
        for (EventInfo eventInfo : infoList) {
            sum += eventInfo.getProgress().getRecord();
        }
        return sum;
    }
}
