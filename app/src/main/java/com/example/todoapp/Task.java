package com.example.todoapp;

public class Task {
    private String text;
    private boolean isDone;

    public Task(String text) {
        this.text = text;
        this.isDone = false;
    }

    public String getText() {
        return text;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
