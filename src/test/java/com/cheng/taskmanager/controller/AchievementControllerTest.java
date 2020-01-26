package com.cheng.taskmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.service.AchievementService;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.PostHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AchievementControllerTest {
    private final static String GET_ACHIEVEMENTS = "/achievement/getAchievements";
    private PostHelper helper;
    @MockBean
    private AchievementService service;
    @Autowired
    private WebApplicationContext context;
    private DateAndTypeBean searchBean;
    private Date today;

    @Before
    public void setUp() {
        helper = new PostHelper(context);
        today = DateFactory.getToday();

        searchBean = new DateAndTypeBean();
        searchBean.setStartDate(today);
        searchBean.setEndDate(today);
        searchBean.setType(Event.BOOK);

    }

    @Test
    public void shouldBadRequestWhenBodyNull() throws Exception {
        helper.checkBadRequest(GET_ACHIEVEMENTS, null);
    }

    @Test
    public void shouldFailWhenStartDateIsNull() throws Exception {
        searchBean.setStartDate(null);
        helper.checkFailed(GET_ACHIEVEMENTS, searchBean);
    }

    @Test
    public void shouldFailWhenEndDateIsNull() throws Exception {
        searchBean.setEndDate(null);
        helper.checkFailed(GET_ACHIEVEMENTS, searchBean);
    }

    @Test
    public void shouldFailWhenTypeIsNull() throws Exception {
        searchBean.setType(null);
        helper.checkFailed(GET_ACHIEVEMENTS, searchBean);
    }

    @Test
    public void shouldResponseRightWhenGetAchievements() throws Exception {

        List<EventInfo> eventInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            EventInfo eventInfo = new EventInfo();
            Progress progress = new Progress();
            progress.setRecord(10);
            eventInfo.setProgress(progress);
            eventInfos.add(eventInfo);
        }

        when(service.getAchievements(searchBean))
                .thenReturn(eventInfos);

        JSONObject map = helper.postDate(GET_ACHIEVEMENTS, searchBean);
        verify(service).getAchievements(searchBean);
        EventInfo[] infos = map.getJSONArray("infos")
                .toJavaObject(EventInfo[].class);
        Assertions.assertNotNull(infos);
        List<EventInfo> infos1 = Arrays.asList(infos);
        assertEquals(eventInfos.size(), infos1.size());
        Integer sum = map.getInteger("sum");
        assertNotNull(sum);
        assertEquals(100, sum);
    }
}