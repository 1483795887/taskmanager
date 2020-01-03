package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMapper {
    int getCurrentEventCount();
    int addEvent(Event event);
    void close(int id);
    int getClosedEventCount();
    void finish(int id);
    int getFinishedEventCount();
}
