package com.luxoft.mpp.controllers;

/**
 * Created by xXx on 6/4/2016.
 */
public class TimeUnit {

    private int time;

    private boolean idle;

    public TimeUnit() {
    }

    public TimeUnit(int time, boolean idle) {

        this.time = time;
        this.idle = idle;
    }

    public int getTime() {

        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setIdle(boolean idle) {
        this.idle = idle;
    }
}
