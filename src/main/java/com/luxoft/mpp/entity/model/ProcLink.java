package com.luxoft.mpp.entity.model;

/**
 * Created by xXx on 6/5/2016.
 */
public class ProcLink {

    private  int col, row;

    public int getRow() {
        return row;
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
        return "ProcLink{" +
                "col=" + col +
                ", row=" + row +
                '}';
    }

    public ProcLink(int row, int col) {
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
}
