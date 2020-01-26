package com.cheng.taskmanager.controller;

import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.bean.ResultMap;
import com.cheng.taskmanager.service.AchievementService;
import com.cheng.taskmanager.utils.EventInfoSummer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/achievement", method = RequestMethod.POST)
public class AchievementController {
    private final static String INFOS = "infos";
    private final static String SUM = "sum";

    private AchievementService service;

    @Autowired
    public AchievementController(AchievementService service) {
        this.service = service;
    }

    @RequestMapping("/getAchievements")
    public ResultMap
    getAchievements(@RequestBody @Valid DateAndTypeBean bean,
                    BindingResult bindingResult) {
        ResultMap map = new ResultMap();
        if (bindingResult.hasErrors()) {
            map.addResultParamError();
        } else {
            map.addResultSuccess();
            List<EventInfo> eventInfoList =
                    service.getAchievements(bean.getStartDate(), bean.getEndDate(), bean.getType());
            map.put(INFOS, eventInfoList);
            map.put(SUM, EventInfoSummer.getSumRecord(eventInfoList));
        }

        return map;
    }
}
