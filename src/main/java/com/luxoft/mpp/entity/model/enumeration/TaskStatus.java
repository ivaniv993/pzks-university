package com.luxoft.mpp.entity.model.enumeration;

/**
 * Created by iivaniv on 21.03.2016.
 */
public enum TaskStatus {

    UPLOAD("UPLOAD"),
    NOT_UPLOAD("NOT UPLOAD");

    private String type;

    TaskStatus(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
