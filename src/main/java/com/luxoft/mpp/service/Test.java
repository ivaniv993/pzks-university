package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.SimpleVertex;
import com.luxoft.mpp.entity.model.TaskElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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

        criticalWay(matrix);

    }


    public static void getCriticalWay( TaskElement taskElement, Stack<TaskElement> stack){

        if (taskElement.getRelatedTaskElements().isEmpty()){
            for (TaskElement e : stack){
                System.out.print(e.getId() + ", ");
            }
            System.out.println("----------------");
        }
        for ( TaskElement e : taskElement.getRelatedTaskElements()){

            if ( !stack.contains(e) ){
                stack.push(e);
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
