package com.luxoft.mpp.entity.model.enumeration;

/**
 * Created by iivaniv on 21.03.2016.
 */
public enum TaskType {

    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private String type;

    TaskType(String type){
        this.type = type;
    }
}
