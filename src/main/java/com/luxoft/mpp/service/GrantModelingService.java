package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.*;
import edu.princeton.cs.algorithms.AdjMatrixEdgeWeightedDigraph;
import edu.princeton.cs.algorithms.DirectedEdge;
import edu.princeton.cs.algorithms.FloydWarshall;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xXx on 6/8/2016.
 */
@Service("grantModelingService")
public class GrantModelingService {


    public List<Processor> modeling(List<Processor> processors, List<Task> tasksGraph, int[][] matrixCS,  List<SimpleMetaData> queue ){

        for (SimpleMetaData elem : queue){

            Task task = getTaskByID(tasksGraph, elem.getVertexId());

            Random random = new Random();
            int randProcId = random.nextInt(matrixCS.length);
            System.out.println("random id : "+randProcId);
            Processor procCandidate = getProcessById(processors, randProcId);

            System.out.println("Current task : "+task.getID());
            if ( taskHasRelatives(task) ) {

                List<TaskLink> inTaskLinks = task.getInLink();
                int transferTime = 0;
                for (TaskLink inTaskLink : inTaskLinks) {

                    Task relativeTask = inTaskLink.getFrom();
                    Processor relativeProc = relativeTask.getOnProcessor();

                    Iterable<DirectedEdge> way = searchShooterWay(matrixCS, procCandidate.getID(), relativeProc.getID() );
                    int size = getWayLength(way);

//                    System.out.println("--Start--");
//                    way.toString();
//                    System.out.println("--End--");

                    TaskLink link = getLinkBetweenTasks(relativeTask, task);

                    if ( (relativeProc.getCurrentWorkingTime()+link.getTransferTime() * size) > transferTime  ) {
                        transferTime = link.getTransferTime() * size+relativeProc.getCurrentWorkingTime();
                    }
                }

                if (procCandidate.getCurrentWorkingTime() < transferTime){
                    TimeUnit idleTime = new TimeUnit(transferTime-procCandidate.getCurrentWorkingTime());
                    procCandidate.addTimeLine(idleTime, true);
                }
                procCandidate.addTimeLine(task, false);
                task.setOnProcessor(procCandidate);


            } else {

                task.setOnProcessor(procCandidate);
                procCandidate.addTimeLine(task, false);


            }
        }
        return processors;
    }



