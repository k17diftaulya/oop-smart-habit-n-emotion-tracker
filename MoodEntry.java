package com.habittracker.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MoodEntry implements Serializable {
    public enum MoodLevel {
        RAD(5, "😊 Rad", "#4CAF50"),
        GOOD(4, "🙂 Good", "#8BC34A"),
        MEH(3, "😐 Meh", "#FFC107"),
        BAD(2, "😔 Bad", "#FF9800"),
        AWFUL(1, "😢 Awful", "#F44336");

        private final int value;
        private final String display;
        private final String color;

        MoodLevel(int value, String display, String color) {
            this.value = value;
            this.display = display;
            this.color = color;
        }

        public int getValue() { return value; }
        public String getDisplay() { return display; }
        public String getColor() { return color; }
    }

    private String id;
    private MoodLevel mood;
    private List<String> activities;
    private LocalDateTime timestamp;
    private String notes;

    public MoodEntry(MoodLevel mood) {
        this.id = java.util.UUID.randomUUID().toString();
        this.mood = mood;
        this.activities = new ArrayList<>();
        this.timestamp = LocalDateTime.now();
    }

    public void addActivity(String activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public void removeActivity(String activity) {
        activities.remove(activity);
    }

    public String getId() { return id; }
    public MoodLevel getMood() { return mood; }
    public void setMood(MoodLevel mood) { this.mood = mood; }
    public List<String> getActivities() { return activities; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}