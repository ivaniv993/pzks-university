package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/7/2016.
 */
public class TimeUnit {

    private boolean idleTime;

    private int time, from, to;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public TimeUnit(int to, int from) {

        this.to = to;
        this.from = from;
    }

    public TimeUnit() {

    }

    public TimeUnit(int time) {

        this.time = time;
    }

    public int getTime() {

        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeDuration(){
        return time;
    }

    public boolean isIdleTime(){
        return idleTime;
    }

}
