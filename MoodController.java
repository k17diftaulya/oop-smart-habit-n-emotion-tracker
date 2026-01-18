package com.habittracker.controller;

import com.habittracker.service.MoodService;
import com.habittracker.model.MoodEntry;

public class MoodController {
    private MoodService moodService;
    public MoodController() { this.moodService = new MoodService(); }
    public boolean addMoodEntry(MoodEntry entry) { return moodService.addMoodEntry(entry); }
    public boolean hasEntryToday() { return moodService.hasEntryToday(); }
    public MoodEntry getTodayEntry() { return moodService.getTodayEntry(); }
    public double getAverageMood() { return moodService.getAverageMoodLast7Days(); }

    public int getTotalMoodEntries() {
        return moodService.getTotalMoodEntries();
    }

    public int getGoodMoodDays() {
        return moodService.getGoodMoodDays();
    }

    public MoodEntry.MoodLevel getMostFrequentMood() {
        return moodService.getMostFrequentMood();
    }

    public java.util.Map<MoodEntry.MoodLevel, Integer> getMoodDistribution() {
        return moodService.getMoodDistribution();
    }
}