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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMapperTest {

    @Autowired
    EventMapper mapper;

    private Event eventToAdd;
    private Date date;

    @Before
    public void setUp() throws Exception{
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
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountIncWhenAddUnfinishedEvent(){
        int count = mapper.getCurrentEventCount();
        mapper.addEvent(eventToAdd);
        assertEquals(count + 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountSameWhenAddFinishedEvent(){
        int count = mapper.getCurrentEventCount();
        eventToAdd.setIsFinished(true);
        mapper.addEvent(eventToAdd);
        assertEquals(count , mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldCurrentEventCountDecOneWhenCloseEvent(){
        int id = mapper.addEvent(eventToAdd);
        int count = mapper.getCurrentEventCount();
        mapper.close(id);
        assertEquals(count - 1, mapper.getCurrentEventCount());
    }

    @Test
    @Transactional
    public void shouldClosedEventCountIncOneWhenCloseEvent(){
        int id = mapper.addEvent(eventToAdd);
        int count = mapper.getClosedEventCount();
        mapper.close(id);
        assertEquals(count + 1, mapper.getClosedEventCount());
    }

    @Test
    @Transactional
    public void shouldFinishedEventCountSameWhenCloseEvent(){
        int id = mapper.addEvent(eventToAdd);
        int count = mapper.getFinishedEventCount();
        mapper.close(id);
        assertEquals(count , mapper.getFinishedEventCount());
    }
}