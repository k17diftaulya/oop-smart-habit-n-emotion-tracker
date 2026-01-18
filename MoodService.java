package com.habittracker.service;

import com.habittracker.model.*;
import java.time.LocalDate;
import java.util.List;

public class MoodService {
    private DataService dataService;
    private AppData appData;

    public MoodService() {
        this.dataService = new DataService();
        this.appData = dataService.loadData();
    }

    public boolean addMoodEntry(MoodEntry entry) {
        if (hasEntryToday()) {
            return false;
        }

        appData.addMoodEntry(entry);
        try {
            dataService.saveData(appData);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasEntryToday() {
        return getTodayEntry() != null;
    }

    public MoodEntry getTodayEntry() {
        LocalDate today = LocalDate.now();
        return appData.getMoodEntries().stream()
                .filter(e -> e.getTimestamp().toLocalDate().equals(today))
                .findFirst()
                .orElse(null);
    }

    public List<MoodEntry> getAllEntries() {
        return appData.getMoodEntries();
    }

    /**
     * Menghitung rata-rata mood dalam 7 hari terakhir
     * Return 0.0 jika tidak ada data
     */
    public double getAverageMoodLast7Days() {
        List<MoodEntry> entries = appData.getMoodEntries();
        if (entries.isEmpty()) {
            return 0.0;
        }

        LocalDate weekAgo = LocalDate.now().minusDays(7);

        // Filter entries dalam 7 hari terakhir
        List<MoodEntry> recentEntries = entries.stream()
                .filter(e -> !e.getTimestamp().toLocalDate().isBefore(weekAgo))
                .toList();

        if (recentEntries.isEmpty()) {
            return 0.0;
        }

        // Hitung rata-rata
        double sum = recentEntries.stream()
                .mapToInt(e -> e.getMood().getValue())
                .sum();

        return sum / recentEntries.size();
    }

    /**
     * Mendapatkan distribusi mood (jumlah untuk setiap jenis mood)
     */
    public java.util.Map<MoodEntry.MoodLevel, Integer> getMoodDistribution() {
        java.util.Map<MoodEntry.MoodLevel, Integer> distribution = new java.util.HashMap<>();

        // Inisialisasi semua mood dengan 0
        for (MoodEntry.MoodLevel mood : MoodEntry.MoodLevel.values()) {
            distribution.put(mood, 0);
        }

        // Hitung jumlah untuk setiap mood
        for (MoodEntry entry : appData.getMoodEntries()) {
            distribution.put(entry.getMood(), distribution.get(entry.getMood()) + 1);
        }

        return distribution;
    }

    /**
     * Mendapatkan mood yang paling sering muncul
     */
    public MoodEntry.MoodLevel getMostFrequentMood() {
        java.util.Map<MoodEntry.MoodLevel, Integer> distribution = getMoodDistribution();

        return distribution.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse(MoodEntry.MoodLevel.MEH);
    }

    /**
     * Mendapatkan jumlah hari dengan mood baik (Rad atau Good)
     */
    public int getGoodMoodDays() {
        return (int) appData.getMoodEntries().stream()
                .filter(e -> e.getMood() == MoodEntry.MoodLevel.RAD || e.getMood() == MoodEntry.MoodLevel.GOOD)
                .count();
    }

    /**
     * Mendapatkan jumlah total entries mood
     */
    public int getTotalMoodEntries() {
        return appData.getMoodEntries().size();
    }
}