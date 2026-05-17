package com.game;

public class Cell {
    private final boolean isMine;
    private CellState state;
    private int adjacentMines;

    public Cell(boolean isMine) {
        this.isMine = isMine;
        this.state = CellState.HIDDEN;
        this.adjacentMines = 0;
    }

    public boolean isMine() {
        return isMine;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
