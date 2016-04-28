package com.luxoft.mpp.entity.model;

import java.util.List;

/**
 * Created by xXx on 4/28/2016.
 */
public class SimpleMetaData {

    private String list;

    private int vertexId;

    private int criticalWay;

    private List<Integer> vertexID;

    private int vertexQuantity;

    public SimpleMetaData(String list, int vertexId, int criticalWay, int vertexQuantity) {
        this.list = list;
        this.vertexId = vertexId;
        this.criticalWay = criticalWay;
        this.vertexQuantity = vertexQuantity;
    }

    public SimpleMetaData(String list, int vertexId, int criticalWay) {
        this.list = list;
        this.vertexId = vertexId;
        this.criticalWay = criticalWay;
    }

    public int getVertexQuantity() {

        return vertexQuantity;
    }

    public void setVertexQuantity(int vertexQuantity) {
        this.vertexQuantity = vertexQuantity;
    }

    public List<Integer> getVertexID() {
        return vertexID;
    }

    public void setVertexID(List<Integer> vertexID) {
        this.vertexID = vertexID;
    }

    public String getList() {

        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public int getVertexId() {
        return vertexId;
    }

    public void setVertexId(int vertexId) {
        this.vertexId = vertexId;
    }

    public int getCriticalWay() {
        return criticalWay;
    }

    public void setCriticalWay(int criticalWay) {
        this.criticalWay = criticalWay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleMetaData)) return false;

        SimpleMetaData that = (SimpleMetaData) o;

        if (criticalWay != that.criticalWay) return false;
        if (vertexId != that.vertexId) return false;
        if (list != null ? !list.equals(that.list) : that.list != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = list != null ? list.hashCode() : 0;
        result = 31 * result + vertexId;
        result = 31 * result + criticalWay;
        return result;
    }
}
