package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgressMapperTest {

    private final static int NOT_EXIST_EVENT_ID = 10000;
    private final static int EXIST_EVENT_ID = 1;
    private final static int EVENT_WITH_PROGRESS = 8;
    private final static int EVENT_WITH_PROGRESS_THE_COUNT = 1;

    @Autowired
   ProgressMapper mapper;

    @Test
    @Transactional
    public void shouldNotNullWhenGetFromEventNotExist(){
        List<Progress> progresses = mapper.getProgresses(NOT_EXIST_EVENT_ID);
        assertNotNull(progresses);
    }

    @Test
    @Transactional
    public void shouldSizeZeroWhenGetFromEventExistWithoutProgress(){
        List<Progress> progresses = mapper.getProgresses(EXIST_EVENT_ID);
        assertEquals(0, progresses.size());
    }
}