package com.cheng.taskmanager.bean;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class ProgressUpdateBean {
    @NotNull
    private Integer id;
    @NotNull
    @PositiveOrZero
    private Integer progress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
