package minesweeper.core;
import java.util.Random;
public class Board {

    
    private static final int[] DX = {-1, -1, -1,  0,  0,  1,  1,  1};
    private static final int[] DY = {-1,  0,  1, -1,  1, -1,  0,  1};

    private final int size;
    private final Cell[][] grid;

    public Board(int size, int numMines) {
        if (size <= 0) {
            throw new IllegalArgumentException(
                "Grid size must be a positive integer, but was: " + size);
        }
        if (numMines < 0 || numMines >= size * size) {
            throw new IllegalArgumentException(
                "numMines must be in [0, size*size - 1], but was: " + numMines
                + " for a " + size + "x" + size + " grid.");
        }

        this.size = size;
        this.grid = new Cell[size][size];

        initializeGrid();
        placeMines(numMines);
        calculateAdjacentMines();
    }

    private void initializeGrid() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                grid[r][c] = new Cell(false);
            }
        }
    }

    private void placeMines(int numMines) {
        Random rng = new Random();
        int placed = 0;
        while (placed < numMines) {
            int r = rng.nextInt(size);
            int c = rng.nextInt(size);
            if (!grid[r][c].isMine()) {
                grid[r][c] = new Cell(true);
                placed++;
            }
        }
    }
    private void calculateAdjacentMines() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (!grid[r][c].isMine()) {
                    int count = 0;
                    for (int d = 0; d < 8; d++) {
                        int nr = r + DX[d];
                        int nc = c + DY[d];
                        if (inBounds(nr, nc) && grid[nr][nc].isMine()) {
                            count++;
                        }
                    }
                    grid[r][c].setAdjacentMines(count);
                }
            }
        }
    }
    public void revealCell(int row, int col) {
        if (!inBounds(row, col)) return;

        Cell target = grid[row][col];
        if (target.getState() != CellState.HIDDEN) return;

        target.setState(CellState.REVEALED);

        if (target.isMine() || target.getAdjacentMines() > 0) return;
        CoordinateQueue queue = new CoordinateQueue();
        queue.enqueue(row, col);

        while (!queue.isEmpty()) {
            int[] coord = queue.dequeue();
            int r = coord[0];
            int c = coord[1];

            for (int d = 0; d < 8; d++) {
                int nr = r + DX[d];
                int nc = c + DY[d];

                if (!inBounds(nr, nc)) continue;
                Cell neighbor = grid[nr][nc];
                if (neighbor.getState() != CellState.HIDDEN) continue;
                if (neighbor.isMine()) continue;

                neighbor.setState(CellState.REVEALED);

                if (neighbor.getAdjacentMines() == 0) {
                    queue.enqueue(nr, nc);
                }
            }
        }
    }

    public GameOutcome getGameState() {
        boolean allSafeRevealed = true;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Cell cell = grid[r][c];
                if (cell.isMine() && cell.getState() == CellState.REVEALED) {
                    return GameOutcome.DEFEAT;
                }
                if (!cell.isMine() && cell.getState() != CellState.REVEALED) {
                    allSafeRevealed = false;
                }
            }
        }

        return allSafeRevealed ? GameOutcome.VICTORY : GameOutcome.IN_PROGRESS;
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    private boolean inBounds(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }
}

