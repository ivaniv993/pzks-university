package com.luxoft.mpp.dao;

import com.luxoft.mpp.entity.model.Etl;
import com.luxoft.mpp.entity.model.TaskVertex;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
 * Created by iivaniv on 22.03.2016.
 *
 */

//@Repository
public class TaskGraphDao extends AbstractBaseDao implements TaskDao{


//    @Transactional(propagation = Propagation.REQUIRED)
    public void save(TaskVertex taskVertex){
//        sessionFactory.getCurrentSession().save(taskVertex);
    }

//    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<TaskVertex> getByEtl(Etl etl){

//        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TaskVertex.class);
//        return criteria.add(Restrictions.eq("upload_id", etl.getId())).list();

        return Collections.emptyList();

    }


}
