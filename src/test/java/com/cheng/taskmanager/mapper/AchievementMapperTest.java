package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Achievement;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.EventFactory;
import com.cheng.taskmanager.utils.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AchievementMapperTest {

    private final static int NOT_EIST_EVENT_ID = 10000;
    @Autowired
    AchievementMapper mapper;
    @Autowired
    EventMapper eventMapper;

    private Date someday;
    private Date today;

    private Event event;

    @Before
    public void setUp() {
        someday = DateFactory.getDateFromString("2020-01-08");
        today = DateFactory.getToday();

        event = EventFactory.getFinishedEvent(Event.BOOK);
        event.setStartDate(someday);
        eventMapper.addEvent(event);
    }

    @Test
    @Transactional
    public void shouldNotNullWhenGetAchievements() {
        assertNotNull(mapper.getAchievements(someday, someday, 0, 1));
    }

    @Test
    @Transactional
    public void shouldCountIncWhenAddAchievement() {
        int count = mapper.getAchievementCount(someday, someday);
        Achievement achievement = new Achievement();
        achievement.setEvent(event);
        achievement.setDate(someday);
        mapper.addAchievement(achievement);
        assertEquals(count + 1, mapper.getAchievementCount(someday, someday));
    }

    @Test
    @Transactional
    public void shouldGetSameCountWhenGetAchievements(){
        for(int i = 0 ; i < 10 ; i ++){
            Event event = EventFactory.getFinishedEvent(Event.BOOK);
            eventMapper.addEvent(event);
            Achievement achievement = new Achievement();
            achievement.setEvent(event);
            achievement.setDate(today);
            mapper.addAchievement(achievement);
        }
        int count = mapper.getAchievementCount(today,today);
        List<Achievement> achievements = mapper.getAchievements(today,today, 0 , count);
        assertEquals(count, achievements.size());
    }
}