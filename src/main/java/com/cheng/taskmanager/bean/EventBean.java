package com.cheng.taskmanager.bean;

import com.cheng.taskmanager.entity.Event;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Date;

public class EventBean {
    private int id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @PastOrPresent
    private Date startDate;
    @NotNull
    @PositiveOrZero
    private Integer targetProgress;
    @NotNull
    private Integer type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getTargetProgress() {
        return targetProgress;
    }

    public void setTargetProgress(Integer targetProgress) {
        this.targetProgress = targetProgress;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public static EventBean getFromEvent(Event event){
        EventBean eventBean = new EventBean();
        eventBean.setId(event.getId());
        eventBean.setName(event.getName());
        eventBean.setStartDate(event.getStartDate());
        eventBean.setTargetProgress(event.getTargetProgress());
        eventBean.setType(event.getType());
        return eventBean;
    }
}
