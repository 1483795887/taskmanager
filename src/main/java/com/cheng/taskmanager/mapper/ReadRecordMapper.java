package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.ReadRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReadRecordMapper {

    void addReadRecord(ReadRecord readRecord);

    List<ReadRecord> getReadRecords(@Param("begin") Date begin, @Param("end") Date end);
}