    public List<Processor> nearbyModeling(List<Processor> processors,
                                           List<Task> tasksGraph,
                                           List<SimpleMetaData> queue,
                                           int[][] matrixCS ){

        List<Processor> result = new ArrayList<Processor>();

        for (SimpleMetaData next : queue ){

            Task task = getTaskByID(tasksGraph, next.getVertexId());

            if (taskHasRelatives(task)){

                Map<Integer, List<Processor> > processorGraphMap = new HashMap<Integer, List<Processor>>();
                Map<Integer, List<Task> > tasksGraphMap = new HashMap<Integer, List<Task>>();

                System.out.println("|===================|");
                int maxTime = Integer.MAX_VALUE;
                for ( Processor procCandidate : processors) {

                    int timeOfFinishParentTask = 0;
                    List<Processor> buffProcessors = new ArrayList<Processor>();
                    List<Task> buffTasks = new ArrayList<Task>();
                    // make snapshot of state
                    makeSnapshot(processors, buffProcessors, tasksGraph, buffTasks);
                    //set current operand to snapshot
                    procCandidate = getProcessById(buffProcessors, procCandidate.getID());
                    task = getTaskByID(buffTasks, task.getID());

                    List<TaskLink> inTaskLinks = task.getInLink();
                    //-------------------------------------------------------------
                    String log = "Parent tasks on processors : ";
                    for (TaskLink inTaskLink : inTaskLinks)
                        log += "["+inTaskLink.getFrom().getOnProcessor().getID()+"]";
                    System.out.println(log);

                    System.out.println("Candidate proc : "+procCandidate.getID());
                    //--------------------------------------------------------------


                    for (TaskLink inTaskLink : inTaskLinks) {

                        Processor from = inTaskLink.getFrom().getOnProcessor();
                        int transferDuration = inTaskLink.getTransferTime();
                        Iterable<DirectedEdge> way = searchShooterWay(matrixCS, from.getID(), procCandidate.getID());

                        System.out.println(way.toString());

                        // get time when parent task finish (current time for current proc)
                        timeOfFinishParentTask = from.getTimeOfFinishTask( inTaskLink.getFrom() );
                        for (DirectedEdge edge : way ){
                            Processor proc = getProcessById(buffProcessors, edge.from());
                            int startOfTransfer = proc.searchBeginForTransfer(transferDuration, timeOfFinishParentTask);
                            int endOfTransfer = startOfTransfer + transferDuration;

                            System.out.println("Transfer on proc :"+proc.getID()+"; start :"+startOfTransfer+"; end :"+endOfTransfer);
                            TimeUnit transferUnit = new TimeUnit(startOfTransfer, endOfTransfer, transferDuration, false );
                            transferUnit.setMessage("{task :"+task.getID()+" [from :"+edge.from()+"][to :"+edge.to()+"]}");
                            proc.addTransferUnit(transferUnit);

                            timeOfFinishParentTask = endOfTransfer;
                        }

                    }
                    task.setOnProcessor(procCandidate);
                    if (procCandidate.getCurrentWorkingTime() > timeOfFinishParentTask )
                        timeOfFinishParentTask = procCandidate.getCurrentWorkingTime();
                    procCandidate.addToWorkingLine(task, timeOfFinishParentTask, false);
                    System.out.println("TASK with id : "+task.getID());

                    int maxComputingTime = getMaxWorkingTimeFromAllProc(buffProcessors);
                    processorGraphMap.put(maxComputingTime, buffProcessors);
                    tasksGraphMap.put(maxComputingTime, buffTasks);
                    System.out.println("------------- max time : "+maxComputingTime);
                    if (maxTime > maxComputingTime)
                        maxTime = maxComputingTime;
                }

                tasksGraph = tasksGraphMap.get(maxTime);
                processors = processorGraphMap.get(maxTime);


                System.out.println("|===================|");

            } else {

                List<Processor> emptyProcessors = getEmptyProcessors(processors);
                if ( ! emptyProcessors.isEmpty() ) {

                    //set on  empty processor
                    Processor proc = getProcessorByConnectivityAndFreedom(emptyProcessors);
                    System.out.println("EMPTY PROC WITH BIGGER CONNECTIVITY : "+proc.getID());
                    task.setOnProcessor(proc);
                    proc.addToWorkingLine(task, 0, false);
                    System.out.println("(EMPTY PROC WITH BIGGER CONNECTIVITY) TASK with id : "+task.getID());

                } else {
                    //set proccessor with smaller task
                    Processor proc = findAnyProcessorWithSmallerTasks(processors);
                    System.out.println("EMPTY PROC WITH SMALLER TASKS : "+proc.getID());
                    task.setOnProcessor(proc);
                    int from = proc.getCurrentWorkingTime();
                    proc.addToWorkingLine(task, from, false);
                    System.out.println("(EMPTY PROC WITH SMALLER TASKS) TASK with id : "+task.getID());

                }
            }
        }
        return processors;
    }

    private int getMaxWorkingTimeFromAllProc( List<Processor> processors ){
        int result = 0;
        for (Processor proc : processors ){
            if (proc.getCurrentWorkingTime() > result )
                result = proc.getCurrentWorkingTime();
        }
        return result;
    }

