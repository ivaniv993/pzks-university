package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.*;
//import com.sun.javafx.sg.prism.NGShape;
import edu.princeton.cs.algorithms.AdjMatrixEdgeWeightedDigraph;
import edu.princeton.cs.algorithms.DirectedEdge;
import edu.princeton.cs.algorithms.FloydWarshall;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xXx on 6/4/2016.
 */
@Service("modelingServiceImpl")
public class ModelingServiceImpl implements ModelingService{


    public List<Processor> createMockCS(int[][] lm, int permit){

        List< Processor > result = new ArrayList<Processor>();

        Processor[] processors = new Processor[lm.length];
        for (int i = 0; i < lm.length; i++) {
            processors[i] = new Processor(i);
        }

        for (int i = 0; i < lm.length; i++) {
            for (int j = i; j < lm[i].length; j++) {
                if ( lm[i][j] != 0 ){
                    ProcLink link = new ProcLink( processors[i], processors[j], permit);
                    processors[i].getLinks().add(link);
                    processors[j].getLinks().add(link);
                }
            }
            result.add(processors[i]);
        }

        return result;
    }


    private static List<List<ProcLink>> getAllWayForProcessors( int matrix[][], int source, int dest){

        List<List<ProcLink>> result = new ArrayList<List<ProcLink>>();

        Stack<ProcLink> vertexStack = new Stack<ProcLink>();

        System.out.println("===========================");

        int col = 0;
        int row = source;
        do{

            while ( true ){

                if(isEmptyRow(matrix, row) ){

                    for (int i = 0; i < matrix.length; i++) {
                        int verticalConnection = matrix[i][row];
                        if ( verticalConnection != 0 && i == dest){
                            ProcLink procLink = new ProcLink(i, row);
                            vertexStack.push(procLink);
                            break;
                        }
                    }

                    break;
                }

                if ( row == dest ) {
//                    SimpleVertex simpleVertex = new SimpleVertex(0, row);
//                    vertexStack.push(simpleVertex);
                    System.out.println("destination found");
                    break;
                }

                if ( matrix[row][col] != 0){
                    ProcLink procLink = new ProcLink(col, row);
                    vertexStack.push(procLink);
                    row = col;
                    col = 0;
                } else {
                    col ++;
                }
            }

            result.add(new ArrayList<ProcLink>(vertexStack));

            while ( ! vertexStack.isEmpty() ){
                ProcLink procLink = vertexStack.peek();

                int nextCol = procLink.getCol();
                nextCol++;

                if ( ! isEmptyRestOfRow(matrix, procLink.getRow(), nextCol) ){
                    System.out.println("form if : vertex ["+procLink.getRow()+"]["+procLink.getCol()+"]");
                    procLink = vertexStack.pop();
                    col = nextCol;
                    row = procLink.getRow();
                    break;
                }
                System.out.println("vertex [" + procLink.getRow() + "][" + procLink.getCol() + "]");
                vertexStack.pop();

            }
            System.out.println("-------------------------");

        }while ( ! vertexStack.isEmpty() || !isEmptyRestOfRow(matrix, row, col));

        return result;

    }

    private static boolean isEmptyRow( int[][] matrix, int inRow ){
        for (int i = 0; i < matrix[inRow].length; i++) {
            if (matrix[inRow][i] != 0)
                return false;
        }
        return true;
    }

