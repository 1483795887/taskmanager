package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.EventFactory;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.utils.DateFactory;
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

    @Autowired
    EventMapper mapper;

    private Event currentEvent;
    private Event finishedEvent;
    private Date oldday;
    private Date middleday1;
    private Date someday;
    private Date today;

    @Before
    public void setUp() {
        currentEvent = EventFactory.getCurrentEvent(Event.BOOK);
        finishedEvent = EventFactory.getFinishedEvent(Event.BOOK);

        oldday = DateFactory.getDateFromString("2020-01-05");
        middleday1 = DateFactory.getDateFromString("2020-01-06");
        someday = DateFactory.getDateFromString("2020-01-07");
        today = DateFactory.getToday();
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
    public void shouldNotRunningWhenFinishEvent() {
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
        currentEvent.setStartDate(someday);
        mapper.update(currentEvent);
        Event event = mapper.getEventById(currentEvent.getId());

        assertNotEquals(someday.toString(), event.getStartDate().toString());
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
        event.setStartDate(oldday);
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

    private void addProgress(int eid, int p, int r, Date date) {
        Progress progress = new Progress();
        progress.setDate(date);
        progress.setEid(eid);
        progress.setProgress(p);
        progress.setRecord(r);
        mapper.addProgress(progress);
    }

    @Test
    @Transactional
    public void shouldCountIncWhenAddProgress() {
        mapper.addEvent(currentEvent);
        Event event = mapper.getEventById(currentEvent.getId());
        int count = event.getProgressList().size();

        addProgress(event.getId(), 10, 10, someday);

        event = mapper.getEventById(currentEvent.getId());

        assertEquals(count + 1, event.getProgressList().size());
    }

    @Test
    @Transactional
    public void shouldBeSameWhenAddAndGetProgress() {
        mapper.addEvent(currentEvent);
        addProgress(currentEvent.getId(), 10, 10, someday);
        Event event = mapper.getEventById(currentEvent.getId());

        assertEquals(someday.toString(), event.getProgressList().get(0).getDate().toString());
    }

    @Test
    @Transactional
    public void shouldGetNearestInDateWhenGetTheFirstProgress() {
        mapper.addEvent(currentEvent);
        addProgress(currentEvent.getId(), 20, 20, someday);
        addProgress(currentEvent.getId(), 10, 10, today);
        Event event = mapper.getEventById(currentEvent.getId());
        assertEquals(today.toString(), event.getProgressList().get(0).getDate().toString());
    }

    @Test
    @Transactional
    public void shouldGetRightAddWhenAddSomeInOneDay() {
        mapper.addEvent(currentEvent);
        int beforeP = 10;
        int afterP = 20;
        int beforeR = 30;
        int afterR = 40;
        addProgress(currentEvent.getId(), beforeP, beforeR, someday);
        addProgress(currentEvent.getId(), afterP, afterR, someday);

        Event event = mapper.getEventById(currentEvent.getId());
        assertEquals(afterP, event.getProgressList().get(0).getProgress());
        assertEquals(afterR + beforeR, event.getProgressList().get(0).getRecord());
    }

    @Test
    @Transactional
    public void shouldNotBeNullWhenGetProgresses() {
        assertNotNull(mapper.getProgresses(today, today));
    }

    @Test
    @Transactional
    public void shouldSomeDayCountIncWhenAddProgress() {
        mapper.addEvent(currentEvent);
        int count = mapper.getProgresses(someday, someday).size();
        addProgress(currentEvent.getId(), 10, 10, someday);
        assertEquals(count + 1, mapper.getProgresses(someday, someday).size());
    }

    @Test
    @Transactional
    public void shouldNotCountIncWhenAddProgressNotInArea() {
        mapper.addEvent(currentEvent);
        int count = mapper.getProgresses(middleday1, someday).size();
        addProgress(currentEvent.getId(), 10, 10, today);
        assertEquals(count, mapper.getProgresses(middleday1, someday).size());
    }

    private void addTestProgresses() {
        currentEvent.setStartDate(someday);
        mapper.addEvent(currentEvent);
        int oldEid = currentEvent.getId();
        currentEvent.setStartDate(today);
        mapper.addEvent(currentEvent);
        int newEid = currentEvent.getId();
        addProgress(oldEid, 10, 10, oldday);
        addProgress(oldEid, 10, 10, someday);
        addProgress(newEid, 10, 10, today);
    }

    @Test
    @Transactional
    public void shouldCountRightWhenBottomLimit() {
        int count = mapper.getProgresses(someday, middleday1).size();
        addTestProgresses();
        assertEquals(count + 1, mapper.getProgresses(oldday, middleday1).size());
    }

    @Test
    @Transactional
    public void shouldCountRightWhenUpperLimit() {
        int count = mapper.getProgresses(middleday1, today).size();
        addTestProgresses();
        assertEquals(count + 2, mapper.getProgresses(middleday1, today).size());

    }
}