package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMapper {
    List<Event> findCurrentEvents();
}
