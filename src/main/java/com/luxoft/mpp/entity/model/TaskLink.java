package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/4/2016.
 */
public class TaskLink {
    @Override
    public String toString() {
        return "TaskLink{" +
                "from=" + from +
                ", to=" + to +
                ", transferTime=" + transferTime +
                '}';
    }

    private Task from;

    private Task to;

    private int transferTime;

    public TaskLink( TaskLink taskLink ){
        if (taskLink == null) {
            return;
        }
        this.from = taskLink.from;
        this.to = this.to;
        this.transferTime = taskLink.transferTime;
    }

    public TaskLink(Task from, Task to, int transferTime) {
        this.from = from;
        this.to = to;
        this.transferTime = transferTime;
    }

    public TaskLink(int transferTime) {

        this.transferTime = transferTime;
    }

    public Task getFrom() {

        return from;
    }

    public void setFrom(Task from) {
        this.from = from;
    }

    public Task getTo() {
        return to;
    }

    public void setTo(Task to) {
        this.to = to;
    }

    public int getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(int transferTime) {
        this.transferTime = transferTime;
    }
}
