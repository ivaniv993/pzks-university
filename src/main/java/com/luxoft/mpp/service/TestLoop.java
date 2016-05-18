package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.SimpleVertex;

import java.util.Stack;

/**
 * Created by xXx on 5/19/2016.
 */
public class TestLoop {

    static int[][] lmq = {{0, 0, 0, 51},
            {0, 0, 48, 48},
            {48, 48, 0, 0},
            {0, 0, 0, 0}};

    static int[][] lm = {{0,  0,  0,  51},
                         {48, 0,  48, 0},
                         {48, 48, 0,  0},
                         {0,  0,  0,  0}};

    public static void main(String[] args ){

        System.out.println(isLoop(lm));

    }

    public static boolean isLoop(int[][] matrix ){

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


            } while( !vertexStack.isEmpty() || !isEmptyRestOfRow(matrix, row, col));

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
