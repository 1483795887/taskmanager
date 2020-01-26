package com.cheng.taskmanager.bean;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Date;

public class DateAndTypeBean {
    @Valid
    private DateRegion region;
    @NotNull
    private Integer type;

    public DateAndTypeBean() {
    }

    public DateAndTypeBean(Date startDate, Date endDate, Integer type) {
        region = new DateRegion(startDate, endDate);
        this.type = type;
    }

    public DateRegion getRegion() {
        return region;
    }

    public void setRegion(DateRegion region) {
        this.region = region;
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
        return region.equals(bean.getRegion()) &&
                type.equals(bean.getType());
    }
}
