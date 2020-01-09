package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.utils.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AchievementMapperTest {
    @Autowired AchievementMapper mapper;

    private Date someday;
    private Date today;

    @Before
    public void setUp(){
        someday = DateFactory.getDateFromString("2020-01-08");
        today = DateFactory.getToday();
    }

    @Test
    @Transactional
    public void shouldNotNullWhenGetAchievements(){
        assertNotNull(mapper.getAchievements(someday,someday, 0, 1));
    }
}