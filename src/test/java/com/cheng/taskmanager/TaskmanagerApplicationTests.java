package com.cheng.taskmanager;

import com.cheng.taskmanager.mapper.EventMapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventMapperTest.class
})
@SpringBootTest
public class TaskmanagerApplicationTests {

}
