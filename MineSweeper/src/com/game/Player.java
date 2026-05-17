package com.game;

import com.utils.LinkedList;

import java.util.Random;

public class Player {
    Random rand;
    Board board;
    LinkedList<Move> moveHistory;

    public Player(Board board) {
        this.board = board;
        rand = new Random();
        moveHistory = new LinkedList<>();
    }

    public GameOutcome playTurn() {
        int boardSize = board.getSize();
        do {
            int row = rand.nextInt(boardSize);
            int column = rand.nextInt(boardSize);

            MoveResult moveResult = this.board.revealCell(row, column);

            if(moveResult == MoveResult.REVEALED_EMPTY || moveResult == MoveResult.REVEALED_MINE) {
                moveHistory.append(new Move(row, column, moveResult == MoveResult.REVEALED_EMPTY));
                break;
            };
        } while(true);

        return board.getGameState();
    }

    public void printBoard() {
        board.printBoard();
    }

    public int getNumberOfMoves() {
        return this.moveHistory.size();
    }
}
