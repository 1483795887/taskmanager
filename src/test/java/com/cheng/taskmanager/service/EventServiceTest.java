package com.cheng.taskmanager.service;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.EventFactory;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.impl.EventServiceImpl;
import com.cheng.taskmanager.utils.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    private final static int TEST_EVENT_ID = 1;

    private EventMapper eventMapper;
    private EventService service;
    private Event event;
    private ArgumentCaptor<Event> eventArgumentCaptor;
    private ArgumentCaptor<Progress> progressArgumentCaptor;
    private Date today;
    private Date startDate;

    @Before
    public void setUp() {
        eventMapper = mock(EventMapper.class);
        service = new EventServiceImpl(eventMapper);
        event = EventFactory.getCurrentEvent(Event.BOOK);
        eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        progressArgumentCaptor = ArgumentCaptor.forClass(Progress.class);

        java.util.Date date = new java.util.Date();
        today = new Date(date.getTime());
        startDate = DateFactory.getDateFromString("2020-01-06");

        event.setStartDate(startDate);
        event.setId(TEST_EVENT_ID);
        Progress progress = new Progress();
        progress.setDate(startDate);
        progress.setProgress(0);
        progress.setEid(TEST_EVENT_ID);
        List<Progress> progresses = new ArrayList<>();
        progresses.add(progress);
        event.setProgressList(progresses);
    }

    @Test
    public void shouldCallAddEventWhenAddEvent() {
        service.addEvent(event);

        verify(eventMapper).addEvent(eventArgumentCaptor.capture());
        assertEquals(event.getName(), eventArgumentCaptor.getValue().getName());
        assertNull(eventArgumentCaptor.getValue().getStartDate());
        assertEquals(event.getType(), eventArgumentCaptor.getValue().getType());
    }

    @Test
    public void shouldMakeInitProgressWhenAddEvent() {
        service.addEvent(event);
        verify(eventMapper).addProgress(progressArgumentCaptor.capture());
        assertEquals(today.toString(), progressArgumentCaptor.getValue().getDate().toString());
        assertEquals(0, progressArgumentCaptor.getValue().getProgress());
    }

    @Test
    public void shouldCallUpdateWhenUpdateEvent() {
        String newName = "newName";
        int targetProgress = 20;
        event.setName(newName);
        event.setStartDate(today);
        event.setTargetProgress(targetProgress);
        service.updateEvent(event);
        verify(eventMapper).update(eventArgumentCaptor.capture());

        assertEquals(newName, eventArgumentCaptor.getValue().getName());
        assertEquals(today.toString(), eventArgumentCaptor.getValue().getStartDate().toString());
        assertEquals(targetProgress, eventArgumentCaptor.getValue().getTargetProgress());
    }

    @Test
    public void shouldArgBeRightWhenUpdateChangedButNotFinished() {
        Progress newProgress = new Progress();
        int np = 20;
        newProgress.setProgress(np);
        when(eventMapper.getEventById(TEST_EVENT_ID)).thenReturn(event);
        service.updateProgress(TEST_EVENT_ID, np);
        verify(eventMapper).addProgress(progressArgumentCaptor.capture());

        Progress progress1 = progressArgumentCaptor.getValue();
        assertEquals(np, progress1.getProgress());
    }
}
