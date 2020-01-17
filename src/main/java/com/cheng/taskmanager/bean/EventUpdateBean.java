package com.cheng.taskmanager.bean;

import javax.validation.constraints.NotNull;

public class EventUpdateBean {
    @NotNull
    private Integer id;
    private Integer targetProgress;
    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTargetProgress() {
        return targetProgress;
    }

    public void setTargetProgress(Integer targetProgress) {
        this.targetProgress = targetProgress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
