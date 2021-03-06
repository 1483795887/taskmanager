package com.cheng.taskmanager.service;

import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.entity.Achievement;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.mapper.AchievementMapper;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.service.impl.EventServiceImpl;
import com.cheng.taskmanager.utils.DateFactory;
import com.cheng.taskmanager.utils.EventFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.cheng.taskmanager.utils.EventFactory.addProgress;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    private final static int TEST_EVENT_ID = 1;
    private final static int START_PROGRESS = 10;
    private final static int MIDDLE_PROGRESS = 15;
    private final static int TARGET_PROGRESS = 100;

    private EventMapper eventMapper;
    private AchievementMapper achievementMapper;
    private EventService service;
    private Event event;
    private ArgumentCaptor<Event> eventArgumentCaptor;
    private ArgumentCaptor<Progress> progressArgumentCaptor;
    private ArgumentCaptor<Achievement> achievementArgumentCaptor;
    private Date today;
    private Date startDate;

    @Before
    public void setUp() {
        eventMapper = mock(EventMapper.class);
        achievementMapper = mock(AchievementMapper.class);
        service = new EventServiceImpl(eventMapper, achievementMapper);
        event = EventFactory.getCurrentEvent(Event.BOOK);
        eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        progressArgumentCaptor = ArgumentCaptor.forClass(Progress.class);
        achievementArgumentCaptor = ArgumentCaptor.forClass(Achievement.class);

        today = DateFactory.getToday();
        startDate = DateFactory.getDateFromString("2020-01-06");

        event.setStartDate(startDate);
        event.setId(TEST_EVENT_ID);
        event.setTargetProgress(TARGET_PROGRESS);

        List<Progress> progresses = new ArrayList<>();
        event.setProgressList(progresses);

        addProgress(event, START_PROGRESS, START_PROGRESS, startDate);
        addProgress(event, MIDDLE_PROGRESS, MIDDLE_PROGRESS - START_PROGRESS, today);

        when(eventMapper.getEventById(TEST_EVENT_ID)).thenReturn(event);
        List<Event> events = new ArrayList<>();
        events.add(event);

        when(eventMapper.getCurrentEvents()).thenReturn(events);
    }

    @Test
    public void shouldCallAddEventWhenAddEvent() {
        service.addEvent(event);

        verify(eventMapper).addEvent(eventArgumentCaptor.capture());
        assertEquals(event.getName(), eventArgumentCaptor.getValue().getName());
        assertNull(eventArgumentCaptor.getValue().getStartDate());
        assertEquals(event.getType(), eventArgumentCaptor.getValue().getType());
    }

    @Test
    public void shouldMakeInitProgressWhenAddEvent() {
        service.addEvent(event);
        verify(eventMapper).addProgress(progressArgumentCaptor.capture());
        Progress progress = progressArgumentCaptor.getValue();
        assertEquals(today.toString(), progress.getDate().toString());
        assertEquals(0, progress.getProgress());
        assertEquals(0, progress.getRecord());
    }

    @Test
    public void shouldCallUpdateWhenUpdateEvent() {
        String newName = "newName";
        int targetProgress = 20;
        event.setName(newName);
        event.setStartDate(today);
        event.setTargetProgress(targetProgress);
        service.updateEvent(event);
        verify(eventMapper).update(eventArgumentCaptor.capture());

        assertEquals(newName, eventArgumentCaptor.getValue().getName());
        assertEquals(today.toString(), eventArgumentCaptor.getValue().getStartDate().toString());
        assertEquals(targetProgress, eventArgumentCaptor.getValue().getTargetProgress());
    }

    @Test
    public void shouldArgBeRightWhenUpdateChangedButNotFinished() {
        Progress newProgress = new Progress();
        int np = 20;
        newProgress.setProgress(np);
        service.updateProgress(TEST_EVENT_ID, np);
        verify(eventMapper).addProgress(progressArgumentCaptor.capture());

        Progress progress1 = progressArgumentCaptor.getValue();
        assertEquals(np, progress1.getProgress());
        assertEquals(np - MIDDLE_PROGRESS, progress1.getRecord());
    }

    @Test
    public void shouldNotDealWhenEventNotExist() {
        int np = 20;
        event.setRunning(false);
        when(eventMapper.getEventById(TEST_EVENT_ID)).thenReturn(null);
        service.updateProgress(TEST_EVENT_ID, np);
    }

    @Test
    public void shouldNotAddProgressWhenEventIsFinished() {
        int np = 20;
        event.setRunning(false);
        doThrow(new RuntimeException()).when(eventMapper).addProgress(any(Progress.class));
        service.updateProgress(TEST_EVENT_ID, np);
    }

    @Test
    public void shouldCallFinishWhenUpdateExactlyFinished() {
        service.updateProgress(TEST_EVENT_ID, TARGET_PROGRESS);
        verify(eventMapper).finish(TEST_EVENT_ID);
        verify(achievementMapper).addAchievement(achievementArgumentCaptor.capture());

        Achievement achievement = achievementArgumentCaptor.getValue();
        assertEquals(achievement.getDate().toString(), today.toString());
        assertEquals(achievement.getEid(), TEST_EVENT_ID);
    }

    @Test
    public void shouldCallFinishWhenUpdateMoreThanFinished() {
        service.updateProgress(TEST_EVENT_ID, TARGET_PROGRESS + 1);
        verify(eventMapper).finish(TEST_EVENT_ID);
        verify(eventMapper).addProgress(progressArgumentCaptor.capture());
        verify(achievementMapper).addAchievement(achievementArgumentCaptor.capture());

        Progress progress = progressArgumentCaptor.getValue();
        assertEquals(TARGET_PROGRESS, progress.getProgress());
        assertEquals(TARGET_PROGRESS - MIDDLE_PROGRESS, progress.getRecord());

        Achievement achievement = achievementArgumentCaptor.getValue();

        assertEquals(achievement.getDate().toString(), today.toString());
        assertEquals(achievement.getEid(), TEST_EVENT_ID);
    }

    @Test
    public void shouldRecordRightWhenAddTwoProgressOneDay() {
        int p = 16;
        int np = 20;
        service.updateProgress(TEST_EVENT_ID, p);
        addProgress(event, p, p - START_PROGRESS, today);
        service.updateProgress(TEST_EVENT_ID, np);
        verify(eventMapper, times(2)).addProgress(progressArgumentCaptor.capture());
        Progress progress = progressArgumentCaptor.getValue();
        assertEquals(progress.getProgress(), np);
        assertEquals(progress.getRecord(), np - p);
    }

    @Test
    public void shouldNotCallFinishWhenUpdateLessThanFinished() {
        doThrow(new RuntimeException()).when(eventMapper).finish(TEST_EVENT_ID);
        doThrow(new RuntimeException()).when(achievementMapper).addAchievement(any(Achievement.class));
        service.updateProgress(TEST_EVENT_ID, TARGET_PROGRESS - 1);
    }

    @Test
    public void shouldCallGetEventWhenGetExistId() {
        Event event = service.getEventById(TEST_EVENT_ID);
        verify(eventMapper).getEventById(TEST_EVENT_ID);
        assertNotNull(event);
    }

    @Test
    public void shouldBeNullWhenGetNotExistId() {
        Event event = service.getEventById(TEST_EVENT_ID + 1);
        verify(eventMapper).getEventById(TEST_EVENT_ID + 1);
        assertNull(event);
    }

    @Test
    public void shouldCallGetCurrentEventsWhenGetCurrentEvents() {
        List<EventInfo> eventInfos = service.getCurrentEvents();
        verify(eventMapper).getCurrentEvents();
        assertNotNull(eventInfos);
    }

    @Test
    public void shouldInfoBeRightWhenGetEvents() {
        List<EventInfo> eventInfos = service.getCurrentEvents();
        EventInfo info = eventInfos.get(0);
        EventBean bean = info.getEvent();
        Progress progress = info.getProgress();
        Event event = service.getEventById(TEST_EVENT_ID);
        Progress progress1 = event.getProgressList().get(0);
        assertEquals(progress.getRecord(), progress1.getRecord());
        assertEquals(progress.getProgress(), progress1.getProgress());
        assertEquals(bean.getName(), event.getName());
        assertEquals(bean.getStartDate().toString(), event.getStartDate().toString());
        assertEquals(bean.getTargetProgress(), event.getTargetProgress());
    }

    @Test
    public void shouldBeOrderedByLastDateWhenGetEvents() {
        Event event1 = EventFactory.getCurrentEvent(Event.BOOK);
        event1.setProgressList(new ArrayList<>());
        event1.setId(TEST_EVENT_ID);
        addProgress(event1, 1, 1, startDate);
        Event event2 = EventFactory.getCurrentEvent(Event.BOOK);
        event2.setId(TEST_EVENT_ID + 1);
        event2.setProgressList(new ArrayList<>());
        addProgress(event2, 1, 1, today);
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);

        when(eventMapper.getCurrentEvents()).thenReturn(eventList);

        List<EventInfo> infos = service.getCurrentEvents();
        EventInfo eventInfo = infos.get(0);
        assertEquals(eventInfo.getProgress().getDate().toString(), today.toString());
    }
}
