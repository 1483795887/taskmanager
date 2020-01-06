package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMapper {
    int getCurrentEventCount();

    void addEvent(Event event); //返回的id在event中

    void addProgress(Progress progress);    //eid要实现写好

    void close(int id);

    int getClosedEventCount();

    void finish(int id);

    int getFinishedEventCount();

    Event getEventById(int id);

    void update(Event event);

    List<Event> getCurrentEvents(@Param("begin") int begin, @Param("limit") int limit);

    List<Event> getFinishedEvents(@Param("begin") int begin, @Param("limit") int limit);

    List<Event> getClosedEvents(@Param("begin") int begin, @Param("limit") int limit);
}
