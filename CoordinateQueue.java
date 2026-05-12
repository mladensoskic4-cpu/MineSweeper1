package minesweeper.core;

public class CoordinateQueue {

    private static class CoordinateNode {
        final int row;
        final int col;
        CoordinateNode next;

        CoordinateNode(int row, int col) {
            this.row = row;
            this.col = col;
            this.next = null;
        }
    }

    private CoordinateNode head; 
    private CoordinateNode tail; 
    private int size;

    public CoordinateQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void enqueue(int row, int col) {
        CoordinateNode newNode = new CoordinateNode(row, col);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public int[] dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot dequeue from an empty queue.");
        }
        int row = head.row;
        int col = head.col;
        head = head.next;
        if (head == null) {
            tail = null; 
        }
        size--;
        return new int[]{row, col};
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}

