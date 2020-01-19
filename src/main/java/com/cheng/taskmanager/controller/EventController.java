package com.cheng.taskmanager.controller;

import com.cheng.taskmanager.bean.EventBean;
import com.cheng.taskmanager.bean.EventUpdateBean;
import com.cheng.taskmanager.bean.ProgressUpdateBean;
import com.cheng.taskmanager.bean.ResultBean;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/event", method = RequestMethod.POST)
public class EventController {

    private EventService eventService;
    private final static String RESULT = "result";
    private final static String EVENT = "event";

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    private void addResultSuccess(Map<String, Object> map) {
        map.put(RESULT, ResultBean.succeed);
    }

    private void addResultParamError(Map<String, Object> map) {
        map.put(RESULT, ResultBean.paramError);
    }

    private void addResultEventNotExist(Map<String, Object> map) {
        ResultBean bean = new ResultBean(ResultBean.FAILED, "event not exist");
        map.put(RESULT, bean);
    }

    @RequestMapping("/addEvent")
    public Map<String, Object> addEvent(@RequestBody @Valid EventBean bean,
                                        BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            addResultParamError(map);
        } else {
            Event event = new Event();
            event.setRunning(true);
            event.setName(bean.getName());
            event.setTargetProgress(bean.getTargetProgress());
            event.setType(bean.getType());
            eventService.addEvent(event);
            addResultSuccess(map);
        }
        return map;
    }

    @RequestMapping("/getEvent")
    public Map<String, Object> getEventById(@RequestBody @NotNull Integer id,
                                            BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            addResultParamError(map);
        } else {
            Event event = eventService.getEventById(id);
            if (event == null) {
                addResultEventNotExist(map);
                map.put(EVENT, null);
            } else {
                map.put(EVENT, event);
                addResultSuccess(map);
            }
        }
        return map;
    }

    @RequestMapping("/updateEvent")
    public Map<String, Object> updateEvent(@RequestBody @Valid EventUpdateBean event,
                                           BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            addResultParamError(map);
        } else {
            Event event1 = eventService.getEventById(event.getId());
            if (event1 == null) {
                addResultEventNotExist(map);
            } else {
                if (event.getName() != null)
                    event1.setName(event.getName());
                if (event.getTargetProgress() != null)
                    event1.setTargetProgress(event.getTargetProgress());
                eventService.updateEvent(event1);
                addResultSuccess(map);
            }

        }
        return map;
    }

    @RequestMapping("/updateProgress")
    public Map<String, Object> updateProgress(@RequestBody @Valid ProgressUpdateBean bean,
                                              BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            addResultParamError(map);
        } else {
            Event event = eventService.getEventById(bean.getId());
            if (event == null)
                addResultEventNotExist(map);
            else {
                addResultSuccess(map);
            }
        }

        return map;
    }
}
