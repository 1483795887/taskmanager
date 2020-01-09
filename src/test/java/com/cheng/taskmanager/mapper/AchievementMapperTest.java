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

    private Date oldday;
    private Date middleday1;
    private Date someday;
    private Date middleday2;
    private Date today;

    private Event event;

    @Before
    public void setUp() {
        oldday = DateFactory.getDateFromString("2020-01-05");
        middleday1 = DateFactory.getDateFromString("2020-01-06");
        someday = DateFactory.getDateFromString("2020-01-07");
        middleday2 = DateFactory.getDateFromString("2020-01-08");
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

    private void addAchievement(Date date) {
        Event event = EventFactory.getFinishedEvent(Event.BOOK);
        eventMapper.addEvent(event);
        Achievement achievement = new Achievement();
        achievement.setDate(date);
        achievement.setEvent(event);
        mapper.addAchievement(achievement);
    }

    private void addTestAchievements(int n, Date date) {
        for (int i = 0; i < n; i++) {
            addAchievement(date);
        }
    }

    @Test
    @Transactional
    public void shouldGetSameCountWhenGetAchievements() {
        addTestAchievements(10, today);
        int count = mapper.getAchievementCount(today, today);
        List<Achievement> achievements = mapper.getAchievements(today, today, 0, count);
        assertEquals(count, achievements.size());
    }

    @Test
    @Transactional
    public void shouldCountBeRightWhenGetAchievements() {
        addTestAchievements(12, today);
        List<Achievement> achievements = mapper.getAchievements(today, today, 0, 5);
        assertEquals(5, achievements.size());
    }

    private void addTestDateDates() {
        addAchievement(oldday);
        addAchievement(someday);
        addAchievement(today);
    }

    @Test
    @Transactional
    public void shouldCountBeRightWhenAtBottomLine() {
        int count = mapper.getAchievementCount(oldday, middleday1);
        addTestDateDates();
        List<Achievement> achievements = mapper.getAchievements(oldday, middleday1, count, 3);
        assertEquals(mapper.getAchievementCount(oldday, middleday1), 1);
        assertEquals(achievements.size(), 1);
    }

    @Test
    @Transactional
    public void shouldCountBeRightWhenAtUpperLine() {
        int count = mapper.getAchievementCount(middleday1, today);
        addTestDateDates();
        List<Achievement> achievements = mapper.getAchievements(middleday1, today, count, 3);
        assertEquals(mapper.getAchievementCount(middleday1, today), 2);
        assertEquals(achievements.size(), 2);
    }

    @Test
    @Transactional
    public void shouldOrderBeRightWhenGetAchievements() {
        int count = mapper.getAchievementCount(oldday, today);
        addAchievement(today);
        addAchievement(oldday);
        List<Achievement> achievements = mapper.getAchievements(oldday, today, count, 2);
        assertEquals(achievements.get(0).getDate().toString(), oldday.toString());
    }

    @Test
    @Transactional
    public void shouldAddWhenThisEventIsAdded() {
        Achievement achievement = new Achievement();
        achievement.setDate(someday);
        achievement.setEvent(event);
        mapper.addAchievement(achievement);
        int count = mapper.getAchievementCount(someday, today);
        achievement.setDate(today);
        mapper.addAchievement(achievement);
        assertEquals(count, mapper.getAchievementCount(someday, today));

    }
}