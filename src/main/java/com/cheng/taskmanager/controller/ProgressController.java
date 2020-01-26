package com.cheng.taskmanager.controller;

import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.bean.GetProgressBean;
import com.cheng.taskmanager.bean.ResultBean;
import com.cheng.taskmanager.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/progress", method = RequestMethod.POST)
public class ProgressController {

    private final static String RESULT = "result";
    private final static String PROGRESSES = "progresses";
    private final static String SUM = "sum";

    private ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    private void addResultSuccess(Map<String, Object> map) {
        map.put(RESULT, ResultBean.succeed);
    }

    private void addResultParamError(Map<String, Object> map) {
        map.put(RESULT, ResultBean.paramError);
    }

    @RequestMapping("/getProgresses")
    public Map<String, Object> addEvent(@RequestBody @Valid GetProgressBean bean,
                                        BindingResult bindingResult) {
        Map<String, Object> map = new HashMap<>();
        if (bindingResult.hasErrors()) {
            addResultParamError(map);
        } else {
            List<EventInfo> progressList =
                    progressService.getProgresses(
                            bean.getStartDate(),
                            bean.getEndDate(),
                            bean.getType());
            addResultSuccess(map);
            map.put(PROGRESSES, progressList);
            map.put(SUM, progressService.getSumRecord(progressList));
        }
        return map;
    }
}
