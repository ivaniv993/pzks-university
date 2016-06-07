package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.Processor;
import com.luxoft.mpp.entity.model.SimpleMetaData;
import com.luxoft.mpp.entity.model.Task;

import java.util.List;

/**
 * Created by xXx on 6/4/2016.
 */
public interface ModelingService {

    List<Processor> createMockCS();

    List<Task> createTaskGraph(int[][] linkMatrix, Integer[] vertex);

    List<Processor> sortByConnectivity( List<Processor> processors );

    List<Processor> modeling(List<Processor> processors, List<Task> tasksGraph, int[][] matrixCS,  List<SimpleMetaData> queue );

}
