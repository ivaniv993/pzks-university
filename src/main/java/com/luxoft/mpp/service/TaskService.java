package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.*;
import org.primefaces.model.diagram.Element;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by iivaniv on 22.03.2016.
 */
public interface TaskService {

    void saveCSGraph( int[][] lm, Integer[] vertex );

    List<Task> createTaskGraph(int[][] linkMatrix, Integer[] vertex);

    boolean isLoop(int[][] lm );

    boolean hasWayToLastVertex(int[][] lm);

    List<SimpleMetaData> getQueueVariant3(int[][] matrix, Integer[] vertex);

    List<SimpleMetaData> getQueueVariant8(int[][] matrix, Integer[] vertex);

    List<SimpleMetaData> getQueueVariant13(int[][] matrix, Integer[] vertex);

    List<Integer> findHangingVertex( int[][] lm );

    boolean testCSGraph( int[][] lm);

//    Map<Integer, Integer> generateNumberOfLinks(int[] vertex);

}
