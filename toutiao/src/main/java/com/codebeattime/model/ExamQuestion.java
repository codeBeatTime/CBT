package com.codebeattime.model;

/**
 * Created by Administrator on 2016/8/21.
 */
public class ExamQuestion {
    private  int id;
    private int eId;
    private int qId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int geteId() {
        return eId;
    }

    public void seteId(int eId) {
        this.eId = eId;
    }

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }
}
