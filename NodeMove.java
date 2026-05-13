package minesweeper.player;

public class NodeMove {
    private final Move data;
    NodeMove next; 

    public NodeMove(Move data) {
        this.data = data;
        this.next = null;
    }

    public Move getData() {
        return data;
    }
}