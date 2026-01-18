package com.habittracker.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Habit implements Serializable {
    private String id;
    private String name;
    private String description;
    private String category;
    private String icon;
    private String color;
    private LocalDate createdDate;
    private Set<LocalDate> completedDates;
    private int currentStreak;
    private int bestStreak;

    public Habit(String name, String description, String category, String icon, String color) {
        this.id = java.util.UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.category = category;
        this.icon = icon;
        this.color = color;
        this.createdDate = LocalDate.now();
        this.completedDates = new HashSet<>();
        this.currentStreak = 0;
        this.bestStreak = 0;
    }

    /**
     * Menandai habit sebagai selesai untuk hari ini
     */
    public void markCompleted() {
        LocalDate today = LocalDate.now();
        if (!completedDates.contains(today)) {
            completedDates.add(today);
            updateStreaks();
        }
    }

    /**
     * Menghapus centang untuk hari ini
     */
    public void unmarkToday() {
        completedDates.remove(LocalDate.now());
        updateStreaks();
    }

    /**
     * Mengecek apakah habit sudah diselesaikan hari ini
     */
    public boolean isCompletedToday() {
        return completedDates.contains(LocalDate.now());
    }

    /**
     * Menghitung dan mengupdate streak
     */
    private void updateStreaks() {
        this.currentStreak = calculateCurrentStreak();
        this.bestStreak = calculateBestStreak();
    }

    /**
     * Menghitung streak saat ini (hari beruntun terakhir)
     */
    private int calculateCurrentStreak() {
        if (completedDates.isEmpty()) {
            return 0;
        }

        LocalDate today = LocalDate.now();
        int streak = 0;
        LocalDate checkDate = today;

        // Cek berapa hari berturut-turut ke belakang
        while (completedDates.contains(checkDate)) {
            streak++;
            checkDate = checkDate.minusDays(1);
        }

        return streak;
    }

    /**
     * Menghitung streak terbaik sepanjang waktu
     */
    private int calculateBestStreak() {
        if (completedDates.isEmpty()) {
            return 0;
        }

        // Konversi ke TreeSet untuk pengurutan
        TreeSet<LocalDate> sortedDates = new TreeSet<>(completedDates);

        int bestStreak = 1;
        int currentStreak = 1;
        LocalDate previousDate = null;

        for (LocalDate date : sortedDates) {
            if (previousDate != null) {
                long daysBetween = ChronoUnit.DAYS.between(previousDate, date);

                if (daysBetween == 1) {
                    // Tanggal berurutan
                    currentStreak++;
                    if (currentStreak > bestStreak) {
                        bestStreak = currentStreak;
                    }
                } else if (daysBetween > 1) {
                    // Ada jeda, reset streak
                    currentStreak = 1;
                }
                // Jika daysBetween == 0, tanggal sama (tidak mungkin dengan Set)
            }
            previousDate = date;
        }

        return Math.max(bestStreak, currentStreak);
    }

    /**
     * Menghitung persentase penyelesaian sejak habit dibuat
     */
    public double getCompletionPercentage() {
        if (createdDate == null) {
            return 0.0;
        }

        long totalDays = ChronoUnit.DAYS.between(createdDate, LocalDate.now()) + 1;
        if (totalDays <= 0) {
            return 0.0;
        }

        long completedCount = completedDates.stream()
                .filter(date -> !date.isAfter(LocalDate.now()))
                .count();

        return (double) completedCount / totalDays * 100;
    }

    /**
     * Mengecek apakah habit diselesaikan pada tanggal tertentu
     */
    public boolean isCompletedOnDate(LocalDate date) {
        return completedDates.contains(date);
    }

    /**
     * Menghapus semua data penyelesaian
     */
    public void clearAllCompletions() {
        completedDates.clear();
        updateStreaks();
    }

    /**
     * Menghapus penyelesaian pada tanggal tertentu
     */
    public void removeCompletion(LocalDate date) {
        completedDates.remove(date);
        updateStreaks();
    }

    /**
     * Menambah penyelesaian pada tanggal tertentu
     */
    public void addCompletion(LocalDate date) {
        if (date != null && !date.isAfter(LocalDate.now())) {
            completedDates.add(date);
            updateStreaks();
        }
    }

    /**
     * Mendapatkan jumlah penyelesaian dalam periode tertentu
     */
    public int getCompletionCountBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return 0;
        }

        return (int) completedDates.stream()
                .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate))
                .count();
    }

    /**
     * Mendapatkan jumlah hari sejak terakhir diselesaikan
     */
    public long getDaysSinceLastCompletion() {
        if (completedDates.isEmpty()) {
            return -1; // Belum pernah diselesaikan
        }

        TreeSet<LocalDate> sortedDates = new TreeSet<>(completedDates);
        LocalDate lastDate = sortedDates.last();

        return ChronoUnit.DAYS.between(lastDate, LocalDate.now());
    }

    /**
     * Mendapatkan tanggal penyelesaian pertama
     */
    public LocalDate getFirstCompletionDate() {
        if (completedDates.isEmpty()) {
            return null;
        }

        TreeSet<LocalDate> sortedDates = new TreeSet<>(completedDates);
        return sortedDates.first();
    }

    /**
     * Mendapatkan tanggal penyelesaian terakhir
     */
    public LocalDate getLastCompletionDate() {
        if (completedDates.isEmpty()) {
            return null;
        }

        TreeSet<LocalDate> sortedDates = new TreeSet<>(completedDates);
        return sortedDates.last();
    }

    // ==================== GETTERS & SETTERS ====================

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Set<LocalDate> getCompletedDates() {
        return new HashSet<>(completedDates);
    }

    public int getCurrentStreak() {
        return calculateCurrentStreak(); // Selalu hitung secara real-time
    }

    public int getBestStreak() {
        return calculateBestStreak(); // Selalu hitung secara real-time
    }

    /**
     * Getter kompatibilitas untuk UI
     */
    public boolean getCompletedStatus() {
        return isCompletedToday();
    }

    /**
     * Mendapatkan total jumlah penyelesaian
     */
    public int getTotalCompletions() {
        return completedDates.size();
    }

    /**
     * Mendapatkan usia habit dalam hari
     */
    public long getAgeInDays() {
        if (createdDate == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(createdDate, LocalDate.now());
    }

    /**
     * Mendapatkan konsistensi habit (persentase penyelesaian sejak dibuat)
     */
    public double getConsistency() {
        return getCompletionPercentage();
    }

    /**
     * Mengecek apakah habit aktif (diselesaikan dalam 7 hari terakhir)
     */
    public boolean isActive() {
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        return completedDates.stream()
                .anyMatch(date -> !date.isBefore(weekAgo));
    }

    /**
     * Mendapatkan frekuensi penyelesaian per minggu
     */
    public double getWeeklyFrequency() {
        long ageInWeeks = getAgeInDays() / 7;
        if (ageInWeeks == 0) {
            ageInWeeks = 1;
        }

        return (double) completedDates.size() / ageInWeeks;
    }

    // ==================== TO STRING & EQUALS ====================

    @Override
    public String toString() {
        return String.format(
                "Habit{id='%s', name='%s', category='%s', currentStreak=%d, bestStreak=%d, completions=%d}",
                id.substring(0, 8), name, category, getCurrentStreak(), getBestStreak(), completedDates.size()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habit habit = (Habit) obj;
        return id.equals(habit.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Method untuk debugging
     */
    public String getDebugInfo() {
        return String.format(
                "Habit: %s\n" +
                        "Created: %s\n" +
                        "Total Completions: %d\n" +
                        "Current Streak: %d\n" +
                        "Best Streak: %d\n" +
                        "Completion Percentage: %.1f%%\n" +
                        "Active: %s\n" +
                        "Weekly Frequency: %.2f times/week",
                name,
                createdDate,
                completedDates.size(),
                getCurrentStreak(),
                getBestStreak(),
                getCompletionPercentage(),
                isActive(),
                getWeeklyFrequency()
        );
    }
}