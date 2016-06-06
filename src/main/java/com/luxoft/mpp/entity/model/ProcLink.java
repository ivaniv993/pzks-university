package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/5/2016.
 */
public class ProcLink {

    private  int col, row;

    private static int permitLimit;
    private int permits;

    public ProcLink(int col, int row, int permits) {

        this.col = col;
        this.row = row;
        this.permits = permits;
        ProcLink.permitLimit = permits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ProcLink)) return false;

        ProcLink procLink = (ProcLink) o;

        if (col != procLink.col) return false;
        if (row != procLink.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = col;
        result = 31 * result + row;
        return result;
    }

    @Override
    public String toString() {
        return  "[" + col +
                "][" + row +"] permit : "+permits;
    }

    public ProcLink( int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getPermits() {

        return permits;
    }

    public void setPermits(int permits) {
        ProcLink.permitLimit = permits;
        this.permits = permits;
    }

    public int getRow() {
        return row;
    }

    public boolean canObtainPermit(){
        return this.permits > 0;
    }

    public ProcLink obtainPermit(){

        if (permits < 0 ) throw new IllegalStateException("Link have not any free permits");

        permits--;

        return this;
    }

    public void releasePermit(){

        if (permits == permitLimit ) throw new IllegalStateException("Permit limit already archived");

        this.permits ++;
    }

}
