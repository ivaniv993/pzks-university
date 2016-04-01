package com.luxoft.mpp.service;

import com.luxoft.mpp.dao.TaskDao;
import com.luxoft.mpp.dao.TaskGraphDao;
import com.luxoft.mpp.entity.model.Etl;
import com.luxoft.mpp.entity.model.TaskVertex;
import org.primefaces.model.diagram.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by iivaniv on 22.03.2016.
 *
 */
@Service("taskVertexServiceImpl")
public class TaskVertexServiceImpl  {

//    @Autowired
//    private TaskDao taskGraphDao;

    public void saveVertex(List<Element> elements){

//        Etl etl = new Etl();
//        for (Element e : elements ){
//
//            TaskVertex vertex = new TaskVertex();
//
//            vertex.setEtl(etl);
//
//            taskGraphDao.save(vertex);
//        }


    }

}
