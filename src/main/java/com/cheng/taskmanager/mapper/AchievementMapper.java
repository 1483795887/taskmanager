package com.cheng.taskmanager.mapper;

import com.cheng.taskmanager.entity.Achievement;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AchievementMapper {
    List<Achievement> getAchievements(@Param("startDate") Date startDate,
                                      @Param("endDate") Date date,
                                      @Param("begin") int begin,
                                      @Param("limit") int limit);
}
