package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/7/2016.
 */
public class TimeUnit extends Task {

    private int id;

    private int time, from, to;

    private boolean idleTime;

    private String message;

    public boolean isIdleTime() {
        return idleTime;
    }

    public void setIdleTime(boolean idleTime) {
        this.idleTime = idleTime;
    }

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

    public TimeUnit( TimeUnit timeUnit ){
        if (timeUnit == null) {
            return;
        }
        this.time = timeUnit.time;
        this.to = timeUnit.to;
        this.from = timeUnit.from;
        this.idleTime = timeUnit.idleTime;
        this.message = timeUnit.message;
    }

    public TimeUnit(int from, int to, int time, boolean idleTime ) {
        this.to = to;
        this.from = from;
        this.time = time;
        this.idleTime = idleTime;
    }

    public TimeUnit(int id, int from, int to, int time, boolean idleTime ) {
        this.id = id;
        this.to = to;
        this.from = from;
        this.time = time;
        this.idleTime = idleTime;
    }


    public TimeUnit() {

    }

    public TimeUnit(int time) {

        this.time = time;
    }

    @Override
    public String toString() {
        return "TimeUnit{" +
                ", from=" + from +
                ", to=" + to +
                ", idleTime=" + idleTime +
                '}';
    }

    public int getTime() {

        return time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTimeDuration(){
        return time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
