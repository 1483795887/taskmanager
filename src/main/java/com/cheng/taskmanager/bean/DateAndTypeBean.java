package com.cheng.taskmanager.bean;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class DateAndTypeBean {
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    @NotNull
    private Integer type;

    public DateAndTypeBean() {
    }

    public DateAndTypeBean(Date startDate, Date endDate, Integer type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DateAndTypeBean))
            return false;
        DateAndTypeBean bean = (DateAndTypeBean) obj;
        return endDate.toString().equals(bean.getEndDate().toString()) &&
                startDate.toString().equals(bean.getStartDate().toString()) &&
                type.equals(bean.getType());
    }
}
