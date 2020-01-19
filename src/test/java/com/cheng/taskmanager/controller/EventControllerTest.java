package com.cheng.taskmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventUpdateBean;
import com.cheng.taskmanager.bean.ProgressUpdateBean;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.service.EventService;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.EventFactory;
import com.cheng.taskmanager.utils.PostHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.util.ArrayList;

import static com.cheng.taskmanager.utils.EventFactory.addProgress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventControllerTest {

    private PostHelper helper;
    @MockBean
    private EventService service;

    @Autowired
    private WebApplicationContext context;

    private Event event;
    private Date someday;
    private Date today;

    private final static int TEST_EVENT_ID = 1;

    private final static String ADD_EVENT_URL = "/event/addEvent";
    private final static String GET_EVENT_URL = "/event/getEvent";
    private final static String UPDATE_EVENT_URL = "/event/updateEvent";
    private final static String UPDATE_PROGRESS = "/event/updateProgress";

    private EventBean getEventBean() {
        EventBean bean = new EventBean();
        bean.setName("test");
        bean.setType(Event.BOOK);
        bean.setTargetProgress(100);

        return bean;
    }

    @Before
    public void setUp() {
        helper = new PostHelper(context);
        event = EventFactory.getCurrentEvent(Event.BOOK);

        someday = DateFactory.getDateFromString("2020-01-16");
        today = DateFactory.getToday();

        event = EventFactory.getCurrentEvent(Event.BOOK);
        event.setId(TEST_EVENT_ID);
        event.setProgressList(new ArrayList<>());
        addProgress(event, 10, 10, someday);
        addProgress(event, 20, 10, today);
    }

    @Test
    public void shouldReturnFailResultWhenNameNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setName(null);
        JSONObject map = helper.postDate("/event/addEvent", bean);
        helper.checkFailed(map);
    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(null);
        JSONObject map = helper.postDate(ADD_EVENT_URL, bean);
        helper.checkFailed(map);

    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressNegative() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(-1);
        JSONObject map = helper.postDate(ADD_EVENT_URL, bean);
        helper.checkFailed(map);
    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressZero() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(0);
        JSONObject map = helper.postDate(ADD_EVENT_URL, bean);
        helper.checkFailed(map);
    }

    @Test
    public void shouldReturnFailResultWhenTypeNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setType(null);
        JSONObject map = helper.postDate(ADD_EVENT_URL, bean);
        helper.checkFailed(map);
    }

    @Test
    public void shouldReturnSucceedResultWhenEventRight() throws Exception {
        EventBean bean = getEventBean();

        JSONObject map = helper.postDate(ADD_EVENT_URL, bean);

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        verify(service).addEvent(eventArgumentCaptor.capture());
        Event event = eventArgumentCaptor.getValue();
        assertEquals(event.getName(), bean.getName());
        assertEquals(event.getTargetProgress(), bean.getTargetProgress());
        assertEquals(event.getType(), bean.getType());

        helper.checkSucceed(map);
    }

    @Test
    public void shouldReturnBadRequestWhenGetEventIdNull() throws Exception {
        helper.checkBadRequest(GET_EVENT_URL, null);
    }

    @Test
    public void shouldFailWhenGetEventIdNotExist() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(null);
        JSONObject map = helper.postDate(GET_EVENT_URL, TEST_EVENT_ID);
        helper.checkFailed(map);
    }

    @Test
    public void shouldSucceedWhenGetEventAlRight() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(event);
        JSONObject map = helper.postDate(GET_EVENT_URL, TEST_EVENT_ID);
        helper.checkSucceed(map);
        Event event = map.getJSONObject("event").toJavaObject(Event.class);
        assertNotNull(event);
        assertNotNull(event.getProgressList());
    }

    @Test
    public void shouldBadRequestWhenUpdateEventWithNullId() throws Exception {
        helper.checkBadRequest(UPDATE_EVENT_URL, null);
    }

    @Test
    public void shouldFailWhenUpdateEventIdNotExist() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(null);
        EventUpdateBean eventUpdateBean = new EventUpdateBean();
        eventUpdateBean.setId(TEST_EVENT_ID);
        JSONObject map = helper.postDate(UPDATE_EVENT_URL, eventUpdateBean);
        helper.checkFailed(map);
    }

    @Test
    public void shouldNotChangeWhenUpdateWithNull() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(event);
        String oName = event.getName();
        int oTargetProgress = event.getTargetProgress();

        EventUpdateBean eventUpdateBean = new EventUpdateBean();
        eventUpdateBean.setId(TEST_EVENT_ID);
        JSONObject map = helper.postDate(UPDATE_EVENT_URL, eventUpdateBean);

        verify(service).updateEvent(any(Event.class));
        helper.checkSucceed(map);
        map = helper.postDate(GET_EVENT_URL, TEST_EVENT_ID);
        helper.checkSucceed(map);
        Event event1 = map.getJSONObject("event").toJavaObject(Event.class);
        assertEquals(event1.getName(), oName);
        assertEquals(event1.getTargetProgress(), oTargetProgress);
    }

    @Test
    public void shouldChangeNameWhenUpdateIt() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(event);
        String nName = "newName";
        int oTargetProgress = event.getTargetProgress();

        EventUpdateBean eventUpdateBean = new EventUpdateBean();
        eventUpdateBean.setId(TEST_EVENT_ID);
        eventUpdateBean.setName(nName);
        JSONObject map = helper.postDate(UPDATE_EVENT_URL, eventUpdateBean);

        verify(service).updateEvent(any(Event.class));
        helper.checkSucceed(map);
        map = helper.postDate(GET_EVENT_URL, TEST_EVENT_ID);
        helper.checkSucceed(map);
        Event event1 = map.getJSONObject("event").toJavaObject(Event.class);
        assertEquals(event1.getName(), nName);
        assertEquals(event1.getTargetProgress(), oTargetProgress);
    }

    @Test
    public void shouldChangeTargetProgressWhenUpdateIt() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(event);
        String nName = event.getName();
        int nTargetProgress = 2300;

        EventUpdateBean eventUpdateBean = new EventUpdateBean();
        eventUpdateBean.setId(TEST_EVENT_ID);
        eventUpdateBean.setTargetProgress(nTargetProgress);
        JSONObject map = helper.postDate(UPDATE_EVENT_URL, eventUpdateBean);

        verify(service).updateEvent(any(Event.class));
        helper.checkSucceed(map);
        map = helper.postDate(GET_EVENT_URL, TEST_EVENT_ID);
        helper.checkSucceed(map);
        Event event1 = map.getJSONObject("event").toJavaObject(Event.class);
        assertEquals(event1.getName(), nName);
        assertEquals(event1.getTargetProgress(), nTargetProgress);
    }

    @Test
    public void shouldBadRequestWhenUpdateProgressWithNullData() throws Exception {
        helper.checkBadRequest(UPDATE_PROGRESS, null);
    }

    @Test
    public void shouldFailWhenUpdateProgressWithNullId() throws Exception {
        ProgressUpdateBean bean = new ProgressUpdateBean();
        bean.setProgress(10);
        JSONObject map = helper.postDate(UPDATE_PROGRESS, bean);
        helper.checkFailed(map);
    }

    @Test
    public void shouldFailWhenUpdateProgressWithNullProgress() throws Exception {
        ProgressUpdateBean bean = new ProgressUpdateBean();
        bean.setId(TEST_EVENT_ID);
        JSONObject map = helper.postDate(UPDATE_PROGRESS, bean);
        helper.checkFailed(map);
    }

    @Test
    public void shouldFailWhenUpdateProgressWithMinusProgress() throws Exception {
        ProgressUpdateBean bean = new ProgressUpdateBean();
        bean.setId(TEST_EVENT_ID);
        bean.setProgress(-1);
        JSONObject map = helper.postDate(UPDATE_PROGRESS, bean);
        helper.checkFailed(map);
    }
}