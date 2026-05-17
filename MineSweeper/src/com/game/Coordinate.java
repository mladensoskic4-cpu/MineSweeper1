package com.game;

public class Coordinate {
    private int row;
    private int column;

    Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
