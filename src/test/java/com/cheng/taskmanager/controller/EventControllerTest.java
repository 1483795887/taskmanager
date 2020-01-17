package com.cheng.taskmanager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.ResultBean;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.service.EventService;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.EventFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.ArrayList;

import static com.cheng.taskmanager.utils.EventFactory.addProgress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private EventService service;

    @Autowired
    private WebApplicationContext context;

    private Event event;
    private Date someday;
    private Date today;

    private final static int TEST_EVENT_ID = 1;

    private EventBean getEventBean() {
        EventBean bean = new EventBean();
        bean.setName("test");
        bean.setType(Event.BOOK);
        bean.setTargetProgress(100);

        return bean;
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        event = EventFactory.getCurrentEvent(Event.BOOK);

        someday = DateFactory.getDateFromString("2020-01-16");
        today = DateFactory.getToday();

        event = EventFactory.getCurrentEvent(Event.BOOK);
        event.setId(TEST_EVENT_ID);
        event.setProgressList(new ArrayList<>());
        addProgress(event, 10, 10, someday);
        addProgress(event, 20, 10, today);
    }

    private JSONObject postDate(String url, Object object) throws Exception {
        MvcResult result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(object))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String string = result.getResponse().getContentAsString();
        return JSONObject.parseObject(string);
    }

    private void checkFailed(JSONObject map) {
        ResultBean bean1 = map.getJSONObject("result").toJavaObject(ResultBean.class);
        assertNotNull(bean1);
        assertEquals(bean1.getCode(), ResultBean.FAILED);
    }

    private void checkSucceed(JSONObject map) {
        ResultBean bean1 = map.getJSONObject("result").toJavaObject(ResultBean.class);
        assertNotNull(bean1);
        assertEquals(bean1.getCode(), ResultBean.SUCCESS);
    }

    @Test
    public void shouldReturnFailResultWhenNameNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setName(null);
        JSONObject map = postDate("/event/addEvent", bean);
        checkFailed(map);
    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(null);
        JSONObject map = postDate("/event/addEvent", bean);
        checkFailed(map);

    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressNegative() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(-1);
        JSONObject map = postDate("/event/addEvent", bean);
        checkFailed(map);
    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressZero() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(0);
        JSONObject map = postDate("/event/addEvent", bean);
        checkFailed(map);
    }

    @Test
    public void shouldReturnFailResultWhenTypeNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setType(null);
        JSONObject map = postDate("/event/addEvent", bean);
        checkFailed(map);
    }

    @Test
    public void shouldReturnSucceedResultWhenEventRight() throws Exception {
        EventBean bean = getEventBean();

        JSONObject map = postDate("/event/addEvent", bean);
        ResultBean bean1 = map.getJSONObject("result").toJavaObject(ResultBean.class);

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        verify(service).addEvent(eventArgumentCaptor.capture());
        Event event = eventArgumentCaptor.getValue();
        assertEquals(event.getName(), bean.getName());
        assertEquals(event.getTargetProgress(), bean.getTargetProgress());
        assertEquals(event.getType(), bean.getType());

        checkSucceed(map);
    }

    @Test
    public void shouldReturnBadRequestWhenGetEventIdNull() throws Exception {
        mockMvc.perform(
                post("/event/getEvent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldFailWhenGetEventIdNotExist() throws Exception {
        when(service.getEventById(1)).thenReturn(null);
        JSONObject map = postDate("/event/getEvent", 1);
        checkFailed(map);
    }

    @Test
    public void shouldSucceedWhenGetEventAlRight() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(event);
        JSONObject map = postDate("/event/getEvent", TEST_EVENT_ID);
        checkSucceed(map);
        Event event = map.getJSONObject("event").toJavaObject(Event.class);
        assertNotNull(event);
        assertNotNull(event.getProgressList());
    }
}