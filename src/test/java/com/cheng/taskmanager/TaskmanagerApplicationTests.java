package com.cheng.taskmanager;

import com.cheng.taskmanager.mapper.AchievementMapperTest;
import com.cheng.taskmanager.mapper.EventMapperTest;
import com.cheng.taskmanager.mapper.ReadRecordMapperTest;
import com.cheng.taskmanager.service.EventServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventMapperTest.class,
        ReadRecordMapperTest.class,
        AchievementMapperTest.class,
        EventServiceTest.class
})
@SpringBootTest
public class TaskmanagerApplicationTests {

}
