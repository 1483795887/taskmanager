package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.utils.DateFactory;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMapperTest {

    private final static int NOT_EXIST_EVENT_ID = 10000;
    private final static int EXIST_EVENT_ID = 1;
    private final static String PAST_TIME = "2018-01-06";
    private final static String NEW_DATE = "2019-01-06";
    private final static String NEWER_DATE = "2020-01-06";

    @Autowired
    EventMapper mapper;

    private Event currentEvent;
    private Event finishedEvent;

    @Before
    public void setUp() {
        currentEvent = EventFactory.getCurrentEvent(Event.BOOK);
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
    public void shouldCurrentEventCountDecOneWhenFinishEvent() {
        mapper.addEvent(currentEvent);
        int count = mapper.getCurrentEventCount();
        mapper.finish(currentEvent.getId());
        assertEquals(count - 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldDateDefaultNowWhenNoDate() {
        mapper.addEvent(currentEvent);
        Event event = mapper.getEventById(currentEvent.getId());
        java.util.Date date = new java.util.Date();
        Date sqlDate = new Date(date.getTime());

        assertEquals(sqlDate.toString(), event.getStartDate().toString());
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
    public void shouldNotRunningWhenFinishEvent(){
        mapper.addEvent(currentEvent);
        mapper.finish(currentEvent.getId());
        Event event = mapper.getEventById(currentEvent.getId());
        assertFalse(event.getRunning());
    }

    @Test
    @Transactional
    public void shouldNewNameWhenChangeName() {
        String newName = "newName";

        mapper.addEvent(currentEvent);
        currentEvent.setName(newName);
        mapper.update(currentEvent);

        assertEquals(newName, mapper.getEventById(currentEvent.getId()).getName());
    }

    @Test
    @Transactional
    public void shouldNotChangeDateWhenChangeDate() {
        mapper.addEvent(currentEvent);
        currentEvent.setStartDate(DateFactory.getDateFromString(NEW_DATE));
        mapper.update(currentEvent);
        Event event = mapper.getEventById(currentEvent.getId());

        assertNotEquals(NEW_DATE, event.getStartDate().toString());
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
        event.setStartDate(DateFactory.getDateFromString(PAST_TIME));
        mapper.update(event);

        assertNotEquals(newName, mapper.getEventById(0).getName());
    }

    private void addEventsByType() {
        for (int i = 0; i < 10; i++) {
            mapper.addEvent(currentEvent);
            mapper.addEvent(finishedEvent);
        }

    }

    @Test
    @Transactional
    public void shouldGetRightCountWhenGetCurrentEvents() {
        addEventsByType();
        assertEquals(mapper.getCurrentEventCount(), mapper.getCurrentEvents().size());
    }

    @Test
    @Transactional
    public void shouldNullWhenGetByNotExistId() {
        Event event = mapper.getEventById(NOT_EXIST_EVENT_ID);
        assertNull(event);
    }

    @Test
    @Transactional
    public void shouldListNotNullWhenGetByExistId() {
        Event event = mapper.getEventById(EXIST_EVENT_ID);
        assertNotNull(event.getProgressList());
    }

    private void addProgress(int eid, int p, String strDate) {
        Progress progress = new Progress();
        progress.setDate(DateFactory.getDateFromString(strDate));
        progress.setEid(eid);
        progress.setProgress(p);
        mapper.addProgress(progress);
    }

    @Test
    @Transactional
    public void shouldCountIncWhenAddProgress() {
        mapper.addEvent(currentEvent);
        Event event = mapper.getEventById(currentEvent.getId());
        int count = event.getProgressList().size();

        addProgress(event.getId(), 10, "2020-1-6");

        event = mapper.getEventById(currentEvent.getId());

        assertEquals(count + 1, event.getProgressList().size());
    }

    @Test
    @Transactional
    public void shouldBeSameWhenAddAndGetProgress() {
        mapper.addEvent(currentEvent);
        addProgress(currentEvent.getId(), 10, NEW_DATE);
        Event event = mapper.getEventById(currentEvent.getId());

        assertEquals(NEW_DATE, event.getProgressList().get(0).getDate().toString());
    }

    @Test
    @Transactional
    public void shouldGetNearestInDateWhenGetTheFirstProgress() {
        mapper.addEvent(currentEvent);
        addProgress(currentEvent.getId(), 20, NEW_DATE);
        addProgress(currentEvent.getId(), 10, NEWER_DATE);
        Event event = mapper.getEventById(currentEvent.getId());
        assertEquals(NEWER_DATE, event.getProgressList().get(0).getDate().toString());
    }

    @Test
    @Transactional
    public void shouldGetLastAddWhenAddSomeInOneDay() {
        mapper.addEvent(currentEvent);
        int beforeP = 10;
        int afterP = 20;
        addProgress(currentEvent.getId(), beforeP, NEW_DATE);
        addProgress(currentEvent.getId(), afterP, NEW_DATE);

        Event event = mapper.getEventById(currentEvent.getId());
        assertEquals(afterP, event.getProgressList().get(0).getProgress());
    }
}