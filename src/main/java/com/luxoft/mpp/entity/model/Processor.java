package com.luxoft.mpp.entity.model;

import com.luxoft.mpp.controllers.TimeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xXx on 6/4/2016.
 */
public class Processor {

    public Processor(){}

    private int ID;

    private List<Task> tasks = new ArrayList<Task>();

    public List<TimeUnit> getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(List<TimeUnit> timeLine) {
        this.timeLine = timeLine;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    private List<TimeUnit> timeLine = new ArrayList<TimeUnit>();

    private List<ProcessorLink> links = new ArrayList<ProcessorLink>();

    public Processor(int ID) {
        this.ID = ID;
    }

    public Processor(int ID, List<ProcessorLink> links) {

        this.ID = ID;
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Processor)) return false;

        Processor processor = (Processor) o;

        if (ID != processor.ID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    public List<ProcessorLink> getLinks() {

        return links;
    }

    public void setLinks(List<ProcessorLink> links) {
        this.links = links;
    }

    public int getID() {

        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Processor{" +
                "ID=" + ID + '}';
    }

    // TODO remake with time unit
    public int getAllTime(){
        int result = 0;
        for ( Task t : tasks){
            result += t.getTimeDuration();
        }
        return result;
    }
}
