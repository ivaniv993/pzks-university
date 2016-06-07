package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/5/2016.
 */
public class ProcLink {

    private  int col, row;

    private Processor source, dest;

    public ProcLink() {
    }

    public Processor getSource() {
        return source;
    }

    public void setSource(Processor source) {
        this.source = source;
    }

    public Processor getDest() {
        return dest;
    }

    public void setDest(Processor dest) {
        this.dest = dest;
    }

    public ProcLink(Processor source, Processor dest, int permits) {

        this.source = source;
        this.dest = dest;
        this.permits = permits;
    }

    public ProcLink(Processor source, Processor dest) {

        this.source = source;
        this.dest = dest;
    }

    private static int permitLimit;
    private int permits;

    private boolean passed;

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public ProcLink(int col, int row, int permits) {

        this.col = col;
        this.row = row;
        this.permits = permits;
        ProcLink.permitLimit = permits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcLink procLink = (ProcLink) o;

        if(source == procLink.source && dest == procLink.dest) return true;

        if(source == procLink.dest && dest == procLink.source) return true;

        return false;
    }

    @Override
    public int hashCode() {
        int result = col;
        result = 31 * result + row;
        return result;
    }

    @Override
    public String toString() {
        return  "[" + source +
                "][" + dest +"] permit : "+permits;
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
