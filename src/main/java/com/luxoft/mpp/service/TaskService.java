package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.SimpleVertex;
import com.luxoft.mpp.entity.model.TaskElement;
import com.luxoft.mpp.entity.model.TaskVertex;
import org.primefaces.model.diagram.Element;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by iivaniv on 22.03.2016.
 */
public interface TaskService {

    void saveVertex(List<Element> elements);

    boolean isLoop(int[][] lm );

    Map<Integer, List<List<SimpleVertex>>> getAllWaysForEachVertex(int matrix[][]);




}
