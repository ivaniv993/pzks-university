package com.luxoft.mpp.service;

import org.primefaces.model.diagram.Element;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by iivaniv on 22.03.2016.
 *
 */
@Service("taskVertexServiceImpl")
public class TaskServiceImpl implements TaskService {

    private int[][] lm = new int[0][0];
    private Integer[] vertex = new Integer[0];



//    @Autowired
//    private TaskDao taskGraphDao;

    public void saveVertex(List<Element> elements){


    }


    public void updateVertex( int duration ){

        List<Integer> v = Arrays.asList(vertex);
        v.add(duration);
        v.toArray(vertex);

    }

    public void updateTaskModel(int from, int to, int value){

        if ( value < 0){
            throw new IllegalArgumentException(value +" can`t be lover zero ");
        }

        if ( from <= lm[0].length && from >= lm.length){
            throw new IllegalArgumentException(from +" out of array ");
        }

        if ( to <= lm[0].length && to >= lm.length){
            throw new IllegalArgumentException(to +" out of array ");
        }

        lm[from][to] = value;

    }


    public void isLoop(){

        for( int i = 0; i < lm.length; i ++ ){

            for ( int j  = 0; j < lm[i].length; j++ ){

                if (lm[i][j] != 0){
                    
                }

            }

        }

    }

}
