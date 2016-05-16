package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.SimpleMetaData;
import com.luxoft.mpp.entity.model.SimpleVertex;

import java.util.*;

/**
 * Created by iivaniv on 25.04.2016.
 */
public class Test {

    private static int[][] matrix = {{0,0,1,1,0},
                                     {0,0,0,0,1},
                                     {0,1,0,0,0},
                                     {0,0,0,0,1},
                                     {0,0,0,0,0}};

    private static Integer[] vertex = {5,6,7,8,9};

    public static void main(String[] args){

        System.out.println("Link matrix ");
        for (int[] aMatrix : matrix) {
            for (int anAMatrix : aMatrix) {
                System.out.print("  " + anAMatrix);
            }
            System.out.println();
        }
        System.out.println("Link matrix -------------");

        List<SimpleVertex> simpleVertexes = testWaySearchForCurrentRow(matrix, 4);

        getQueueVariant8( matrix, vertex );

        for ( SimpleVertex v : simpleVertexes ){
            System.out.println(" vertex : "+v.toString());
        }

    }

    public static List<SimpleMetaData> getQueueVariant8(int[][] matrix, Integer[] vertex) {
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

//        Collections.sort(result, new Comparator<SimpleMetaData>() {
//            @Override
//            public int compare(SimpleMetaData o1, SimpleMetaData o2) {
//                if (o1.getVertexQuantity() > o2.getVertexQuantity()) {
//                    return -1;
//                } else if (o1.getVertexQuantity() < o2.getVertexQuantity()) {
//                    return 1;
//                } else if (o1.getVertexQuantity() == o2.getVertexQuantity()) {
//                    if (o1.getCriticalWay() > o2.getCriticalWay()) {
//                        return -1;
//                    } else if (o1.getCriticalWay() < o2.getCriticalWay()) {
//                        return 1;
//                    }
//                }
//                return 0;
//            }
//        });

        return result;
    }

    private static Map<Integer, List<List<Integer>>> convertSimpleVertexToVertexID(Map<Integer, List<List<SimpleVertex>>> criticalWay){
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



    private static List<Integer> getCriticalWayByTaskQuantity(List<List<Integer>> criticalWays, Integer[] vertex){
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


    private static List<Integer> getCriticalWayByTaskValue(List<List<Integer>> criticalWays, Integer[] vertex){
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


    public static void criticalWay(int[][] matrix){


        for (int nextRow = 0; nextRow < matrix.length; nextRow++) {

            Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

            System.out.println("===========================");

            int col = 0;
            int row = nextRow;
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

        }

    }

    public static Map<Integer, List<List<SimpleVertex>>> getAllWaysForEachVertex(int matrix[][]){

        Map<Integer, List<List<SimpleVertex>>> result = new HashMap<Integer, List<List<SimpleVertex>>>();

        for (int nextRow = 0; nextRow < matrix.length; nextRow++) {
            result.put(nextRow, getAllWayForGraphStart(matrix, nextRow));
        }

        return result;

    }

    private static List<List<SimpleVertex>> getAllWayForGraphStart(int matrix[][],  int currentVertexID){

        List<List<SimpleVertex>> result = new ArrayList<List<SimpleVertex>>();

        Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

        System.out.println("===========================");



        int col = currentVertexID;
        int row = 0;

        do {

            while(  hasReferenceToRow(matrix, col, row) ) {

                if (matrix[row][col] != 0){
                    vertexStack.push(new SimpleVertex(col, row));
                    col = row;
                    row = 0;
                }else{
                    row ++;
                }
            }

            result.add(new ArrayList<SimpleVertex>(vertexStack));

            while( ! vertexStack.isEmpty() ){

                SimpleVertex simpleVertex = vertexStack.peek();

                int nextRow = simpleVertex.getRow();
                nextRow++;

                if ( ! hasReferenceToRow(matrix, simpleVertex.getRow(), nextRow) ){
                    System.out.println("form if : vertex ["+simpleVertex.getRow()+"]["+simpleVertex.getCol()+"]");
                    simpleVertex = vertexStack.pop();
                    row = nextRow;
                    col = simpleVertex.getCol();
                    break;
                }
                System.out.println("vertex [" + simpleVertex.getRow() + "][" + simpleVertex.getCol() + "]");
                vertexStack.pop();
            }



        }while( ! vertexStack.isEmpty() );

//        do{
//
//            while ( true ){
//
//                if ( isEmptyRow(matrix, row ) ) {
//                    SimpleVertex simpleVertex = new SimpleVertex(0, row ); // last vertex (empty row)
//                    vertexStack.push(simpleVertex);
//                    break;
//                }
//
//                if ( matrix[row][col] != 0){
//                    SimpleVertex simpleVertex = new SimpleVertex(col, row);
//                    vertexStack.push(simpleVertex);
//                    row = col;
//                    col = 0;
//                } else {
//                    col ++;
//                }
//            }
//
//            result.add(new ArrayList<SimpleVertex>(vertexStack));
//
//            while ( ! vertexStack.isEmpty() ){
//                SimpleVertex simpleVertex = vertexStack.peek();
//
//                int nextCol = simpleVertex.getCol();
//                nextCol++;
//
//                if ( ! isEmptyRestOfRow(matrix, simpleVertex.getRow(), nextCol) ){
//                    System.out.println("form if : vertex ["+simpleVertex.getRow()+"]["+simpleVertex.getCol()+"]");
//                    simpleVertex = vertexStack.pop();
//                    col = nextCol;
//                    row = simpleVertex.getRow();
//                    break;
//                }
//                System.out.println("vertex [" + simpleVertex.getRow() + "][" + simpleVertex.getCol() + "]");
//                vertexStack.pop();
//
//            }
//            System.out.println("-------------------------");
//
//        }while ( ! vertexStack.isEmpty() || !isEmptyRestOfRow(matrix, row, col));

        return result;

    }


    private static List<SimpleVertex> testWaySearchForCurrentRow(int[][] matrix, int row){

        Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

        int currentCol = row;
        int nextRow = 0;
        vertexStack.push(new SimpleVertex(currentCol, nextRow));
        while(  hasReferenceToRow(matrix, currentCol, nextRow) ) {

            if (matrix[nextRow][currentCol] != 0){
                vertexStack.push(new SimpleVertex(currentCol, nextRow));
                currentCol = nextRow;
                nextRow = 0;
            }else{
                nextRow ++;
            }

        };

        return vertexStack;
    }

    private static boolean hasReferenceToRow( int[][] matrix, int col, int fromRow ){

        for (int i = fromRow; i < matrix.length; i++ ){
            if (matrix[i][col] != 0)
                return true;
        }
        return false;
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






}
