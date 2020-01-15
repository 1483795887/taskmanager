package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.impl.ProgressServiceImpl;
import com.cheng.taskmanager.utils.DateFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProgressServiceTest {

    private EventMapper eventMapper;
    private ProgressService service;
    private Date someday;
    private Date today;

    @Before
    public void setUp() {
        eventMapper = mock(EventMapper.class);
        service = new ProgressServiceImpl(eventMapper);

        someday = DateFactory.getDateFromString("2020-01-10");
        today = DateFactory.getToday();
    }

    @Test
    public void shouldCallRightWhenGetProgresses(){
        List<EventInfo> eventInfos = service.getProgresses(someday, today);
        verify(eventMapper).getProgresses(any(Date.class), any(Date.class));
        assertNotNull(eventInfos);
    }
}
