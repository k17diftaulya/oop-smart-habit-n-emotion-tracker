package com.habittracker.model;

public abstract class Tracker {
    protected String userId;

    public Tracker(String userId) {
        this.userId = userId;
    }

    public String getUserId() { return userId; }

    public abstract void saveToFile();
    public abstract void loadFromFile();
}