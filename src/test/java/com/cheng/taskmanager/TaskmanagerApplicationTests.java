package com.cheng.taskmanager;

import com.cheng.taskmanager.mapper.EventMapperTest;
import com.cheng.taskmanager.mapper.ReadRecordMapperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EventMapperTest.class,
        ReadRecordMapperTest.class
})
@SpringBootTest
public class TaskmanagerApplicationTests {

}
