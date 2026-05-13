package minesweeper.player;

public class Move {
    private final int row;
    private final int col;
    private final boolean wasSafe;

    public Move(int row, int col, boolean wasSafe) {
        this.row = row;
        this.col = col;
        this.wasSafe = wasSafe;
    }

    public int getRow() {
    	return row; 
    }
    public int getCol() { 
    	return col; 
    }
    public boolean wasSafe() { 
    	return wasSafe; 
    }

    @Override
    public String toString() {
        return "Move{row=" + row + ", col=" + col + ", wasSafe=" + wasSafe + "}";
    }
}
