package com.luxoft.mpp.entity.model;

import java.io.Serializable;

/**
 * Created by iivaniv on 22.04.2016.
 */

public class TaskElement implements Serializable {

    private int id;

    private int taskDuration;

    private int earlyStart;
    private int earlyFinish;
    private int lateStart;
    private int lateFinish;

    public TaskElement() {
    }

    public TaskElement(int id, int duratuion) {
        this.id = id;
        this.taskDuration = duratuion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }


    public int getEarlyStart() {
        return earlyStart;
    }

    public void setEarlyStart(int earlyStart) {
        this.earlyStart = earlyStart;
    }

    public int getEarlyFinish() {
        return earlyFinish;
    }

    public void setEarlyFinish(int earlyFinish) {
        this.earlyFinish = earlyFinish;
    }

    public int getLateStart() {
        return lateStart;
    }

    public void setLateStart(int lateStart) {
        this.lateStart = lateStart;
    }

    public int getLateFinish() {
        return lateFinish;
    }

    public void setLateFinish(int lateFinish) {
        this.lateFinish = lateFinish;
    }


}
