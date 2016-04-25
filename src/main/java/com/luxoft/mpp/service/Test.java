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

        getCriticalWay(new TaskElement(), ways);

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






}
