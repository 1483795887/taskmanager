package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.DateRegion;
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
    private DateRegion region;

    private DateAndTypeBean inputBean;

    private List<Achievement> achievementList;

    private Event addAchievement(int id, int type) {
        Event event = EventFactory.getCurrentEvent(type);
        event.setId(id);
        event.setStartDate(today);
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

        inputBean = new DateAndTypeBean(someday, today, Event.ALL);

        region = new DateRegion();
        region.setStartDate(someday);
        region.setEndDate(today);
    }

    @Test
    public void shouldCallRightWhenGetAchievements() {
        List<EventInfo> eventInfoList = service.getAchievements(inputBean);
        verify(achievementMapper).getAchievements(region);
        assertNotNull(eventInfoList);
    }

    @Test
    public void shouldExchangeDateWhenEndBeforeStart() {
        service.getAchievements(new DateAndTypeBean(today, someday, Event.ALL));
        verify(achievementMapper).getAchievements(region);
    }

    @Test
    public void shouldResultRightWhenGetAchievements() {
        Event event = addAchievement(TEST_EVENT_ID, Event.GAME);
        when(achievementMapper.getAchievements(any(DateRegion.class))).
                thenReturn(achievementList);
        when(eventMapper.getEventById(anyInt())).thenReturn(event);
        List<EventInfo> infos = service.getAchievements(
                new DateAndTypeBean(someday, today, Event.ALL));
        EventInfo info = infos.get(0);
        EventBean bean = info.getEvent();
        assertEquals(bean.getName(), event.getName());
        assertEquals(info.getProgress().getProgress(), event.getTargetProgress());
        assertEquals(info.getProgress().getRecord(), event.getTargetProgress());
    }

    private void addTestDataForType() {
        int id = TEST_EVENT_ID;
        for (int i = 0; i < 5; i++) {
            Event event = addAchievement(id, Event.BOOK);
            when(eventMapper.getEventById(id)).thenReturn(event);
            id++;
        }
        for (int i = 0; i < 4; i++) {
            Event event = addAchievement(id, Event.ANIM);
            when(eventMapper.getEventById(id)).thenReturn(event);
            id++;
        }
        for (int i = 0; i < 3; i++) {
            Event event = addAchievement(id, Event.GAME);
            when(eventMapper.getEventById(id)).thenReturn(event);
            id++;
        }
        for (int i = 0; i < 2; i++) {
            Event event = addAchievement(id, Event.LEGACY);
            when(eventMapper.getEventById(id)).thenReturn(event);
            id++;
        }
    }

    @Test
    public void shouldSelectAllWhenGetAll() {
        addTestDataForType();
        when(achievementMapper.getAchievements(region)).thenReturn(achievementList);
        List<EventInfo> eventInfoList = service.getAchievements(inputBean);
        assertEquals(eventInfoList.size(), 14);
    }

    @Test
    public void shouldSelectExactTypeWhenGetAchievements() {
        addTestDataForType();
        when(achievementMapper.getAchievements(region)).thenReturn(achievementList);
        inputBean.setType(Event.LEGACY);
        List<EventInfo> eventInfoList = service.getAchievements(inputBean);
        assertEquals(eventInfoList.size(), 2);
        for (EventInfo eventInfo : eventInfoList) {
            assertEquals(eventInfo.getEvent().getType(), Event.LEGACY);
        }
    }
}
