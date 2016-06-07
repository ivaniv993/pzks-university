package com.luxoft.mpp.entity.model;

/**
 * Created by iivaniv on 07.06.2016.
 */
public class CSLink {

    private int source, dest;

    public CSLink() {
    }

    public CSLink(int source, int dest) {

        this.source = source;
        this.dest = dest;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }
}
