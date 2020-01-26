package com.cheng.taskmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.bean.GetProgressBean;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.service.ProgressService;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.PostHelper;
import org.junit.Before;
import org.junit.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProgressControllerTest {
    private final static String GET_PROGRESSES = "/progress/getProgresses";
    private PostHelper helper;
    @MockBean
    private ProgressService service;
    @Autowired
    private WebApplicationContext context;
    private GetProgressBean progressBean;
    private Date today;

    @Before
    public void setUp() {
        helper = new PostHelper(context);
        today = DateFactory.getToday();

        progressBean = new GetProgressBean();
        progressBean.setStartDate(today);
        progressBean.setEndDate(today);
        progressBean.setType(Event.BOOK);
    }

    @Test
    public void shouldBadRequestWhenBodyNull() throws Exception {
        helper.checkBadRequest(GET_PROGRESSES, null);
    }

    @Test
    public void shouldFailWhenStartDateIsNull() throws Exception {
        progressBean.setStartDate(null);
        helper.checkFailed(GET_PROGRESSES, progressBean);
    }

    @Test
    public void shouldFailWhenEndDateIsNull() throws Exception {
        progressBean.setEndDate(null);
        helper.checkFailed(GET_PROGRESSES, progressBean);
    }

    @Test
    public void shouldFailWhenTypeIsNull() throws Exception {
        progressBean.setType(null);
        helper.checkFailed(GET_PROGRESSES, progressBean);
    }

    @Test
    public void shouldResponseRightWhenGetProgresses() throws Exception {
        List<EventInfo> eventInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            eventInfos.add(new EventInfo());
        }

        int sum = 10;
        when(service.getProgresses(any(Date.class), any(Date.class), anyInt()))
                .thenReturn(eventInfos);
        when(service.getSumRecord(eventInfos)).thenReturn(sum);

        JSONObject map = helper.postDate(GET_PROGRESSES, progressBean);
        verify(service).getProgresses(today, today, Event.BOOK);
        verify(service).getSumRecord(eventInfos);

        helper.checkSucceed(map);
        EventInfo[] infos = map.getJSONArray("progresses")
                .toJavaObject(EventInfo[].class);
        assertNotNull(infos);
        List<EventInfo> infos1 = Arrays.asList(infos);
        assertEquals(eventInfos.size(), infos1.size());
        int resultSum = map.getInteger("sum");
        assertEquals(sum, resultSum);
    }
}