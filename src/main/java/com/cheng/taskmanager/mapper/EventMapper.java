package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMapper {
    int getCurrentEventCount();
    void addEvent(Event event); //返回的id在event中
    void close(int id);
    int getClosedEventCount();
    void finish(int id);
    int getFinishedEventCount();

    Event getEventById(int id);
}
