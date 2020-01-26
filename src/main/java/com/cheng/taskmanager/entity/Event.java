package com.cheng.taskmanager.entity;

import java.sql.Date;
import java.util.List;

public class Event {
    public final static int ALL = 0;
    public final static int BOOK = 1;
    public final static int ANIM = 2;
    public final static int GAME = 3;
    public final static int LEGACY = 100;

    private Integer id;
    private String name;
    private Integer targetProgress;
    private Date startDate;
    private Boolean running;
    private Integer type;
    private List<Progress> progressList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTargetProgress() {
        return targetProgress;
    }

    public void setTargetProgress(Integer targetProgress) {
        this.targetProgress = targetProgress;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Progress> getProgressList() {
        return progressList;
    }

    public void setProgressList(List<Progress> progressList) {
        this.progressList = progressList;
    }
}
