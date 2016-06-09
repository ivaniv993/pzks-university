package com.luxoft.mpp.entity.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xXx on 6/4/2016.
 */
public class Processor {

    public Processor(){}

    public Processor(Processor p ){

        if (p == null) {
            return;
        }
        this.ID = p.ID;
        this.procPassed = p.procPassed;
    }

    private int ID;

    private boolean procPassed;

    private LinkedList<TimeUnit> transferLine = new LinkedList<TimeUnit>();

    public int searchBeginForTransfer(int transferDuration, int currentTime){

        if ( transferLine.isEmpty() )
            return currentTime;

        int finishTransfer = currentTime+transferDuration;
        for ( TimeUnit timeUnit : transferLine ){
            if ( ! timeUnit.isIdleTime() ){

                if ( currentTime <= timeUnit.getFrom() && finishTransfer <= timeUnit.getFrom() ||
                     currentTime >= timeUnit.getTo()  && finishTransfer >= timeUnit.getTo()
                        ){

                    return timeUnit.getTo();
                }

            }
        }

        // return end of last transfer
        return transferLine.getLast().getTo();

    }

    public void addTransferUnit(TimeUnit timeUnit ){
        transferLine.addLast(timeUnit);
    }

    public LinkedList<TimeUnit> getTransferLine() {
        return transferLine;
    }

    public void setTransferLine(LinkedList<TimeUnit> transferLine) {
        this.transferLine = transferLine;
    }

    public boolean isProcPassed() {
        return procPassed;
    }

    public void setProcPassed(boolean procPassed) {
        this.procPassed = procPassed;
    }

    private List<Task> tasks = new ArrayList<Task>();

    public List<TimeUnit> getWorkingLine() {
        return workingLine;
    }

    public void setWorkingLine(List<TimeUnit> workingLine) {
        this.workingLine = workingLine;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    private List<TimeUnit> workingLine = new ArrayList<TimeUnit>();

    private List<ProcLink> links = new ArrayList<ProcLink>();

    public Processor(int ID) {
        this.ID = ID;
    }

    public Processor(int ID, List<ProcLink> links) {

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

    public List<ProcLink> getLinks() {

        return links;
    }

    public void setLinks(List<ProcLink> links) {
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

    public int getCurrentWorkingTime(){
        int result = 0;
        for ( TimeUnit t : workingLine){
            if( result < t.getTo() )
                result = t.getTo();
        }
        return result;
    }

    @Deprecated
    public void addTimeLine(Task task, boolean idleTime){

        int from = getCurrentWorkingTime();
        TimeUnit timeUnit = new TimeUnit(task.getID(), from, from + task.getTimeDuration(), task.getTimeDuration(), idleTime);
        workingLine.add(timeUnit);
        tasks.add(task);

    }

    public void addToWorkingLine( Task task, int from, boolean idleTime ){

        TimeUnit timeUnit = new TimeUnit(task.getID(), from, from + task.getTimeDuration(), task.getTimeDuration(), idleTime);
        workingLine.add(timeUnit);
        tasks.add(task);

    }


    public int getTimeOfFinishTask( Task task ){

        if (! tasks.contains(task)) throw new IllegalStateException("Task not on processor");

        for ( TimeUnit taskOnProc : workingLine ){
            if (taskOnProc.getId() == task.getID())
                return taskOnProc.getTo();
        }
        throw new IllegalStateException("cant find task");

    }
}
