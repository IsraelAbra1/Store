package com.example.fbstore;

public class MyRecord {
    private String name;
    private int score;

    public MyRecord(String name, int score) {
        this.name = name;
        this.score = score;
    }

    // MUST have the constructor  for the FireBase
    public MyRecord() {
    }

    // MUST generate getters and setters for the FireBase
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
