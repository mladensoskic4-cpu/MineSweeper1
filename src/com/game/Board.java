package com.game;
import com.utils.Queue;

import java.util.Random;

public class Board {
    private static final int[] DX = {-1, -1, -1,  0,  0,  1,  1,  1};
    private static final int[] DY = {-1,  0,  1, -1,  1, -1,  0,  1};

    private final Cell[][] grid;
    private final int size;
    private final int numberOfMines;

    public Board(int size, int numberOfMines) {
        if (size <= 0) {
            throw new IllegalArgumentException(
                    "Grid size must be a positive integer, but was: " + size);
        }

        if (numberOfMines < 0 || numberOfMines >= size * size) {
            throw new IllegalArgumentException(
                    "numberOfMines must be in [0, size*size - 1], but was: " + numberOfMines
                            + " for a " + size + "x" + size + " grid.");
        }

        this.size = size;
        this.grid = new Cell[size][size];
        this.numberOfMines = numberOfMines;

        initializeGrid();
        placeMines(numberOfMines);
        calculateAdjacentMines();
    }

    private void initializeGrid() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                grid[row][column] = new Cell(false);
            }
        }
    }

    private void placeMines(int numMines) {
        Random rng = new Random();
        int placed = 0;

        while (placed < numMines) {
            int row = rng.nextInt(size);
            int column = rng.nextInt(size);
            if (!grid[row][column].isMine()) {
                grid[row][column] = new Cell(true);
                placed++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (!grid[row][column].isMine()) {
                    int count = 0;
                    for (int direction = 0; direction < 8; direction++) {
                        int nr = row + DX[direction];
                        int nc = column + DY[direction];
                        if (inBounds(nr, nc) && grid[nr][nc].isMine()) {
                            count++;
                        }
                    }
                    grid[row][column].setAdjacentMines(count);
                }
            }
        }
    }

    public MoveResult revealCell(int row, int col) {
        if (!inBounds(row, col)) return MoveResult.OUT_OF_BOUNDS;

        Cell target = getCell(row, col);
        if (target.getState() != CellState.HIDDEN) return MoveResult.ALREADY_REVEALED;

        target.setState(CellState.REVEALED);

        if (target.isMine()) return MoveResult.REVEALED_MINE;
        if(target.getAdjacentMines() > 0) return MoveResult.REVEALED_EMPTY;

        Queue<Coordinate> queue = new Queue<>();
        queue.push(new Coordinate(row, col));

        while (!queue.isEmpty()) {
            Coordinate coordinate = queue.pop();

            for (int direction = 0; direction < 8; direction++) {
                if (Math.abs(DX[direction]) == Math.abs(DY[direction])) continue;

                int nr = coordinate.getRow() + DX[direction];
                int nc = coordinate.getColumn() + DY[direction];

                if (!inBounds(nr, nc)) continue;

                Cell neighbor = getCell(nr, nc);

                if (neighbor.getState() != CellState.HIDDEN) continue;
                if (neighbor.isMine()) continue;

                neighbor.setState(CellState.REVEALED);

                if (neighbor.getAdjacentMines() == 0) {
                    queue.push(new Coordinate(nr, nc));
                }
            }
        }

        return MoveResult.REVEALED_EMPTY;
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    private boolean inBounds(int row, int column) {
        return row >= 0 && row < size && column >= 0 && column < size;
    }

    public GameOutcome getGameState() {
        int numberOfUnrevealedFields = 0;

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                if (getCell(row, column).getState() == CellState.REVEALED) {
                    if (getCell(row, column).isMine()) return GameOutcome.DEFEAT;
                    numberOfUnrevealedFields++;
                }
            }
        }

        if (numberOfUnrevealedFields == numberOfMines) {
            return GameOutcome.VICTORY;
        }

        return GameOutcome.IN_PROGRESS;
    }

    public void printBoard() {
        IO.print("  ");
        for (int i = 0; i < size; i++) IO.print("%d ".formatted(i));
        IO.println();

        for (int row = 0; row < size; row++) {
            IO.print("%d ".formatted(row));
            for (int column = 0; column < size; column++) {
                String renderingCharacter = "";
                Cell cell = getCell(row, column);

                switch (cell.getState()) {
                    case HIDDEN -> renderingCharacter = "#";
                    case FLAGGED -> renderingCharacter = "F";
                    case REVEALED -> {
                        if (cell.isMine()) {
                            renderingCharacter = "*";
                            break;
                        }

                        if (cell.getAdjacentMines() > 0) {
                            renderingCharacter = String.valueOf(cell.getAdjacentMines());
                        } else {
                            renderingCharacter = ".";
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + cell.getState());
                }
                IO.print("%s ".formatted(renderingCharacter));
            }
            IO.println();
        }
    }
}
