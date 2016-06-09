package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.*;
import edu.princeton.cs.algorithms.AdjMatrixEdgeWeightedDigraph;
import edu.princeton.cs.algorithms.DirectedEdge;
import edu.princeton.cs.algorithms.FloydWarshall;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

                    if ( (relativeProc.getCurrentTime()+link.getTransferTime() * size) > transferTime  ) {
                        transferTime = link.getTransferTime() * size+relativeProc.getCurrentTime();
                    }
                }

                if (procCandidate.getCurrentTime() < transferTime){
                    TimeUnit idleTime = new TimeUnit(transferTime-procCandidate.getCurrentTime());
                    procCandidate.addTimeLine(idleTime, true);
                }
                procCandidate.addTimeLine(task, false);
                task.setOnProcessor(procCandidate);


            } else {

                task.setOnProcessor(procCandidate);
                procCandidate.addTimeLine(task, false);
                List<Processor> emptyProcessors = findEmptyProcessors(processors);
//                if ( ! emptyProcessors.isEmpty() ) {
//
//                    //set on  empty processor
////                    Processor proc = getProcessorByConnectivityAndFreedom(emptyProcessors);
////                    System.out.println("EMPTY PROC WITH BIGGER CONNECTIVITY : "+proc.getID());
//                    task.setOnProcessor(proc);
//                    proc.addTimeLine(task, false);
//
//                } else {
//                    //set proccessor with smaller task
////                    Processor proc = findAnyProcessorWithSmallerTasks(processors);
////                    System.out.println("EMPTY PROC WITH SMALLER TASKS : "+proc.getID());
//                    task.setOnProcessor(proc);
//                    proc.addTimeLine(task, false);
//
//                }

            }
        }
        return processors;
    }


    private boolean existEmptyProcessors(List<Processor> processors, int curTime){

        for (Processor proc : processors )
            if (proc.getCurrentTime() <= curTime )
                return true;
        return false;
    }

    private int getWayLength( Iterable<DirectedEdge> iterable){
        int l = 0;
        for (DirectedEdge edge : iterable)
            l ++;
        return l;
    }

    private Task getTaskOnProcessorRelativeWith(Task task, Processor processor){

        for (Task taskOnProc : processor.getTasks()){
            for (TaskLink outLink : taskOnProc.getOutLink() ){
                if (outLink.getTo() == task )
                    return taskOnProc;
            }
        }
        throw new IllegalStateException("Cant find task");
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


    private int getDurationOfTransition( List<ProcLink> way, int linkDuration){

        return way.size()*linkDuration;

    }


    private Task getTaskByID( List<Task> tasks , int id){

        for ( Task t :  tasks ){
            if (t.getID() == id )
                return t;
        }
        throw new IllegalArgumentException("wrong id");

    }

    private List<Processor> findProcessorsWithRelativeTask(Task task){

        List<Processor> result = new ArrayList<Processor>();
        List<TaskLink> inputTaskLinks = task.getInLink();

        for ( TaskLink  inputLink : inputTaskLinks ){

            Task fromTask = inputLink.getFrom();

            if ( ! fromTask.isOnProcessor() )
                throw new IllegalStateException("Task not on processor; task : "+fromTask.getID());
            Processor processor = fromTask.getOnProcessor();
            result.add(processor);
        }
        return result;
    }

    private TaskLink getLinkBetweenTaskAndTaskOnProcessor(Processor onProcessor, Task toTask){

        List<TaskLink> inputTaskLinks = toTask.getInLink();

        TaskLink result = null;

        for (TaskLink taskLink : inputTaskLinks ) {

            Task buff = taskLink.getFrom();

            if (! buff.isOnProcessor()) throw new IllegalStateException("Input task must bee on processor");

            if ( buff.getOnProcessor() == onProcessor ){
                result =  taskLink;
            }

        }

        if (result == null) {
            throw new IllegalStateException("task must bee on processor");
        }
        return result;

    }

    private Processor getProcessorWithSmallerTimeFromRelative(List<Processor> processors){

        Processor result = processors.get(0);
        for ( Processor proc : processors ){
            if (result.getCurrentTime() < proc.getCurrentTime())
                result = proc;
        }
        return result;

    }


    private List<Processor> findEmptyProcessors( List<Processor> processors ){

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
            if (p.getCurrentTime() < result.getCurrentTime())
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

    public List<Processor> createMockCS(){

        List< Processor > result = new ArrayList<Processor>();

        int permit = 2;

        Processor
                p0 = new Processor(0),
                p1 = new Processor(1),
                p2 = new Processor(2),
                p3 = new Processor(3),
                p4 = new Processor(4),
                p5 = new Processor(5);


        int[][] matrix =
                {{0,1,0,0,0,1},//0
                        {1,0,1,1,0,0},//1
                        {0,1,0,1,1,0},//2
                        {0,1,1,0,1,1},//3
                        {0,0,1,1,0,1},//4
                        {1,0,0,1,1,0},//5
                };
        ProcLink
                link01 = new ProcLink(0, 1, permit),
                link05 = new ProcLink(0, 5, permit),

                link12 = new ProcLink(1, 2, permit),
                link13 = new ProcLink(1, 3, permit),

                link23 = new ProcLink(2, 3, permit),
                link24 = new ProcLink(2, 4, permit),

                link34 = new ProcLink(3, 4, permit),
                link35 = new ProcLink(3, 5, permit),

                link45 = new ProcLink(4, 5, permit);

        Collections.addAll(p0.getLinks(), link01, link05);
        Collections.addAll(p1.getLinks(), link01, link12, link13);
        Collections.addAll(p2.getLinks(), link12, link23, link24);
        Collections.addAll(p3.getLinks(), link13, link23, link34, link35);
        Collections.addAll(p4.getLinks(), link24, link34, link45);
        Collections.addAll(p5.getLinks(), link35, link45, link05);

        Collections.addAll(result, p0, p1, p2, p3, p4, p5);

        return result;
    }

}
