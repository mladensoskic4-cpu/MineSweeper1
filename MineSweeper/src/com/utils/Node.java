package com.utils;

public class Node<T> {
    private final T data;
    public Node<T> next;

    public Node(T data) {
        this.data = data;
        this.next = null;
    }

    public T getData() {
        return data;
    }
}