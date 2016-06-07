package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/7/2016.
 */
public class IdleTime extends Task {

    public int from, to, time;

    public IdleTime(int time){
        this.time = time;
    }

    @Override
    public boolean isIdleTime(){
        return true;
    }

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public void setFrom(int from) {
        this.from = from;
    }

    @Override
    public int getTo() {
        return to;
    }

    @Override
    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    public IdleTime(int from, int to) {
        super(to, from);
        this.from = from;
        this.to = to;

    }
}
