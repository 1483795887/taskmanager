package com.cheng.taskmanager.entity;

import java.sql.Date;

public class ReadRecord {
    private Date date;
    private Integer record;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getRecord() {
        return record;
    }

    public void setRecord(Integer record) {
        this.record = record;
    }
}
