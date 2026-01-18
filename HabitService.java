package com.habittracker.service;

import com.habittracker.model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.temporal.ChronoUnit;

public class HabitService {
    private DataService dataService;
    private AppData appData;
    private ScheduledExecutorService scheduler;

    public HabitService() {
        this.dataService = new DataService();
        this.appData = dataService.loadData();
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduleDailyReset();
    }

    public List<Habit> getAllHabits() { return appData.getHabits(); }

    public void addHabit(Habit habit) {
        appData.addHabit(habit);
        saveData();
    }

    public void toggleHabit(String habitId) {
        Habit habit = appData.getHabitById(habitId);
        if (habit != null) {
            if (habit.isCompletedToday()) habit.unmarkToday();
            else habit.markCompleted();
            saveData();
        }
    }

    public void deleteHabit(String habitId) {
        appData.removeHabit(habitId);
        saveData();
    }

    public double getDailyProgress() {
        List<Habit> habits = getAllHabits();
        if (habits.isEmpty()) return 0.0;

        long completedCount = habits.stream()
                .filter(Habit::isCompletedToday)
                .count();

        return (double) completedCount / habits.size() * 100;
    }

    private void scheduleDailyReset() {
        long initialDelay = calculateInitialDelay();
        long period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(() -> {
            // Reset logic
            System.out.println("Daily reset at: " + LocalDate.now());
        }, initialDelay, period, TimeUnit.SECONDS);
    }

    private long calculateInitialDelay() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        return java.time.Duration.between(
                java.time.LocalDateTime.now(),
                tomorrow.atStartOfDay()
        ).getSeconds();
    }

    private void saveData() {
        try { dataService.saveData(appData); } catch (Exception e) { e.printStackTrace(); }
    }

    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) scheduler.shutdown();
    }

    public int getTotalStreak() {
        return appData.getHabits().stream()
                .mapToInt(Habit::getCurrentStreak)
                .sum();
    }

    public double getAverageStreak() {
        if (appData.getHabits().isEmpty()) {
            return 0.0;
        }

        return appData.getHabits().stream()
                .mapToInt(Habit::getCurrentStreak)
                .average()
                .orElse(0.0);
    }

    public int getBestOverallStreak() {
        return appData.getHabits().stream()
                .mapToInt(Habit::getBestStreak)
                .max()
                .orElse(0);
    }

    public Habit getHabitWithLongestStreak() {
        return appData.getHabits().stream()
                .max(java.util.Comparator.comparingInt(Habit::getCurrentStreak))
                .orElse(null);
    }

    public Habit getMostCompletedHabit() {
        return appData.getHabits().stream()
                .max(java.util.Comparator.comparingInt(h -> h.getCompletedDates().size()))
                .orElse(null);
    }

    public int getTodayCompletions() {
        LocalDate today = LocalDate.now();
        return (int) appData.getHabits().stream()
                .filter(h -> h.getCompletedDates().contains(today))
                .count();
    }

    public int getTotalCompletions() {
        return appData.getHabits().stream()
                .mapToInt(h -> h.getCompletedDates().size())
                .sum();
    }

    public double getWeeklyCompletionRate() {
        if (appData.getHabits().isEmpty()) {
            return 0.0;
        }

        LocalDate weekStart = LocalDate.now().minusDays(6); // 7 hari termasuk hari ini
        LocalDate today = LocalDate.now();

        int totalPossible = appData.getHabits().size() * 7; // 7 hari
        int actualCompletions = 0;

        for (Habit habit : appData.getHabits()) {
            for (LocalDate date = weekStart; !date.isAfter(today); date = date.plusDays(1)) {
                if (habit.getCompletedDates().contains(date)) {
                    actualCompletions++;
                }
            }
        }
        return totalPossible > 0 ? (double) actualCompletions / totalPossible * 100 : 0.0;
    }

    public java.util.Map<LocalDate, Integer> getCompletionTrend() {
        java.util.Map<LocalDate, Integer> trend = new java.util.LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        // Isi 7 hari terakhir
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            trend.put(date, 0);
        }

        // Hitung penyelesaian per hari
        for (Habit habit : appData.getHabits()) {
            for (LocalDate date : habit.getCompletedDates()) {
                if (trend.containsKey(date)) {
                    trend.put(date, trend.get(date) + 1);
                }
            }
        }
        return trend;
    }

    public String getMostProductiveCategory() {
        if (appData.getHabits().isEmpty()) {
            return "No habits";
        }

        java.util.Map<String, Integer> categoryCompletions = new java.util.HashMap<>();

        for (Habit habit : appData.getHabits()) {
            String category = habit.getCategory();
            int completions = habit.getCompletedDates().size();
            categoryCompletions.put(category, categoryCompletions.getOrDefault(category, 0) + completions);
        }

        return categoryCompletions.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse("Unknown");
    }
}