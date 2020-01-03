package com.cheng.taskmanager.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventMapperTest {

    @Autowired
    EventMapper mapper;

    @Test   //获得当前事件不为空
    public void shouldNotBeNullWhenGetCurrentEvents() {
        assertNotNull(mapper.findCurrentEvents());
    }


}