package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.utils.DateFactory;
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

    private Date beginDate, endDate, outDate, middleDate1, middleDate2;

    private ReadRecord readRecord;

    @Before
    public void setUp() {
        beginDate = DateFactory.getDateFromString("2019-01-07");
        endDate = DateFactory.getDateFromString("2020-01-07");
        outDate = DateFactory.getDateFromString("2019-01-05");
        middleDate1 = DateFactory.getDateFromString("2019-01-06");
        middleDate2 = DateFactory.getDateFromString("2019-01-08");

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

    @Test
    @Transactional
    public void shouldRecordBeAddedUpWhenAddTwoRecordSameDay() {
        int record1 = 10, record2 = 20;
        readRecord.setRecord(record1);
        mapper.addReadRecord(readRecord);
        readRecord.setRecord(record2);
        mapper.addReadRecord(readRecord);

        List<ReadRecord> records = mapper.getReadRecords(beginDate, beginDate);
        assertEquals(1, records.size());

        ReadRecord readRecord1 = records.get(0);

        assertEquals(record1 + record2, readRecord1.getRecord());
    }

    private void addRecord(Date date) {
        readRecord.setDate(date);
        mapper.addReadRecord(readRecord);
    }

    private void addTestRecords() {
        addRecord(outDate);
        addRecord(beginDate);
        addRecord(endDate);
    }

    @Test
    @Transactional
    public void shouldGetSizeRightWhenGetRecordsAtBottomLine() {
        addTestRecords();
        assertEquals(2, mapper.getReadRecords(outDate, middleDate2).size());
    }

    @Test
    @Transactional
    public void shouldGetSizeRightWhenGetRecordsAtUpperLine() {
        addTestRecords();
        assertEquals(2, mapper.getReadRecords(middleDate1, endDate).size());
    }

}
