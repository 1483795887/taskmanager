package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.EventFactory;
import com.cheng.taskmanager.entity.Progress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMapperTest {

    private final static int NOT_EXIST_EVENT_ID = 10000;
    private final static int EXIST_EVENT_ID = 1;

    @Autowired
    EventMapper mapper;

    private Event currentEvent;
    private Event closedEvent;
    private Event finishedEvent;

    @Before
    public void setUp() {
        currentEvent = EventFactory.getCurrentEvent(Event.BOOK);
        closedEvent = EventFactory.getClosedEvent(Event.BOOK);
        finishedEvent = EventFactory.getFinishedEvent(Event.BOOK);
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountIncWhenAddUnfinishedEvent() {
        int count = mapper.getCurrentEventCount();
        mapper.addEvent(currentEvent);
        assertEquals(count + 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountSameWhenAddFinishedEvent() {
        int count = mapper.getCurrentEventCount();
        mapper.addEvent(finishedEvent);
        assertEquals(count, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountDecOneWhenCloseEvent() {
        mapper.addEvent(currentEvent);
        int count = mapper.getCurrentEventCount();
        mapper.close(currentEvent.getId());
        assertEquals(count - 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldClosedEventCountIncOneWhenCloseEvent() {
        mapper.addEvent(currentEvent);
        int count = mapper.getClosedEventCount();
        mapper.close(currentEvent.getId());
        assertEquals(count + 1, mapper.getClosedEventCount());
    }

    @Test
    @Transactional
    public void shouldFinishedEventCountSameWhenCloseEvent() {
        mapper.addEvent(currentEvent);
        int count = mapper.getFinishedEventCount();
        mapper.close(currentEvent.getId());
        assertEquals(count, mapper.getFinishedEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountDecOneWhenFinishEvent() {
        mapper.addEvent(currentEvent);
        int count = mapper.getCurrentEventCount();
        mapper.finish(currentEvent.getId());
        assertEquals(count - 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldFinishedEventCountIncOneWhenFinishEvent() {
        mapper.addEvent(currentEvent);
        int count = mapper.getFinishedEventCount();
        mapper.finish(currentEvent.getId());
        assertEquals(count + 1, mapper.getFinishedEventCount());
    }

    @Test
    @Transactional
    public void shouldNameSameWhenGetEventById() {
        mapper.addEvent(currentEvent);
        Event event = mapper.getEventById(currentEvent.getId());
        assertEquals(event.getName(), currentEvent.getName());
    }

    @Test
    @Transactional
    public void shouldNewNameWhenChangeTName() {
        String newName = "newName";

        mapper.addEvent(currentEvent);
        currentEvent.setName(newName);
        mapper.update(currentEvent);

        assertEquals(newName, mapper.getEventById(currentEvent.getId()).getName());
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void shouldThrowNullPointerWhenUpdateNameWithoutId() {
        String newName = "newName";

        currentEvent.setName(newName);
        mapper.addEvent(currentEvent);

        Event event = EventFactory.getCurrentEvent(Event.BOOK);
        event.setId(null);
        event.setName(newName);
        mapper.update(event);

        assertNotEquals(newName, mapper.getEventById(0).getName());
    }

    private void addEventsByType() {
        for (int i = 0; i < 10; i++) {
            mapper.addEvent(currentEvent);
            mapper.addEvent(closedEvent);
            mapper.addEvent(finishedEvent);
        }

    }

    @Test
    @Transactional
    public void shouldGetRightCountWhenGetCurrentEvents() {
        int limit = 3;
        int begin = mapper.getCurrentEventCount();
        addEventsByType();
        assertEquals(limit, mapper.getCurrentEvents(begin, limit).size());
    }

    @Test
    @Transactional
    public void shouldGetRightCountWhenGetClosedEvents() {
        int limit = 10;
        int begin = mapper.getClosedEventCount();
        addEventsByType();
        assertEquals(limit, mapper.getClosedEvents(begin, limit).size());
    }

    @Test
    @Transactional
    public void shouldGetRightCountWhenGetFinishedEvents() {
        int limit = 11;
        int begin = mapper.getFinishedEventCount();
        addEventsByType();
        assertEquals(10, mapper.getFinishedEvents(begin, limit).size());
    }

    @Test
    @Transactional
    public void shouldNullWhenGetByNotExistId(){
        Event event = mapper.getEventById(NOT_EXIST_EVENT_ID);
        assertNull(event);
    }

    @Test
    @Transactional
    public void shouldListNotNullWhenGetByExistId(){
        Event event = mapper.getEventById(EXIST_EVENT_ID);
        assertNotNull(event.getProgressList());
    }

    private void addProgress(int eid, int p, String strDate){
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            date = new Date(simpleDateFormat.parse(strDate).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Progress progress = new Progress();
        progress.setDate(date);
        progress.setEid(eid);
        progress.setProgress(p);
        mapper.addProgress(progress);
    }

    @Test
    @Transactional
    public void shouldCountIncWhenAddProgress(){
        mapper.addEvent(currentEvent);
        Event event = mapper.getEventById(currentEvent.getId());
        int count = event.getProgressList().size();

        addProgress(event.getId(), 10, "2020-1-6");

        event = mapper.getEventById(currentEvent.getId());

        assertEquals(count + 1, event.getProgressList().size());
    }
}