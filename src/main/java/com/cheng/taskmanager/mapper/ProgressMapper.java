package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Progress;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressMapper {
    List<Progress> getProgresses(int eid);
}
