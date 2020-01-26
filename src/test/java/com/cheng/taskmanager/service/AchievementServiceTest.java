package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Achievement;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.mapper.AchievementMapper;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.impl.AchievementServiceImpl;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.EventFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AchievementServiceTest {

    private final static int TEST_EVENT_ID = 1;

    private AchievementMapper achievementMapper;
    private EventMapper eventMapper;
    private AchievementService service;
    private Date someday;
    private Date today;

    private List<Achievement> achievementList;

    private Event addAchievement(int id, int type) {
        Event event = EventFactory.getCurrentEvent(type);
        event.setId(id);
        Achievement achievement = new Achievement();
        achievement.setEid(id);
        achievementList.add(achievement);
        return event;
    }

    @Before
    public void setUp() {
        achievementMapper = mock(AchievementMapper.class);
        eventMapper = mock(EventMapper.class);
        service = new AchievementServiceImpl(achievementMapper, eventMapper);

        someday = DateFactory.getDateFromString("2020-01-20");
        today = DateFactory.getToday();

        achievementList = new ArrayList<>();

    }

    @Test
    public void shouldCallRightWhenGetAchievements() {
        List<EventInfo> eventInfoList = service.getAchievements(
                today, today, Event.ALL);
        verify(achievementMapper).getAchievements(today, today);
        assertNotNull(eventInfoList);
    }

    @Test
    public void shouldExchangeDateWhenEndBeforeStart() {
        service.getAchievements(today, someday, Event.ALL);
        verify(achievementMapper).getAchievements(someday, today);
    }

    @Test
    public void shouldResultRightWhenGetAchievements() {
        Event event = addAchievement(TEST_EVENT_ID, Event.GAME);
        when(achievementMapper.getAchievements(
                any(Date.class), any(Date.class))).
                thenReturn(achievementList);
        when(eventMapper.getEventById(anyInt())).thenReturn(event);
        List<EventInfo> infos = service.getAchievements(someday, today, Event.ALL);
        EventInfo info = infos.get(0);
        EventBean bean = info.getEvent();
        assertEquals(bean.getName(), event.getName());
        assertEquals(info.getProgress().getProgress(), event.getTargetProgress());
        assertEquals(info.getProgress().getRecord(), event.getTargetProgress());
    }
}
