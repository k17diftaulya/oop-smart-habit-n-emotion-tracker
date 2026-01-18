package com.habittracker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData implements Serializable {
    private User currentUser;
    private List<Habit> habits;
    private List<MoodEntry> moodEntries;
    private Map<String, List<String>> customActivities;

    public AppData() {
        this.habits = new ArrayList<>();
        this.moodEntries = new ArrayList<>();
        this.customActivities = new HashMap<>();
    }

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }
    public List<Habit> getHabits() { return habits; }
    public void setHabits(List<Habit> habits) { this.habits = habits; }
    public List<MoodEntry> getMoodEntries() { return moodEntries; }
    public void setMoodEntries(List<MoodEntry> moodEntries) { this.moodEntries = moodEntries; }
    public Map<String, List<String>> getCustomActivities() { return customActivities; }
    public void setCustomActivities(Map<String, List<String>> customActivities) {
        this.customActivities = customActivities;
    }

    public void addHabit(Habit habit) { habits.add(habit); }
    public void removeHabit(String habitId) { habits.removeIf(h -> h.getId().equals(habitId)); }
    public Habit getHabitById(String habitId) {
        return habits.stream()
                .filter(h -> h.getId().equals(habitId))
                .findFirst()
                .orElse(null);
    }
    public void addMoodEntry(MoodEntry entry) { moodEntries.add(entry); }
    public MoodEntry getTodayMoodEntry() {
        return moodEntries.stream()
                .filter(e -> e.getTimestamp().toLocalDate().equals(java.time.LocalDate.now()))
                .findFirst()
                .orElse(null);
    }
    public boolean hasMoodEntryToday() { return getTodayMoodEntry() != null; }
}