package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.DateFactory;
import com.cheng.taskmanager.entity.ReadRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReadRecordMapperTest {

    @Autowired
    ReadRecordMapper mapper;

    Date beginDate, endDate, outDate;

    ReadRecord readRecord;

    @Before
    public void setUp() throws Exception {
        beginDate = DateFactory.getDateFromString("2019-01-07");
        endDate = DateFactory.getDateFromString("2020-01-07");
        outDate = DateFactory.getDateFromString("2019-01-06");

        readRecord = new ReadRecord();
        readRecord.setDate(beginDate);
        readRecord.setRecord(10);
    }

    @Test
    @Transactional
    public void shouldNotBeNullWhenGetRecords() {
        assertNotNull(mapper.getReadRecords(beginDate, endDate));
    }

    @Test
    @Transactional
    public void shouldBeSameWhenGetAddedRecord() {
        mapper.addReadRecord(readRecord);

        List<ReadRecord> records = mapper.getReadRecords(beginDate, beginDate);

        assertEquals(1, records.size());
        ReadRecord readRecord1 = records.get(0);
        assertEquals(readRecord1.getDate().toString(), readRecord.getDate().toString());
        assertEquals(readRecord1.getRecord(), readRecord.getRecord());
    }
}
