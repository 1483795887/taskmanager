package com.cheng.taskmanager.bean;

import java.util.HashMap;

public class ResultMap extends HashMap<String, Object> {
    private final static String RESULT = "result";

    public void addResultSuccess() {
        put(RESULT, ResultBean.succeed);
    }

    public void addResultParamError() {
        put(RESULT, ResultBean.paramError);
    }
}
