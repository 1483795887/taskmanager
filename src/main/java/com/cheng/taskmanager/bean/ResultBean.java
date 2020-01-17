package com.cheng.taskmanager.bean;

public class ResultBean {
    public final static int SUCCESS = 0;
    public final static int FAILED = 1;

    public final static ResultBean paramError = new ResultBean(FAILED, "paramError");
    public final static ResultBean succeed = new ResultBean(SUCCESS, "success");

    private int code;
    private String msg;

    public ResultBean() {
    }

    public ResultBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
