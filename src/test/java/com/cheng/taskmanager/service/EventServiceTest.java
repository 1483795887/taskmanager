package com.cheng.taskmanager.service;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.EventFactory;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.impl.EventServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EventServiceTest {
    private EventMapper eventMapper;
    private EventService service;
    private Event event;

    @Before
    public void setUp() {
        eventMapper = mock(EventMapper.class);
        service = new EventServiceImpl(eventMapper);
        event = EventFactory.getCurrentEvent(Event.BOOK);
    }

    @Test
    public void shouldCallAddEventWhenAddEvent(){
        service.addEvent(event);
        verify(eventMapper).addEvent(event);
    }
}
