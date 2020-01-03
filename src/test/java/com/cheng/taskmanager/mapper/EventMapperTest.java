package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMapperTest {

    @Autowired
    EventMapper mapper;

    private Event eventToAdd;
    private Date date;

    @Before
    public void setUp() throws Exception {
        eventToAdd = new Event();
        eventToAdd.setCurrentProgress(0);
        eventToAdd.setTargetProgress(100);
        eventToAdd.setName("test");
        eventToAdd.setIsClosed(false);
        eventToAdd.setIsFinished(false);
        eventToAdd.setType(Event.BOOK);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        date = new Date(simpleDateFormat.parse("2019-12-29").getTime());
        eventToAdd.setStartDate(date);
        eventToAdd.setLastModifiedDate(date);
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountIncWhenAddUnfinishedEvent() {
        int count = mapper.getCurrentEventCount();
        mapper.addEvent(eventToAdd);
        assertEquals(count + 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountSameWhenAddFinishedEvent() {
        int count = mapper.getCurrentEventCount();
        eventToAdd.setIsFinished(true);
        mapper.addEvent(eventToAdd);
        assertEquals(count, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountDecOneWhenCloseEvent() {
        mapper.addEvent(eventToAdd);
        int count = mapper.getCurrentEventCount();
        mapper.close(eventToAdd.getId());
        assertEquals(count - 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldClosedEventCountIncOneWhenCloseEvent() {
        mapper.addEvent(eventToAdd);
        int count = mapper.getClosedEventCount();
        mapper.close(eventToAdd.getId());
        assertEquals(count + 1, mapper.getClosedEventCount());
    }

    @Test
    @Transactional
    public void shouldFinishedEventCountSameWhenCloseEvent() {
        mapper.addEvent(eventToAdd);
        int count = mapper.getFinishedEventCount();
        mapper.close(eventToAdd.getId());
        assertEquals(count, mapper.getFinishedEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountDecOneWhenFinishEvent() {
        mapper.addEvent(eventToAdd);
        int count = mapper.getCurrentEventCount();
        mapper.finish(eventToAdd.getId());
        assertEquals(count - 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldFinishedEventCountIncOneWhenFinishEvent() {
        mapper.addEvent(eventToAdd);
        int count = mapper.getFinishedEventCount();
        mapper.finish(eventToAdd.getId());
        assertEquals(count + 1, mapper.getFinishedEventCount());
    }

    @Test
    @Transactional
    public void shouldNameSameWhenGetEventById() {
        mapper.addEvent(eventToAdd);
        Event event = mapper.getEventById(eventToAdd.getId());
        assertEquals(event.getName(), eventToAdd.getName());
    }

    @Test
    @Transactional
    public void shouldNewNameWhenChangeTName() {
        String newName = "newName";

        mapper.addEvent(eventToAdd);
        eventToAdd.setName(newName);
        mapper.update(eventToAdd);

        assertEquals(newName, mapper.getEventById(eventToAdd.getId()).getName());
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void shouldThrowNullPointerWhenUpdateNameWithoutId() {
        String newName = "newName";

        mapper.addEvent(eventToAdd);
        eventToAdd.setName(newName);

        Event event = new Event();
        event.setName(eventToAdd.getName());
        event.setCurrentProgress(eventToAdd.getCurrentProgress());
        event.setTargetProgress(eventToAdd.getTargetProgress());
        event.setIsFinished(eventToAdd.getIsFinished());
        event.setIsClosed(eventToAdd.getIsClosed());
        event.setStartDate(eventToAdd.getStartDate());
        event.setLastModifiedDate(eventToAdd.getLastModifiedDate());
        event.setType(eventToAdd.getType());
        mapper.update(event);

        assertNotEquals(newName, mapper.getEventById(0).getName());
    }

    private void addEventsByType() {
        for (int i = 0; i < 10; i++) {
            eventToAdd.setIsFinished(false);
            eventToAdd.setIsClosed(false);
            mapper.addEvent(eventToAdd);

            eventToAdd.setIsFinished(true);
            mapper.addEvent(eventToAdd);

            eventToAdd.setIsFinished(false);
            eventToAdd.setIsClosed(true);
            mapper.addEvent(eventToAdd);
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
        int begin = mapper.getCurrentEventCount();
        addEventsByType();
        assertEquals(limit, mapper.getCurrentEvents(begin, limit).size());
    }

    @Test
    @Transactional
    public void shouldGetRightCountWhenGetFinishedEvents() {
        int limit = 11;
        int begin = mapper.getCurrentEventCount();
        addEventsByType();
        assertEquals(10, mapper.getCurrentEvents(begin, limit).size());
    }
}