    private void makeSnapshot( List<Processor> procSource, List<Processor> procDestination,
                               List<Task> tasksSource, List<Task> tasksDestination){

        //initialization
        for ( Processor proc : procSource){
            procDestination.add(new Processor(proc));
        }

        for ( Task task : tasksSource){
            tasksDestination.add(new Task(task));
        }

        //init procSource
        for (int i = 0; i < procSource.size(); i++) {

            // TODO proclink ...

            // time line
            List< TimeUnit > newWorkingLine = new ArrayList<TimeUnit>();
            for ( TimeUnit timeUnit : procSource.get(i).getWorkingLine() ){
                newWorkingLine.add(new TimeUnit(timeUnit));
            }
            procDestination.get(i).setWorkingLine(newWorkingLine);

            //transfer line
            LinkedList< TimeUnit > newTransferLine = new LinkedList<TimeUnit>();
            for ( TimeUnit timeUnit : procSource.get(i).getTransferLine() ){
                newTransferLine.add( new TimeUnit(timeUnit) );
            }
            procDestination.get(i).setTransferLine(newTransferLine);

            // proc tasksSource
            List<Task> newTasks = new ArrayList<Task>();
            for ( Task task : procSource.get(i).getTasks() ){
                newTasks.add( new Task(task) );
            }
            procDestination.get(i).setTasks(newTasks);

        }

        // init tasksSource
        for (int i = 0; i < tasksSource.size(); i++) {

            if ( tasksSource.get(i).getOnProcessor() != null) {
                Processor onProcessor = getProcessById(procDestination, tasksSource.get(i).getOnProcessor().getID() );
                tasksDestination.get(i).setOnProcessor(onProcessor);

            }

            List<TaskLink> inputLinks = new ArrayList<TaskLink>();
            for (TaskLink taskLink : tasksSource.get(i).getInLink()) {
                inputLinks.add(new TaskLink(taskLink));
            }
            tasksDestination.get(i).setInLink(inputLinks);


            List<TaskLink> outputLink = new ArrayList<TaskLink>();
            for (TaskLink taskLink : tasksSource.get(i).getOutLink()) {
                outputLink.add(new TaskLink(taskLink));
            }
            tasksDestination.get(i).setOutLink(outputLink);

        }

    }


    private int getWayLength( Iterable<DirectedEdge> iterable){
        int l = 0;
        for (DirectedEdge edge : iterable)
            l ++;
        return l;
    }


    private TaskLink getLinkBetweenTasks( Task from, Task to ){

        for ( TaskLink taskLink : from.getOutLink() ){
            if (taskLink.getTo() == to){
                return taskLink;
            }
        }
        throw new IllegalStateException("Cant find link");
    }


    private Iterable<DirectedEdge> searchShooterWay(int [][] lm, int from, int to){

        AdjMatrixEdgeWeightedDigraph adjMatrix = new AdjMatrixEdgeWeightedDigraph(lm.length);
        for (int i = 0; i < lm.length; i++) {
            for (int j = 0; j < lm[i].length; j++) {
                if (lm[i][j] != 0 ) {
                    adjMatrix.addEdge(new DirectedEdge(i, j, 1));
                }
            }
        }

        FloydWarshall floyd = new FloydWarshall(adjMatrix);

        return floyd.path(from, to);
    }


    private Task getTaskByID( List<Task> tasks , int id){

        for ( Task t :  tasks ){
            if (t.getID() == id )
                return t;
        }
        throw new IllegalArgumentException("wrong id");

    }


    private List<Processor> getEmptyProcessors(List<Processor> processors){

        List<Processor> result = new ArrayList<Processor>();
        for (Processor p : processors ){
            if (p.getTasks().isEmpty())
                result.add(p);
        }
        return result;
    }


    private boolean taskHasRelatives(Task task){
        return task.getInLink().size() > 0;
    }


    private Processor findAnyProcessorWithSmallerTasks( List<Processor> allProcessors ){

        Processor result = allProcessors.get(0);
        for ( Processor  p : allProcessors ){
            if (p.getCurrentWorkingTime() < result.getCurrentWorkingTime())
                result = p;
        }
        return result;
    }



    private Processor getProcessorByConnectivityAndFreedom(List<Processor> processors){

        Processor result = new Processor();
        for ( Processor p : processors){
            if ( result.getLinks().size() < p.getLinks().size()) {
                result = p;
            }
        }
        return result;
    }

    private Processor getProcessById( List<Processor> processors, int id ) {

        for (Processor proc : processors) {
            if (proc.getID() == id)
                return proc;
        }
        throw new IllegalStateException("Wrong id");

    }

}
