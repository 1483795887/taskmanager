package com.cheng.taskmanager.bean;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class DateRegion {
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;

    public DateRegion() {
    }

    public DateRegion(@NotNull Date startDate, @NotNull Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void checkOrder() {
        if (startDate.after(endDate)) {
            Date temp = endDate;
            endDate = startDate;
            startDate = temp;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DateRegion))
            return false;
        DateRegion bean = (DateRegion) obj;
        return endDate.toString().equals(bean.getEndDate().toString()) &&
                startDate.toString().equals(bean.getStartDate().toString());
    }
}
