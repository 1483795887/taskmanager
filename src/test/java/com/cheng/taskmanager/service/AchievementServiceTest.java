package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.mapper.AchievementMapper;
import com.cheng.taskmanager.service.impl.AchievementServiceImpl;
import com.cheng.taskmanager.utils.DateFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AchievementServiceTest {
    private AchievementMapper achievementMapper;
    private AchievementService service;
    private Date today;

    @Before
    public void setUp() throws Exception {
        achievementMapper = mock(AchievementMapper.class);
        service = new AchievementServiceImpl(achievementMapper);

        today = DateFactory.getToday();
    }

    @Test
    public void shouldCallRightWhenGetAchievements() {
        List<EventInfo> eventInfoList = service.getAchievements(
                today, today, Event.ALL);
        verify(achievementMapper).getAchievements(today, today);
        assertNotNull(eventInfoList);
    }
}
