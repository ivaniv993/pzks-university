package com.luxoft.mpp.dao;

import com.luxoft.mpp.entity.model.Etl;
import com.luxoft.mpp.entity.model.TaskVertex;

import java.util.List;

/**
 * Created by iivaniv on 22.03.2016.
 */
public interface TaskDao {

    void save(TaskVertex taskVertex);

    List<TaskVertex> getByEtl(Etl etl);
}
