package com.cheng.taskmanager;

import com.cheng.taskmanager.mapper.EventMapperTest;
import com.cheng.taskmanager.mapper.ProgressMapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventMapperTest.class,
        ProgressMapperTest.class
})
@SpringBootTest
public class TaskmanagerApplicationTests {

}
