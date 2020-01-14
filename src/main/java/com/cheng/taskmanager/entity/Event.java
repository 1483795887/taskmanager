package com.cheng.taskmanager.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.sql.Date;
import java.util.List;

public class Event {
    public final static int All = 0;
    public final static int BOOK = 1;
    public final static int ANIM = 2;
    public final static int OTHER = 3;
    public final static int CARD = 1000;

    private Integer id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @Positive
    private Integer targetProgress;
    private Date startDate;
    private Boolean running;
    @NotNull
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
