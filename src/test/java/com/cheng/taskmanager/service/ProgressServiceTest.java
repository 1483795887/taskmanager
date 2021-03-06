package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.DateRegion;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProgressServiceTest {

    private final static int TEST_EVENT_ID = 1;

    private EventMapper eventMapper;
    private ProgressService service;
    private Date someday;
    private Date today;

    private List<Progress> progressList;

    private Event event;

    private DateAndTypeBean inputBean;
    private DateRegion region;

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

        progressList = new ArrayList<>();
        event = getEvent(TEST_EVENT_ID, Event.BOOK);

        inputBean = new DateAndTypeBean(someday, today, Event.ALL);
        region = new DateRegion(someday, today);
    }

    @Test
    public void shouldCallRightWhenGetProgresses() {
        inputBean.setType(Event.ALL);
        List<EventInfo> eventInfos = service.getProgresses(inputBean);
        verify(eventMapper).getProgresses(any(DateRegion.class));
        assertNotNull(eventInfos);
    }

    @Test
    public void shouldExchangeDatesWhenDateIsOpposite() {
        service.getProgresses(inputBean);
        verify(eventMapper).getProgresses(region);
    }

    @Test
    public void shouldResultRightWhenGetProgresses() {
        when(eventMapper.getProgresses(any(DateRegion.class))).
                thenReturn(event.getProgressList());
        when(eventMapper.getEventById(anyInt())).thenReturn(event);
        List<EventInfo> infos = service.getProgresses(inputBean);
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

        when(eventMapper.getProgresses(any(DateRegion.class))).thenReturn(progressList);
    }

    @Test
    public void shouldGetAllWhenTypeIsAll() {
        addTestDataForType();
        List<EventInfo> eventInfos = service.getProgresses(inputBean);
        assertEquals(eventInfos.size(), 8);
    }

    @Test
    public void shouldGetExactTypeWhenAssignedType() {
        addTestDataForType();
        inputBean.setType(Event.ANIM);
        List<EventInfo> eventInfos = service.getProgresses(inputBean);
        assertEquals(eventInfos.size(), 2);
        for (EventInfo eventInfo : eventInfos) {
            assertEquals(eventInfo.getEvent().getType(), Event.ANIM);
        }
    }
}
