package com.luxoft.mpp.entity.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by iivaniv on 22.04.2016.
 */

public class TaskElement implements Serializable {

    private int id;

    private int taskDuration;

    private List<TaskElement> relatedTaskElements;

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

    public List<TaskElement> getRelatedTaskElements() {
        return relatedTaskElements;
    }

    public void setRelatedTaskElements(List<TaskElement> relatedTaskElements) {
        this.relatedTaskElements = relatedTaskElements;
    }

}
