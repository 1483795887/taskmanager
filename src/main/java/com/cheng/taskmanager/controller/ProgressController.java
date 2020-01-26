package com.cheng.taskmanager.controller;

import com.cheng.taskmanager.bean.DateAndTypeBean;
import com.cheng.taskmanager.bean.EventInfo;
import com.cheng.taskmanager.bean.ResultMap;
import com.cheng.taskmanager.service.ProgressService;
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
@RequestMapping(value = "/progress", method = RequestMethod.POST)
public class ProgressController {

    private final static String RESULT = "result";
    private final static String INFOS = "infos";
    private final static String SUM = "sum";

    private ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @RequestMapping("/getProgresses")
    public ResultMap addEvent(@RequestBody @Valid DateAndTypeBean bean,
                              BindingResult bindingResult) {
        ResultMap map = new ResultMap();
        if (bindingResult.hasErrors()) {
            map.addResultParamError();
        } else {
            List<EventInfo> progressList =
                    progressService.getProgresses(
                            bean.getStartDate(),
                            bean.getEndDate(),
                            bean.getType());
            map.addResultSuccess();
            map.put(INFOS, progressList);
            map.put(SUM, EventInfoSummer.getSumRecord(progressList));
        }
        return map;
    }
}
