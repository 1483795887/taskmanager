package com.cheng.taskmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.bean.EventUpdateBean;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
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
import java.util.Arrays;
import java.util.List;

import static com.cheng.taskmanager.utils.EventFactory.addProgress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventControllerTest {

    private final static int TEST_EVENT_ID = 1;
    private final static int OLD_PROGRESS = 10;
    private final static int NEW_PROGRESS = 20;
    private final static int OLD_TARGET_PROGRESS = 100;
    private final static int NEW_TARGET_PROGRESS = 200;
    private final static String OLD_NAME = "test";
    private final static String NEW_NAME = "newTest";
    private final static String ADD_EVENT_URL = "/event/addEvent";
    private final static String GET_EVENT_URL = "/event/getEvent";
    private final static String UPDATE_EVENT_URL = "/event/updateEvent";
    private final static String GET_CURRENT_EVENTS = "/event/getCurrentEvents";
    private PostHelper helper;
    @MockBean
    private EventService service;
    @Autowired
    private WebApplicationContext context;
    private Event event;
    private Date someday;
    private Date today;

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
        addProgress(event, OLD_PROGRESS, 10, today);

    }

    @Test
    public void shouldReturnFailResultWhenNameNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setName(null);
        helper.checkFailed(ADD_EVENT_URL, bean);
    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(null);
        helper.checkFailed(ADD_EVENT_URL, bean);

    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressNegative() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(-1);
        helper.checkFailed(ADD_EVENT_URL, bean);
    }

    @Test
    public void shouldReturnFailResultWhenTargetProgressZero() throws Exception {
        EventBean bean = getEventBean();
        bean.setTargetProgress(0);
        helper.checkFailed(ADD_EVENT_URL, bean);
    }

    @Test
    public void shouldReturnFailResultWhenTypeNull() throws Exception {
        EventBean bean = getEventBean();
        bean.setType(null);
        helper.checkFailed(ADD_EVENT_URL, bean);
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
        helper.checkFailed(GET_EVENT_URL, TEST_EVENT_ID);
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

    private void checkFailForUpdate(Integer id, String name,
                                    Integer tp, Integer cp) throws Exception {
        when(service.getEventById(id)).thenReturn(event);

        EventUpdateBean eventUpdateBean = new EventUpdateBean();
        eventUpdateBean.setId(id);
        eventUpdateBean.setName(name);
        eventUpdateBean.setCurrentProgress(cp);
        eventUpdateBean.setTargetProgress(tp);
        helper.checkFailed(UPDATE_EVENT_URL, eventUpdateBean);
    }

    @Test
    public void shouldFailWhenUpdateEventIdNotExist() throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(null);
        EventUpdateBean eventUpdateBean = new EventUpdateBean();
        eventUpdateBean.setId(TEST_EVENT_ID);
        eventUpdateBean.setName(OLD_NAME);
        eventUpdateBean.setCurrentProgress(OLD_PROGRESS);
        eventUpdateBean.setTargetProgress(OLD_TARGET_PROGRESS);
        helper.checkFailed(UPDATE_EVENT_URL, eventUpdateBean);
    }

    @Test
    public void shouldFailWhenUpdateWithNullName() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, null,
                OLD_TARGET_PROGRESS, OLD_PROGRESS);
    }

    @Test
    public void shouldFailWhenUpdateWithEmptyName() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, "",
                OLD_TARGET_PROGRESS, OLD_PROGRESS);
    }

    @Test
    public void shouldFailWhenUpdateWithNullTargetProgress() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, OLD_NAME,
                null, OLD_PROGRESS);
    }

    @Test
    public void shouldFailWhenUpdateWithZeroTargetProgress() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, OLD_NAME,
                0, OLD_PROGRESS);
    }

    @Test
    public void shouldFailWhenUpdateWithMinusTargetProgress() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, OLD_NAME,
                -1, OLD_PROGRESS);
    }

    @Test
    public void shouldFailWhenUpdateWithTPLowerThanCP() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, OLD_NAME,
                OLD_PROGRESS - 1, OLD_PROGRESS);
    }

    @Test
    public void shouldFailWhenUpdateWithNullCurrentProgress() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, OLD_NAME,
                OLD_TARGET_PROGRESS, null);
    }

    @Test
    public void shouldFailWhenUpdateWithMinusCurrentProgress() throws Exception {
        checkFailForUpdate(TEST_EVENT_ID, OLD_NAME,
                OLD_TARGET_PROGRESS, -1);
    }

    private void checkChangedForUpdate(String name, int targetProgress,
                                       int currentProgress) throws Exception {
        when(service.getEventById(TEST_EVENT_ID)).thenReturn(event);

        EventUpdateBean eventUpdateBean = new EventUpdateBean();
        eventUpdateBean.setId(TEST_EVENT_ID);
        eventUpdateBean.setName(name);
        eventUpdateBean.setTargetProgress(targetProgress);
        eventUpdateBean.setCurrentProgress(currentProgress);
        JSONObject map = helper.postDate(UPDATE_EVENT_URL, eventUpdateBean);

        verify(service).updateEvent(any(Event.class));
        helper.checkSucceed(map);
        map = helper.postDate(GET_EVENT_URL, TEST_EVENT_ID);
        helper.checkSucceed(map);
        Event event1 = map.getJSONObject("event").toJavaObject(Event.class);
        assertEquals(event1.getName(), name);
        assertEquals(event1.getTargetProgress(), targetProgress);
        verify(service).updateProgress(TEST_EVENT_ID, currentProgress);
    }

    @Test
    public void shouldChangeNameWhenUpdateIt() throws Exception {
        checkChangedForUpdate(NEW_NAME, OLD_TARGET_PROGRESS, OLD_PROGRESS);
    }

    @Test
    public void shouldChangeTargetProgressWhenUpdateIt() throws Exception {
        checkChangedForUpdate(OLD_NAME, NEW_TARGET_PROGRESS, OLD_PROGRESS);
    }

    @Test
    public void shouldChangeCurrentProgressWhenUpdateIt() throws Exception {
        checkChangedForUpdate(OLD_NAME, OLD_TARGET_PROGRESS, NEW_PROGRESS);
    }

    @Test
    public void shouldBeAlrightWhenGetCurrentProgresses() throws Exception {
        List<EventInfo> infoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            EventBean bean = new EventBean();
            bean.setName("test");
            bean.setTargetProgress(100);
            bean.setType(Event.BOOK);
            bean.setId(i);
            Progress progress = new Progress();
            progress.setEid(i);
            progress.setProgress(10);
            progress.setRecord(10);
            EventInfo eventInfo = new EventInfo();
            eventInfo.setProgress(progress);
            eventInfo.setEvent(bean);
            infoList.add(eventInfo);
        }

        when(service.getCurrentEvents()).thenReturn(infoList);
        JSONObject map = helper.postDate(GET_CURRENT_EVENTS, null);
        helper.checkSucceed(map);
        verify(service).getCurrentEvents();
        EventInfo[] infos = map.getJSONArray("events")
                .toJavaObject(EventInfo[].class);
        assertNotNull(infos);
        List<EventInfo> infos1 = Arrays.asList(infos);
        assertEquals(10, infos1.size());
    }

}