    private static boolean isEmptyRestOfRow( int[][] matrix, int inRow, int fromNextCol ){
        if ( fromNextCol >= matrix[inRow].length )
            return true;
        for (int i = fromNextCol; i < matrix[inRow].length; i++) {
            if (matrix[inRow][i] != 0)
                return false;
        }
        return true;
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


    private static List<ProcLink> getVerticalLink(int[][] matrix, int col ){

        List<ProcLink > result = new ArrayList<ProcLink>();
        for (int i = 0; i < matrix.length; i++) {

            if( matrix[i][col] != 0){
                result.add(new ProcLink(i, col));
            }
        }
        return result;
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


    public List<Processor> modeling(List<Processor> processors, List<Task> tasksGraph, int[][] matrixCS,  List<SimpleMetaData> queue ){

//        int currentTime = 0;
//        for (SimpleMetaData elem : queue){
//            int id = elem.getVertexId();
//            Task task = getTaskByID(tasksGraph, id);
//
//            if ( hasInputLink(task) ){
//
//                // if relative tasks already on processors
//
//                List<Processor> relativeProcessors = findProcessorsWithRelativeTask(task);
//
//                Processor procCandidate = getProcessorWithSmallerTimeFromRelative(relativeProcessors);
//
//                List<Processor> bufList = new ArrayList<Processor>(relativeProcessors);
//                bufList.remove(procCandidate);
//
//                int source = procCandidate.getID();
//
//                Map<Processor, IdleTime > processorTimes = new HashMap<Processor, IdleTime>();
//                for ( Processor procDestination : bufList ) {
//                    int destination = procDestination.getID();
//
//                    System.out.println("[source= "+source+"][dest = "+destination+"]");
//                    List<List<ProcLink>> allWays = getAllWayForProcessors(matrixCS, source, destination);
//                    TaskLink taskLink = getLinkBetweenTaskAndTaskOnProcessor(procDestination, task);
//                    List<ProcLink> shorterWay = getShorterTransitionWay(allWays, taskLink.getTransferTime());
//                    int transferTime = getDurationOfTransition(shorterWay, taskLink.getTransferTime());
//
//
//                    IdleTime idleTime = new IdleTime(transferTime);
//
//                    processorTimes.put(procDestination, idleTime);
//                }
//
//                //synchronize processors
//                IdleTime idleTime = new IdleTime(0);
//                for ( Map.Entry<Processor, IdleTime> entry : processorTimes.entrySet()){
//
//                    int longestTransition = entry.getKey().getCurrentWorkingTime() + entry.getValue().getTime();
//
//                    if ( longestTransition > procCandidate.getCurrentWorkingTime() ){
//                        idleTime = new IdleTime(longestTransition-procCandidate.getCurrentWorkingTime());
//                    }
//
//                }
//                procCandidate.addTimeLine(idleTime);
//
//                task.setOnProcessor(procCandidate);
//                procCandidate.addTimeLine(task);
//
//
//            } else {
//
//
//
//                List<Processor> emptyProcessors = findEmptyProcessors(processors);
//                if ( ! emptyProcessors.isEmpty() ) {
//
//                    //set on  empty processor
//                    Processor proc = getProcessorByConnectivityAndFreedom(emptyProcessors);
//                    task.setOnProcessor(proc);
//                    proc.addTimeLine(task);
//
//                    // synchronize current time
//                    if (currentTime < task.getTimeDuration())
//                        currentTime = task.getTimeDuration();
//
//                } else {
//                    //set proccessor with smaller task
//                    Processor proc = findAnyProcessorWithSmallerTasks(processors);
//                    task.setOnProcessor(proc);
//                    proc.addTimeLine(task);
//
//
//                    // synchronize current time
//                    if ( currentTime < proc.getCurrentWorkingTime() )
//                        currentTime = proc.getCurrentWorkingTime();
//
//                }
//
//            }
//        }
        return processors;
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

//    public List<List<ProcLink>>

    private int getDurationOfTransition( List<ProcLink> way, int linkDuration){

        return way.size()*linkDuration;

    }

    private List<ProcLink> getShorterTransitionWay( List<List<ProcLink>> allWays, int linkDuration ){

        if (allWays.isEmpty()) throw new IllegalStateException("No any ways");

        List<ProcLink> result = allWays.get(0);
        int time = getDurationOfTransition(result, linkDuration);
        for (int i = 1; i < allWays.size(); i++) {

            int durationTime = getDurationOfTransition( allWays.get(i), linkDuration );

            if ( time > durationTime) {
                time = durationTime;
                result = allWays.get(i);
            }
        }
        return result;
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
            if (result.getCurrentWorkingTime() < proc.getCurrentWorkingTime())
                result = proc;
        }
        return result;

    }








}
