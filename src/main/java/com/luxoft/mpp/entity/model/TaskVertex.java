package com.luxoft.mpp.entity.model;

import com.luxoft.mpp.entity.model.enumeration.TaskStatus;
import com.luxoft.mpp.entity.model.enumeration.TaskType;

import javax.persistence.*;
import java.util.List;

/**
 * Created by iivaniv on 18.03.2016.
 *
 */
//@Entity
//@Table(name="task_vertex1", schema = "public")
public class TaskVertex {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Long id;

//    @JoinColumn(name = "upload_id", referencedColumnName = "id")
//    @ManyToOne( optional = false )
    private Etl etl;

//    @JoinColumn(name = "task_vertex_id", referencedColumnName = "id")
//    @ManyToOne
    private TaskVertex taskVertex;

//    @Column(name = "status")
    private TaskStatus taskStatus;

    private String description;

//    @OneToMany
    private List<TaskVertex> relatedTaskVertex;

//    @Column(name = "task_time")
    private int time;

//    @Column(name = "task_type")
//    @Enumerated(EnumType.STRING)
    private TaskType taskType;

//    @Column(name = "task_x")
    private int x;

//    @Column(name = "task_y")
    private int y;

    public TaskVertex() { }

    public TaskVertex(TaskVertex taskVertex) {
        this.description = taskVertex.description;
        this.time = taskVertex.time;
        this.x = taskVertex.x;
        this.y = taskVertex.y;
    }

    public Etl getEtl() {
        return etl;
    }

    public void setEtl(Etl etl) {
        this.etl = etl;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskVertex getTaskVertex() {
        return taskVertex;
    }

    public void setTaskVertex(TaskVertex taskVertex) {
        this.taskVertex = taskVertex;
    }

    public List<TaskVertex> getRelatedTaskVertex() {
        return relatedTaskVertex;
    }

    public void setRelatedTaskVertex(List<TaskVertex> relatedTaskVertex) {
        this.relatedTaskVertex = relatedTaskVertex;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
