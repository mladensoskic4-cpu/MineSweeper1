package com.game;

public record Move(int row, int col, boolean wasSafe) {
    @Override
    public String toString() {
        return "Move{row=" + row + ", col=" + col + ", wasSafe=" + wasSafe + "}";
    }
}
