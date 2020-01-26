package com.cheng.taskmanager.utils;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Progress;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventInfoSummerTest {

    @Test
    void shouldSumBeRightWhenGetSumRecord() {
        List<EventInfo> eventInfos = new ArrayList<>();
        EventInfo info = new EventInfo();
        Progress progress = new Progress();
        progress.setRecord(10);
        info.setProgress(progress);
        eventInfos.add(info);

        EventInfo info1 = new EventInfo();
        Progress progress1 = new Progress();
        progress1.setRecord(20);
        info1.setProgress(progress1);
        eventInfos.add(info1);

        int sum = EventInfoSummer.getSumRecord(eventInfos);
        assertEquals(30, sum);
    }
}