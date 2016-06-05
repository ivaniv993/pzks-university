package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.*;
import com.sun.javafx.sg.prism.NGShape;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xXx on 6/4/2016.
 */
@Service("modelingServiceImpl")
public class ModelingServiceImpl implements ModelingService{



    public List<Processor> createMockCS(){

        List< Processor > result = new ArrayList<Processor>();

        Processor
                p1 = new Processor(1),
                p2 = new Processor(2),
                p3 = new Processor(3),
                p4 = new Processor(4),
                p5 = new Processor(5);
        ProcessorLink
                link12 = new ProcessorLink(p1, p2, 1),
                link13 = new ProcessorLink(p1, p3, 2),
                link14 = new ProcessorLink(p1, p4, 3),
                link15 = new ProcessorLink(p1, p5, 4),

                link23 = new ProcessorLink(p2, p3, 5),

                link34 = new ProcessorLink(p3, p4, 6),

                link45 = new ProcessorLink(p4, p5, 7);

//        Collections.addAll(p1.getLinks(), p2, p3, p4, p5);
//        Collections.addAll(p2.getLinks(), p1, p3);
//        Collections.addAll(p3.getLinks(), p1, p2, p4);
//        Collections.addAll(p4.getLinks(), p1, p3, p5);
//        Collections.addAll(p5.getLinks(), p1, p5);

        Collections.addAll(result, p1, p2, p3, p4, p5);

        return result;
    }

    public List<Processor> sortByConnectivity( List<Processor> processors ){

        Collections.sort(processors, new Comparator<Processor>() {
            @Override
            public int compare(Processor o1, Processor o2) {
                if(o1.getLinks().size() > o2.getLinks().size()){
                    return -1;
                }else if (o1.getLinks().size() < o2.getLinks().size()){
                    return 1;
                }else return 0;
            }
        });

        return processors;
    }

    public List<Task> createTaskGraph(int[][] linkMatrix, Integer[] vertex){

        List<Task> result = new ArrayList<Task>();
        //generate tasks
        for (int i = 0; i < vertex.length; i++) {
            result.add(new Task(vertex[i], i));
        }

        for (int i = 0; i < vertex.length; i++) {

            Task task = getTaskByID(result, i);

            // input Links
            List<TaskLink> inLink = new ArrayList<TaskLink>();
            for (int j = 0; j < linkMatrix[i].length; j++) {
                if (linkMatrix[j][i] != 0){
                    TaskLink taskLink = new TaskLink( getTaskByID(result, j), getTaskByID(result, i), linkMatrix[j][i]);
                    inLink.add( taskLink );
                }
            }
            task.setInLink(inLink);

            // output Links
            List<TaskLink> outLink = new ArrayList<TaskLink>();
            for (int j = 0; j < linkMatrix.length; j++) {
                if (linkMatrix[i][j] != 0){
                    TaskLink taskLink = new TaskLink( getTaskByID(result, i), getTaskByID(result, j), linkMatrix[i][j]);
                    outLink.add( taskLink );
                }
            }
            task.setOutLink(outLink);

        }
        return result;

    }


    public void modeling(List<Processor> processors, List<Task> tasksGraph,  List<SimpleMetaData> queue ){

        int currentTime = 0;
        for (SimpleMetaData elem : queue){
            int id = elem.getVertexId();
            Task task = getTaskByID(tasksGraph, id);

            if ( hasInputLink(task) ){

                List<Processor> relativeProcessors = findProcessorsWithRelativeTask(task);
                Processor processor = getProcessorFromRelative(relativeProcessors);


            } else {

                List<Processor> emptyProcessors = findEmptyProcessors(processors);
                if ( ! emptyProcessors.isEmpty() ) {
                    Processor proc = getProcessorByConnectivityAndFreedom(emptyProcessors);
                    task.setOnProcessor(proc);
                    proc.getTasks().add(task);
                } else {
                    Processor proc = findAnyProcessorWithSmallerTasks(processors);
                    task.setOnProcessor(proc);
                    proc.getTasks().add(task);

                }

            }

        }
    }


    private Processor findAnyProcessorWithSmallerTasks( List<Processor> allProcessors ){

        Processor result = allProcessors.get(0);
        for ( Processor  p : allProcessors ){
            if (p.getAllTime() < result.getAllTime())
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


    private List<Processor> findEmptyProcessors( List<Processor> processors ){

        List<Processor> result = new ArrayList<Processor>();
        for (Processor p : processors ){
            if (p.getTasks().isEmpty())
                result.add(p);
        }
        return result;
    }


    private boolean hasInputLink( Task task ){
        return task.getInLink().size() > 0;
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
                throw new IllegalStateException("Task not on processor");
            Processor processor = fromTask.getOnProcessor();
            result.add(processor);
        }
        return result;
    }

    private Processor getProcessorFromRelative(List<Processor> processors){

        Processor result = processors.get(0);
        for ( Processor proc : processors ){
            if (result.getAllTime() < proc.getAllTime())
                result = proc;
        }
        return result;

    }

//    private void findShooterWayBetweenProcessors( Processor from, Processor to,
//                                                  List<List<Processor>> listOfWay ,
//                                                  List<Processor> currentWay){
//
//        List<Processor> links = from.getLinks();
//        currentWay.add(from);
//        for ( Processor proc : links ){
//            if ( proc.getID() == to.getID() ){
//                currentWay.add(proc);
//                listOfWay.add(currentWay);
//            } else {
//                findShooterWayBetweenProcessors(proc, to, listOfWay, currentWay );
//            }
//        }
//
//
//    }


}
