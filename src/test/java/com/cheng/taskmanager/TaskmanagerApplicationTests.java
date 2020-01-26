package com.cheng.taskmanager;

import com.cheng.taskmanager.controller.EventControllerTest;
import com.cheng.taskmanager.controller.ProgressControllerTest;
import com.cheng.taskmanager.mapper.AchievementMapperTest;
import com.cheng.taskmanager.mapper.EventMapperTest;
import com.cheng.taskmanager.service.AchievementServiceTest;
import com.cheng.taskmanager.service.EventServiceTest;
import com.cheng.taskmanager.service.ProgressServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventMapperTest.class,
        AchievementMapperTest.class,
        EventServiceTest.class,
        ProgressServiceTest.class,
        AchievementServiceTest.class,
        EventControllerTest.class,
        ProgressControllerTest.class
})
@SpringBootTest
public class TaskmanagerApplicationTests {

}
