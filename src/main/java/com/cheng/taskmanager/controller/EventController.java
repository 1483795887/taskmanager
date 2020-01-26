package com.cheng.taskmanager.controller;

import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventUpdateBean;
import com.cheng.taskmanager.bean.ResultMap;
import com.cheng.taskmanager.entity.Event;
import com.cheng.taskmanager.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/event", method = RequestMethod.POST)
public class EventController {

    private final static String RESULT = "result";
    private final static String EVENT = "event";
    private final static String EVENTS = "events";
    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping("/addEvent")
    public ResultMap addEvent(@RequestBody @Valid EventBean bean,
                              BindingResult bindingResult) {
        ResultMap map = new ResultMap();
        if (bindingResult.hasErrors()) {
            map.addResultParamError();
        } else {
            Event event = new Event();
            event.setRunning(true);
            event.setName(bean.getName());
            event.setTargetProgress(bean.getTargetProgress());
            event.setType(bean.getType());
            eventService.addEvent(event);
            map.addResultSuccess();
        }
        return map;
    }

    @RequestMapping("/getEvent")
    public ResultMap getEventById(@RequestBody @NotNull Integer id,
                                  BindingResult bindingResult) {
        ResultMap map = new ResultMap();
        if (bindingResult.hasErrors()) {
            map.addResultParamError();
        } else {
            Event event = eventService.getEventById(id);
            if (event == null) {
                map.addFailMsg("event not exist");
                map.put(EVENT, null);
            } else {
                map.put(EVENT, event);
                map.addResultSuccess();
            }
        }
        return map;
    }

    @RequestMapping("/updateEvent")
    public ResultMap updateEvent(@RequestBody @Valid EventUpdateBean event,
                                 BindingResult bindingResult) {
        ResultMap map = new ResultMap();
        if (bindingResult.hasErrors()) {
            map.addResultParamError();
        } else {
            Event event1 = eventService.getEventById(event.getId());
            if (event1 == null ||
                    event.getTargetProgress() < event.getCurrentProgress()) {
                map.addFailMsg("event not exist");
            } else {
                event1.setName(event.getName());
                event1.setTargetProgress(event.getTargetProgress());
                eventService.updateEvent(event1);
                eventService.updateProgress(event.getId(), event.getCurrentProgress());
                map.addResultSuccess();
            }

        }
        return map;
    }

    @RequestMapping("/getCurrentEvents")
    public ResultMap getCurrentEvents() {
        ResultMap map = new ResultMap();
        map.addResultSuccess();
        map.put(EVENTS, eventService.getCurrentEvents());
        return map;
    }
}
