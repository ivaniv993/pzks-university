package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.*;
import org.primefaces.model.diagram.Element;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by iivaniv on 22.03.2016.
 *
 */
@Service("taskServiceImpl")
public class TaskServiceImpl implements TaskService {

    private int[][] lm = new int[0][0];
    private Integer[] vertex = new Integer[0];
    public List<Task> tasks = new ArrayList<Task>();

    public void saveCSGraph(int[][] lm, Integer[] vertex ){
        this.lm = lm;
        this.vertex = vertex;
        this.tasks = createTaskGraph(lm, vertex);
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

    private Task getTaskByID( List<Task> tasks , int id){

        for ( Task t :  tasks ){
            if (t.getID() == id )
                return t;
        }
        throw new IllegalArgumentException("wrong id");

    }

    public boolean testCSGraph( int[][] matrix){
        for (int row = 0; row < matrix.length; row++) {

            Stack<SimpleVertex> stack = new Stack<SimpleVertex>();
            for (int col = 0; col < matrix[row].length; col++) {


                if(  matrix[row][col] != 0 ) {
                    if (row == col)
                        return true;


                    int nextCol = col;
                    while( !isEmptyRow(matrix, row) && nextCol < matrix.length){

                        SimpleVertex curVertex = new SimpleVertex(nextCol, row);

                        if ( matrix[row][nextCol] != 0 ){
                            stack.push(curVertex);
                            row = nextCol;
                            nextCol = 0;
                            continue;
                        }
                        nextCol++;
                    }
                }
                if (stack.size() < matrix.length) {
                    return false;
                }else
                    return true;
            }

        }
        return false;
    }

    public boolean hasWayToLastVertex(int[][] matrix){

        for (int row = 0; row < matrix.length; row++) {

            if( row == (matrix.length-1))
                return true;

            for (int col = 0; col < matrix[row].length; col++) {
                if(  matrix[row][col] != 0 ) {

                    int nextCol = col;
                    while( !isEmptyRow(matrix, row) && nextCol < matrix.length){


                        if ( matrix[row][nextCol] != 0 ){
                            row = nextCol;
                            nextCol = 0;
                            continue;
                        }
                        nextCol++;
                    }
                    if( row == (matrix.length-1))
                        return true;
                }
            }
        }
        return false;
    }


    public boolean isLoop(int[][] matrix ){

        for (int i = 0; i < matrix.length; i++) {

            int row = i;
            int col = 0;
            Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

            do {

                while ( true ){

                    SimpleVertex currentVertex = new SimpleVertex(col, row);

                    if (vertexStack.contains(currentVertex))
                        return true;

                    if ( isEmptyRow(matrix, row ) ) {
                        SimpleVertex simpleVertex = new SimpleVertex(0, row); // last vertex (empty row)
                        vertexStack.push(simpleVertex);
                        break;
                    }

                    if ( matrix[row][col] != 0){
                        SimpleVertex simpleVertex = new SimpleVertex(col, row);
                        vertexStack.push(simpleVertex);
                        row = col;
                        col = 0;
                    } else {
                        col ++;
                    }
                }

                while ( ! vertexStack.isEmpty() ){
                    SimpleVertex simpleVertex = vertexStack.peek();

                    int nextCol = simpleVertex.getCol();
                    nextCol++;

                    if ( ! isEmptyRestOfRow(matrix, simpleVertex.getRow(), nextCol) ){
                        System.out.println("form if : vertex ["+simpleVertex.getRow()+"]["+simpleVertex.getCol()+"]");
                        simpleVertex = vertexStack.pop();
                        col = nextCol;
                        row = simpleVertex.getRow();
                        break;
                    }
                    System.out.println("vertex [" + simpleVertex.getRow() + "][" + simpleVertex.getCol() + "]");
                    vertexStack.pop();

                }
                System.out.println("-------------------------");


            } while( !vertexStack.isEmpty() || !isEmptyRestOfRow(matrix, row, col) );

        }
        return false;

    }

    public List<Integer> findHangingVertex( int[][] lm ){

        List<Integer> result = new ArrayList<Integer>();
        for ( int i = 0; i < lm.length; i++ ){

            if (isEmptyColumn(lm, i) && isEmptyRow(lm, i))
                result.add(i);

        }
        return result;
    }

    private boolean isEmptyColumn( int[][] matrix,  int x ){
        for (int i =0; i < matrix[x].length; i++ )
            if (matrix[i][x] != 0)
                return false;
        return true;
    }


    private List<Integer> getCriticalWayByTaskValue(List<List<Integer>> criticalWays, Integer[] vertex){
        List<Integer> result = new ArrayList<Integer>();
        int criticalWay = 0;
        for (List<Integer> list : criticalWays) {
            int sum = 0;
            for (Integer taskId : list) {
                sum += vertex[taskId];
            }
            if (sum > criticalWay) {
                criticalWay = sum;
                result = list;
            }
        }
        return result;

    }

    private List<Integer> getCriticalWayByTaskQuantity(List<List<Integer>> criticalWays, Integer[] vertex){
        List<Integer> result = new ArrayList<Integer>();
        int taskQuantity = 0;
        for (List<Integer> list : criticalWays) {

            if (list.size() < taskQuantity)
                continue;

            if (list.size() == taskQuantity) {
                result = getCriticalWayByTaskValue(criticalWays, vertex);
            } else {
                taskQuantity = list.size();
                result = list;
            }
        }
        return result;

    }


    @Override
    public List<SimpleMetaData> getQueueVariant3(int[][] matrix, Integer[] vertex) {
        List<SimpleMetaData> result = new ArrayList<SimpleMetaData>();

        Map<Integer, List<List<SimpleVertex>>> allWaysForEachVertex = getAllWaysForEachVertex(matrix);
        Map<Integer, List<List<Integer>>> criticalWayVertexID = convertSimpleVertexToVertexID(allWaysForEachVertex);

        for (Map.Entry<Integer, List<List<Integer>>> waysForCurrVertex : criticalWayVertexID.entrySet()) {
            List<Integer> list = getCriticalWayByTaskValue(waysForCurrVertex.getValue(), vertex);

            int sum = 0;
            String criticalWay = "";
            for (Integer aList : list) {
                sum += vertex[aList];
                criticalWay += aList+"; ";
            }
            result.add(new SimpleMetaData(criticalWay, waysForCurrVertex.getKey(), sum));
        }

        Collections.sort(result, new Comparator<SimpleMetaData>() {
            @Override
            public int compare(SimpleMetaData o1, SimpleMetaData o2) {
                if (o1.getCriticalWay() > o2.getCriticalWay()) {
                    return -1;
                } else if (o1.getCriticalWay() < o2.getCriticalWay()) {
                    return 1;
                } else return 0;
            }
        });

        return result;
    }

    @Override
    public List<SimpleMetaData> getQueueVariant8(int[][] matrix, Integer[] vertex) {
        List<SimpleMetaData> result = new ArrayList<SimpleMetaData>();

        Map<Integer, List<List<SimpleVertex>>> allWaysForEachVertex = getAllWaysForEachVertex(matrix);
        Map<Integer, List<List<Integer>>> criticalWayVertexID = convertSimpleVertexToVertexID(allWaysForEachVertex);

        for (Map.Entry<Integer, List<List<Integer>>> waysForCurrVertex : criticalWayVertexID.entrySet()) {
            List<Integer> list = getCriticalWayByTaskQuantity(waysForCurrVertex.getValue(), vertex);

            int sum = 0;
            String criticalWay = "";
            for (Integer aList : list) {
                sum += vertex[aList];
                criticalWay += aList+"; ";
            }
            SimpleMetaData simpleMetaData = new SimpleMetaData(criticalWay, waysForCurrVertex.getKey(), sum, list.size());
            simpleMetaData.setVertexID(list);
            result.add(simpleMetaData);
        }

        Collections.sort(result, new Comparator<SimpleMetaData>() {
            @Override
            public int compare(SimpleMetaData o1, SimpleMetaData o2) {
                if (o1.getVertexQuantity() > o2.getVertexQuantity()) {
                    return -1;
                } else if (o1.getVertexQuantity() < o2.getVertexQuantity()) {
                    return 1;
                } else if (o1.getVertexQuantity() == o2.getVertexQuantity()) {
                    if (o1.getCriticalWay() > o2.getCriticalWay()) {
                        return -1;
                    } else if (o1.getCriticalWay() < o2.getCriticalWay()) {
                        return 1;
                    }
                }
                return 0;
            }
        });

        return result;
    }

    @Override
    public List<SimpleMetaData> getQueueVariant13(int[][] matrix, Integer[] vertex) {
        List<SimpleMetaData> result = new ArrayList<SimpleMetaData>();
        Map<Integer, List<List<SimpleVertex>>> allWaysForEachVertex = getAllWaysForEachVertex(matrix);
        Map<Integer, List<List<Integer>>> criticalWayVertexID = convertSimpleVertexToVertexID(allWaysForEachVertex);

        Random random = new Random();
        int rand = criticalWayVertexID.size();
        while( ! criticalWayVertexID.isEmpty() ){

            int randomID = random.nextInt(rand);
            System.out.println("rand = "+randomID);
            if (criticalWayVertexID.get(randomID) == null)
                continue;

            List<Integer> list = getCriticalWayByTaskValue(criticalWayVertexID.get(randomID), vertex);

            int sum = 0;
            String criticalWay = "";
            for (Integer aList : list) {
                sum += vertex[aList];
                criticalWay += aList+"; ";
            }
            SimpleMetaData simpleMetaData = new SimpleMetaData(criticalWay, randomID, sum, list.size());
            simpleMetaData.setVertexID(list);

            result.add(simpleMetaData);
            criticalWayVertexID.remove(randomID);
        }

        return result;
    }



    private Map<Integer, List<List<Integer>>> convertSimpleVertexToVertexID(Map<Integer, List<List<SimpleVertex>>> criticalWay){
        Map<Integer, List<List<Integer>>> result = new HashMap<Integer, List<List<Integer>>>();

        for (Map.Entry<Integer, List<List<SimpleVertex>>> waysForCurrVertex : criticalWay.entrySet()){

            List<List<Integer>> wayList =  new ArrayList<List<Integer>>();
            for (List<SimpleVertex> list : waysForCurrVertex.getValue()){

                List<Integer> idsList = new ArrayList<Integer>();
                for (SimpleVertex e :list) {
                    idsList.add(e.getRow());
                }
                wayList.add(idsList);
            }
            result.put(waysForCurrVertex.getKey(), wayList);
        }
        return result;
    }

    private Map<Integer, List<List<SimpleVertex>>> getAllWaysForEachVertex(int matrix[][]){

        Map<Integer, List<List<SimpleVertex>>> result = new HashMap<Integer, List<List<SimpleVertex>>>();

        for (int nextRow = 0; nextRow < matrix.length; nextRow++) {
            result.put(nextRow, getAllWayForCurrentVertex(matrix, nextRow));
        }

        return result;

    }


    private static List<List<SimpleVertex>> getAllWayForCurrentVertex( int matrix[][], int currentVertexID){

        List<List<SimpleVertex>> result = new ArrayList<List<SimpleVertex>>();

        Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

        System.out.println("===========================");

        int col = 0;
        int row = currentVertexID;
        do{

            while ( true ){

                if ( isEmptyRow(matrix, row ) ) {
                    SimpleVertex simpleVertex = new SimpleVertex(0, row); // last vertex (empty row)
                    vertexStack.push(simpleVertex);
                    break;
                }

                if ( matrix[row][col] != 0){
                    SimpleVertex simpleVertex = new SimpleVertex(col, row);
                    vertexStack.push(simpleVertex);
                    row = col;
                    col = 0;
                } else {
                    col ++;
                }
            }

            result.add(new ArrayList<SimpleVertex>(vertexStack));

            while ( ! vertexStack.isEmpty() ){
                SimpleVertex simpleVertex = vertexStack.peek();

                int nextCol = simpleVertex.getCol();
                nextCol++;

                if ( ! isEmptyRestOfRow(matrix, simpleVertex.getRow(), nextCol) ){
                    System.out.println("form if : vertex ["+simpleVertex.getRow()+"]["+simpleVertex.getCol()+"]");
                    simpleVertex = vertexStack.pop();
                    col = nextCol;
                    row = simpleVertex.getRow();
                    break;
                }
                System.out.println("vertex [" + simpleVertex.getRow() + "][" + simpleVertex.getCol() + "]");
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

    public List<Task> getTasks() {
        return tasks;
    }





}
