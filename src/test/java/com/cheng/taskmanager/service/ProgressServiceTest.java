package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.impl.ProgressServiceImpl;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.EventFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProgressServiceTest {

    private final static int TEST_EVENT_ID = 1;

    private EventMapper eventMapper;
    private ProgressService service;
    private Date someday;
    private Date today;
    private ArgumentCaptor<Date> startDateCap, endDateCap;

    private List<Progress> progressList;

    private Event event;

    private void addProgress(Event event, int p, int r, Date date) {
        Progress progress = new Progress();
        progress.setEid(event.getId());
        progress.setRecord(r);
        progress.setDate(date);
        progress.setProgress(p);
        progress.setEid(event.getId());
        List<Progress> progresses = new ArrayList<>();
        progresses.add(progress);
        progresses.addAll(event.getProgressList());
        event.setProgressList(progresses);

        progressList.add(progress);
    }

    private Event getEvent(int id, int type) {
        Event event = EventFactory.getCurrentEvent(type);
        event.setProgressList(new ArrayList<>());
        event.setId(id);
        addProgress(event, 10, 10, someday);
        addProgress(event, 20, 10, today);
        return event;
    }

    @Before
    public void setUp() {
        eventMapper = mock(EventMapper.class);
        service = new ProgressServiceImpl(eventMapper);

        someday = DateFactory.getDateFromString("2020-01-10");
        today = DateFactory.getToday();

        startDateCap = ArgumentCaptor.forClass(Date.class);
        endDateCap = ArgumentCaptor.forClass(Date.class);

        progressList = new ArrayList<>();
        event = getEvent(TEST_EVENT_ID, Event.BOOK);
    }

    @Test
    public void shouldCallRightWhenGetProgresses() {
        List<EventInfo> eventInfos = service.getProgresses(
                someday, today, Event.ALL);
        verify(eventMapper).getProgresses(any(Date.class), any(Date.class));
        assertNotNull(eventInfos);
    }

    @Test
    public void shouldExchangeDatesWhenDateIsOpposite() {
        service.getProgresses(today, someday, Event.ALL);
        verify(eventMapper).getProgresses(
                startDateCap.capture(), endDateCap.capture());
        Date startDate = startDateCap.getValue();
        Date endDate = endDateCap.getValue();
        assertEquals(startDate.toString(), someday.toString());
        assertEquals(endDate.toString(), today.toString());
    }

    @Test
    public void shouldResultRightWhenGetProgresses() {
        when(eventMapper.getProgresses(
                any(Date.class), any(Date.class))).
                thenReturn(event.getProgressList());
        when(eventMapper.getEventById(anyInt())).thenReturn(event);
        List<EventInfo> infos = service.getProgresses(someday, today, Event.ALL);
        EventInfo info = infos.get(0);
        EventBean bean = info.getEvent();
        Progress expectProgress = event.getProgressList().get(0);
        assertEquals(bean.getName(), event.getName());
        assertEquals(info.getProgress().getProgress(), expectProgress.getProgress());
        assertEquals(info.getProgress().getRecord(), expectProgress.getRecord());
    }

    private void addTestDataForType() {
        Event event = getEvent(TEST_EVENT_ID, Event.BOOK);
        when(eventMapper.getEventById(TEST_EVENT_ID)).thenReturn(event);
        Event event1 = getEvent(TEST_EVENT_ID + 1, Event.BOOK);
        when(eventMapper.getEventById(TEST_EVENT_ID + 1)).thenReturn(event1);
        Event event2 = getEvent(TEST_EVENT_ID + 2, Event.ANIM);
        when(eventMapper.getEventById(TEST_EVENT_ID + 2)).thenReturn(event2);

        when(eventMapper.getProgresses(
                any(Date.class), any(Date.class))).thenReturn(progressList);
    }

    @Test
    public void shouldGetAllWhenTypeIsAll() {
        addTestDataForType();
        List<EventInfo> eventInfos = service.getProgresses(
                someday, today, Event.ALL);
        assertEquals(eventInfos.size(), 8);
        for (EventInfo eventInfo : eventInfos) {
            EventBean event = eventInfo.getEvent();
            assertNotEquals(event.getType(), Event.CARD);
        }
    }

    @Test
    public void shouldGetExactTypeWhenAssignedType() {
        addTestDataForType();
        List<EventInfo> eventInfos = service.getProgresses(
                someday, today, Event.ANIM);
        assertEquals(eventInfos.size(), 2);
        for (EventInfo eventInfo : eventInfos) {
            assertEquals(eventInfo.getEvent().getType(), Event.ANIM);
        }
    }

    @Test
    public void shouldSumBeRightWhenGetSumRecord() {
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

        int sum = service.getSumRecord(eventInfos);
        assertEquals(30, sum);
    }
}
