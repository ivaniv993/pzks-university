package com.luxoft.mpp.entity.model;

/**
 * Created by iivaniv on 25.04.2016.
 */
public class SimpleVertex{

    private int col;

    private int row;

    public SimpleVertex(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleVertex that = (SimpleVertex) o;

        if (col != that.col) return false;
        return row == that.row;

    }

    @Override
    public int hashCode() {
        int result = col;
        result = 31 * result + row;
        return result;
    }

    public int getCol() {

        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "[ "+col+" ][ "+row+" ]";
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
