package com.cheng.taskmanager.service;

import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.entity.EventFactory;
import com.cheng.taskmanager.entity.Progress;
import com.cheng.taskmanager.entity.ReadRecord;
import com.cheng.taskmanager.mapper.EventMapper;
import com.cheng.taskmanager.mapper.ReadRecordMapper;
import com.cheng.taskmanager.service.impl.EventServiceImpl;
import com.cheng.taskmanager.utils.DateFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    private final static int TEST_EVENT_ID = 1;
    private final static int START_PROGRESS = 10;
    private final static int TARGET_PROGRESS = 100;

    private EventMapper eventMapper;
    private ReadRecordMapper readRecordMapper;
    private EventService service;
    private Event event;
    private ArgumentCaptor<Event> eventArgumentCaptor;
    private ArgumentCaptor<Progress> progressArgumentCaptor;
    private ArgumentCaptor<ReadRecord> readRecordArgumentCaptor;
    private Date today;
    private Date startDate;

    @Before
    public void setUp() {
        eventMapper = mock(EventMapper.class);
        readRecordMapper = mock(ReadRecordMapper.class);
        service = new EventServiceImpl(eventMapper, readRecordMapper);
        event = EventFactory.getCurrentEvent(Event.BOOK);
        eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        progressArgumentCaptor = ArgumentCaptor.forClass(Progress.class);
        readRecordArgumentCaptor = ArgumentCaptor.forClass(ReadRecord.class);

        java.util.Date date = new java.util.Date();
        today = new Date(date.getTime());
        startDate = DateFactory.getDateFromString("2020-01-06");

        event.setStartDate(startDate);
        event.setId(TEST_EVENT_ID);
        event.setTargetProgress(TARGET_PROGRESS);
        Progress progress = new Progress();
        progress.setDate(startDate);
        progress.setProgress(START_PROGRESS);
        progress.setEid(TEST_EVENT_ID);
        List<Progress> progresses = new ArrayList<>();
        progresses.add(progress);
        event.setProgressList(progresses);

        when(eventMapper.getEventById(TEST_EVENT_ID)).thenReturn(event);
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
        assertEquals(today.toString(), progressArgumentCaptor.getValue().getDate().toString());
        assertEquals(0, progressArgumentCaptor.getValue().getProgress());
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
    }

    @Test
    public void shouldNotDealWhenEventNotExist() {
        int np = 20;
        event.setFinished(true);
        when(eventMapper.getEventById(TEST_EVENT_ID)).thenReturn(null);
        service.updateProgress(TEST_EVENT_ID, np);
    }

    @Test
    public void shouldNotAddProgressWhenEventIsFinished() {
        int np = 20;
        event.setFinished(true);
        doThrow(new RuntimeException()).when(eventMapper).addProgress(any(Progress.class));
        service.updateProgress(TEST_EVENT_ID, np);
    }

    @Test
    public void shouldNotAddProgressWhenEventIsClosed() {
        int np = 20;
        event.setClosed(true);
        doThrow(new RuntimeException()).when(eventMapper).addProgress(any(Progress.class));
        service.updateProgress(TEST_EVENT_ID, np);
    }

    @Test
    public void shouldCallFinishWhenUpdateExactlyFinished() {
        service.updateProgress(TEST_EVENT_ID, TARGET_PROGRESS);
        verify(eventMapper).finish(TEST_EVENT_ID);
    }

    @Test
    public void shouldCallFinishWhenUpdateMoreThanFinished() {
        service.updateProgress(TEST_EVENT_ID, TARGET_PROGRESS + 1);
        verify(eventMapper).finish(TEST_EVENT_ID);
        verify(eventMapper).addProgress(progressArgumentCaptor.capture());
        Progress progress = progressArgumentCaptor.getValue();
        assertEquals(TARGET_PROGRESS, progress.getProgress());
    }

    @Test
    public void shouldNotCallFinishWhenUpdateLessThanFinished() {
        doThrow(new RuntimeException()).when(eventMapper).finish(TEST_EVENT_ID);
        service.updateProgress(TEST_EVENT_ID, TARGET_PROGRESS - 1);
    }

    @Test
    public void shouldNotAddReadRecordWhenUpdateBookNotChange() {
        event.getProgressList().get(0).setDate(today);

        doThrow(new RuntimeException()).when(readRecordMapper).addReadRecord(any(ReadRecord.class));

        service.updateProgress(TEST_EVENT_ID, START_PROGRESS);
    }

    @Test
    public void shouldAddReadRecordWhenUpdateBook() {
        int np = 20;
        service.updateProgress(TEST_EVENT_ID, np);

        verify(readRecordMapper).addReadRecord(readRecordArgumentCaptor.capture());

        ReadRecord readRecord = readRecordArgumentCaptor.getValue();
        assertEquals(today.toString(), readRecord.getDate().toString());
        assertEquals(np - START_PROGRESS, readRecord.getRecord());
    }

    @Test
    public void shouldNotOverTheTargetWhenUpdateBookWhenOverFinish() {
        int np = TARGET_PROGRESS + 1;
        service.updateProgress(TEST_EVENT_ID, np);

        verify(readRecordMapper).addReadRecord(readRecordArgumentCaptor.capture());

        ReadRecord readRecord = readRecordArgumentCaptor.getValue();
        assertEquals(today.toString(), readRecord.getDate().toString());
        assertEquals(TARGET_PROGRESS - START_PROGRESS, readRecord.getRecord());

    }

    @Test
    public void shouldNotAddReadRecordWhenUpdateNotBook() {
        event.setType(Event.TASK);
        doThrow(new RuntimeException()).when(readRecordMapper).addReadRecord(any(ReadRecord.class));

        int np = 20;
        service.updateProgress(TEST_EVENT_ID, np);
    }
}
