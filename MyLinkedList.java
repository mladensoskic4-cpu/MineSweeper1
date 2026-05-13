package minesweeper.player;

public class MyLinkedList {

    private NodeMove head;
    private NodeMove tail;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    public void insert(Move move) {
        if (move == null) throw new IllegalArgumentException("Move must not be null.");
        NodeMove newNode = new NodeMove(move);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }
   
    public NodeMove getHead() {
        return head;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
