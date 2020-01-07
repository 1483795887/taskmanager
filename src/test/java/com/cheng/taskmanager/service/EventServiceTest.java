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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EventServiceTest {
    private EventMapper eventMapper;
    private EventService service;
    private Event event;
    private ArgumentCaptor<Event> eventArgumentCaptor;
    private ArgumentCaptor<Progress> progressArgumentCaptor;
    private Date today;

    @Before
    public void setUp() {
        eventMapper = mock(EventMapper.class);
        service = new EventServiceImpl(eventMapper);
        event = EventFactory.getCurrentEvent(Event.BOOK);
        eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        progressArgumentCaptor = ArgumentCaptor.forClass(Progress.class);

        java.util.Date date = new java.util.Date();
        today = new Date(date.getTime());
    }

    @Test
    public void shouldCallAddEventWhenAddEvent() {
        event.setStartDate(today);
        service.addEvent(event);

        verify(eventMapper).addEvent(eventArgumentCaptor.capture());
        assertEquals(event.getName(), eventArgumentCaptor.getValue().getName());
        assertNull(eventArgumentCaptor.getValue().getStartDate());
        assertEquals(event.getType(), eventArgumentCaptor.getValue().getType());
    }

    @Test
    public void shouldMakeInitProgressWhenAddEvent(){
        service.addEvent(event);
        verify(eventMapper).addProgress(progressArgumentCaptor.capture());
        assertEquals(today.toString(), progressArgumentCaptor.getValue().getDate().toString());
        assertEquals(0, progressArgumentCaptor.getValue().getProgress());
    }

    @Test
    public void shouldCallUpdateWhenUpdateEvent(){
        service.addEvent(event);
        String newName = "newName";
        Date newDate = DateFactory.getDateFromString("2019-01-07");
        int targetProgress = 20;
        event.setName(newName);
        event.setStartDate(newDate);
        event.setTargetProgress(targetProgress);
        service.updateEvent(event);
        verify(eventMapper).update(eventArgumentCaptor.capture());

        assertEquals(newName, eventArgumentCaptor.getValue().getName());
        assertEquals(newDate.toString(), eventArgumentCaptor.getValue().getStartDate().toString());
        assertEquals(targetProgress,eventArgumentCaptor.getValue().getTargetProgress());
    }
}
