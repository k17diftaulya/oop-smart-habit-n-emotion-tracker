package com.habittracker.controller;

import com.habittracker.service.HabitService;
import com.habittracker.model.Habit;
import java.util.List;

public class HabitController {
    private HabitService habitService;
    public HabitController() { this.habitService = new HabitService(); }
    public List<Habit> getAllHabits() { return habitService.getAllHabits(); }
    public void addHabit(Habit habit) { habitService.addHabit(habit); }
    public void toggleHabit(String habitId) { habitService.toggleHabit(habitId); }
    public void deleteHabit(String habitId) { habitService.deleteHabit(habitId); }
    public double getDailyProgress() { return habitService.getDailyProgress(); }
    public int getTotalStreak() { return habitService.getAllHabits().stream().mapToInt(Habit::getCurrentStreak).sum(); }
    public int getHabitCount() { return habitService.getAllHabits().size(); }
    public void shutdown() { habitService.shutdown(); }

    public int getTodayCompletions() {
        return habitService.getTodayCompletions();
    }

    public int getTotalCompletions() {
        return habitService.getTotalCompletions();
    }

    public double getWeeklyCompletionRate() {
        return habitService.getWeeklyCompletionRate();
    }
    public int getBestOverallStreak() {
        return habitService.getBestOverallStreak();
    }

    public Habit getHabitWithLongestStreak() {
        return habitService.getHabitWithLongestStreak();
    }

    public String getMostProductiveCategory() {
        return habitService.getMostProductiveCategory();
    }
}