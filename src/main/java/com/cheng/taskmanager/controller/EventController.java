package com.cheng.taskmanager.controller;

import com.cheng.taskmanager.bean.EventBean;
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

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping("/addEvent")
    public Map<String, Object> addEvent(@RequestBody @Valid EventBean bean,
                                        BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            map.put("result", ResultBean.paramError);
        } else {
            Event event = new Event();
            event.setRunning(true);
            event.setName(bean.getName());
            event.setTargetProgress(bean.getTargetProgress());
            event.setType(bean.getType());
            eventService.addEvent(event);
            map.put("result", ResultBean.succeed);
        }
        return map;
    }

    @RequestMapping("/getEvent")
    public Map<String, Object> getEventById(@RequestBody @NotNull Integer id,
                                            BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            map.put("result", ResultBean.paramError);
        } else {
            Event event = eventService.getEventById(id);
            if (event == null) {
                ResultBean resultBean = new ResultBean(
                        ResultBean.FAILED, "event not exist");
                map.put("result", resultBean);
                map.put("event", null);
            } else {
                map.put("result", ResultBean.succeed);
                map.put("event", event);
            }

        }
        return map;
    }
}
