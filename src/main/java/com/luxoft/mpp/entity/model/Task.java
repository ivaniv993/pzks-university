package com.luxoft.mpp.entity.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xXx on 6/4/2016.
 */
public class Task{

    private List<TaskLink> inLink = new ArrayList<TaskLink>();

    private List<TaskLink> outLink = new ArrayList<TaskLink>();

    private int timeDuration;

    private int ID;

    private Processor onProcessor;

    public Task(int timeDuration) {
        this.timeDuration = timeDuration;
    }

    public Task(List<TaskLink> inLink, List<TaskLink> outLink, int timeDuration, int ID) {
        this.inLink = inLink;
        this.outLink = outLink;
        this.timeDuration = timeDuration;
        this.ID = ID;
    }

    public Task(int timeDuration, int ID) {
        this.timeDuration = timeDuration;

        this.ID = ID;
    }

    public Task(){}

    @Override
    public String toString() {
        return "Task{" +
                "timeDuration=" + timeDuration +
                ", ID=" + ID +
                '}';
    }

    public boolean isOnProcessor() {
        return onProcessor != null;
    }

    public void setOnProcessor(Processor processor) {
        this.onProcessor = processor;
    }

    public Processor getOnProcessor() {
        return this.onProcessor;
    }

    public List<TaskLink> getInLink() {
        return inLink;
    }

    public void setInLink(List<TaskLink> inLink) {
        this.inLink = inLink;
    }

    public List<TaskLink> getOutLink() {
        return outLink;
    }

    public void setOutLink(List<TaskLink> outLink) {
        this.outLink = outLink;
    }

    public int getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(int timeDuration) {
        this.timeDuration = timeDuration;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


}
