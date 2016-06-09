package com.luxoft.mpp.service;

import org.springframework.stereotype.Service;

/**
 * Created by xXx on 6/8/2016.
 */
@Service("testCSGraphService")
public class TestCSGraphService {


    public void testGraph(int[][] matrix){

        int [][] buffMatrix = new int[matrix.length][matrix.length];

        for ( int l = 0; l < matrix.length; l ++ ) {
            for (int i = 0; i < matrix.length; i++) {

                for (int j = 0; j < matrix.length; j++) {

                    int sum = 0;
                    for (int k = 0; k < matrix.length; k++) {

                        sum += matrix[i][k] * matrix[k][j];

                    }
                    buffMatrix[i][j] = sum;
                }
            }


        }

        System.out.println("\n________Test after graph___________");

        for (int i = 0; i < buffMatrix.length; i++) {
            for (int j = 0; j < buffMatrix[i].length; j++) {
                System.out.print(buffMatrix[i][j] + ", ");
            }
            System.out.println();
        }

        System.out.println("\n________Test after graph___________");
        System.out.println(matrix.length);

    }


}
