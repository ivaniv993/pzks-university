package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.SimpleVertex;
import com.luxoft.mpp.entity.model.TaskElement;

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


    public static void main(String[] args){

        System.out.println("Link matrix ");
        for (int[] aMatrix : matrix) {
            for (int anAMatrix : aMatrix) {
                System.out.print("  " + anAMatrix);
            }
            System.out.println();
        }
        System.out.println("Link matrix -------------");

        Stack<TaskElement> ways =  new Stack<TaskElement>();

        Map<Integer, List<List<SimpleVertex>>> allWaysForEachVertex = new HashMap<Integer, List<List<SimpleVertex>>>();
        allWaysForEachVertex = getAllWaysForEachVertex(matrix);

        System.out.println("******************");
        for (Map.Entry<Integer, List<List<SimpleVertex>>> waysForCurrVertex : allWaysForEachVertex.entrySet()){

            System.out.println("Vertex = "+waysForCurrVertex.getKey());

            int k = 0;
            for (List<SimpleVertex> list : waysForCurrVertex.getValue()){

                boolean printRow = true;
                for (SimpleVertex e :list) {
                    System.out.print(e.getRow() + ", ");

                }
                System.out.println(" Way "+(++k));
            }

        }

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






}
