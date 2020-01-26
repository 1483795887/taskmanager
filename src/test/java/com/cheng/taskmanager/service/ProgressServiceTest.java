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
    private ArgumentCaptor<Date> startDateCap, endDateCap;

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
    }

    private Event getEvent(int type){
        Event event = EventFactory.getCurrentEvent(type);
        event.setProgressList(new ArrayList<>());
        event.setId(TEST_EVENT_ID);
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

        event = getEvent(Event.BOOK);
    }

    @Test
    public void shouldCallRightWhenGetProgresses() {
        List<EventInfo> eventInfos = service.getProgresses(someday, today);
        verify(eventMapper).getProgresses(any(Date.class), any(Date.class));
        assertNotNull(eventInfos);
    }

    @Test
    public void shouldExchangeDatesWhenDateIsOpposite() {
        service.getProgresses(today, someday);
        verify(eventMapper).getProgresses(startDateCap.capture(), endDateCap.capture());
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
        List<EventInfo> infos = service.getProgresses(someday, today);
        EventInfo info = infos.get(0);
        EventBean bean = info.getEvent();
        Progress expectProgress = event.getProgressList().get(0);
        assertEquals(bean.getName(), event.getName());
        assertEquals(info.getProgress().getProgress(), expectProgress.getProgress());
        assertEquals(info.getProgress().getRecord(), expectProgress.getRecord());
    }
}
