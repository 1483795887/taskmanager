package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.bean.DateRegion;
import com.cheng.taskmanager.entity.Achievement;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.EventFactory;
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

    @Autowired
    AchievementMapper mapper;
    @Autowired
    EventMapper eventMapper;

    private Date oldday;
    private Date middleday1;
    private Date someday;
    private Date today;

    private DateRegion region;

    @Before
    public void setUp() {
        oldday = DateFactory.getDateFromString("2020-01-05");
        middleday1 = DateFactory.getDateFromString("2020-01-06");
        someday = DateFactory.getDateFromString("2020-01-07");
        today = DateFactory.getToday();
    }

    private Achievement addAchievement(Date date) {
        Event event = EventFactory.getFinishedEvent(Event.BOOK);
        eventMapper.addEvent(event);
        Achievement achievement = new Achievement();
        achievement.setDate(date);
        achievement.setEid(event.getId());
        mapper.addAchievement(achievement);
        return achievement;
    }

    private List<Achievement> getAchievements(Date start, Date end) {
        DateRegion region = new DateRegion();
        region.setStartDate(start);
        region.setEndDate(end);
        return mapper.getAchievements(region);
    }

    @Test
    @Transactional
    public void shouldNotNullWhenGetAchievements() {
        assertNotNull(getAchievements(someday, someday));
    }

    private int getAchievementCount(Date start, Date end) {
        DateRegion region = new DateRegion();
        region.setStartDate(start);
        region.setEndDate(end);
        return mapper.getAchievementCount(region);
    }

    @Test
    @Transactional
    public void shouldCountIncWhenAddAchievement() {
        int count = getAchievementCount(someday, someday);
        addAchievement(someday);
        assertEquals(count + 1, getAchievementCount(someday, someday));
    }


    private void addTestDateDates() {
        addAchievement(oldday);
        addAchievement(someday);
        addAchievement(today);
    }

    @Test
    @Transactional
    public void shouldCountBeRightWhenAtBottomLine() {
        addTestDateDates();
        List<Achievement> achievements = getAchievements(oldday, middleday1);
        assertEquals(getAchievementCount(oldday, middleday1), 1);
        assertEquals(achievements.size(), 1);
    }

    @Test
    @Transactional
    public void shouldCountBeRightWhenAtUpperLine() {
        addTestDateDates();
        List<Achievement> achievements = getAchievements(middleday1, today);
        assertEquals(getAchievementCount(middleday1, today), 2);
        assertEquals(achievements.size(), 2);
    }

    @Test
    @Transactional
    public void shouldOrderBeRightWhenGetAchievements() {
        addAchievement(today);
        addAchievement(oldday);
        List<Achievement> achievements = getAchievements(oldday, today);
        assertEquals(achievements.get(0).getDate().toString(), oldday.toString());
    }

}