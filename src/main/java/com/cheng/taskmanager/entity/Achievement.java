package com.cheng.taskmanager.entity;

import java.sql.Date;

public class Achievement {
    private int eid;
    private Date date;

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
