package com.habittracker.view;

import com.habittracker.controller.*;
import com.habittracker.model.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainView extends JPanel {
    private JTabbedPane tabbedPane;
    private HabitController habitController;
    private MoodController moodController;
    private JPanel habitsListPanel;
    private JLabel progressLabel;

    public MainView() {
        habitController = new HabitController();
        moodController = new MoodController();

        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(240, 248, 255));

        // Add Tabs
        tabbedPane.addTab("🏆 Habits", createHabitPanel());
        tabbedPane.addTab("😊 Mood", createMoodPanel());
        tabbedPane.addTab("📊 Statistics", createStatsPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==================== HEADER PANEL ====================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(70, 130, 180));
        header.setPreferredSize(new Dimension(800, 70));

        // Title
        JLabel titleLabel = new JLabel("Smart Habit & Emotion Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(titleLabel, BorderLayout.CENTER);

        // Logout Button
        JButton logoutButton = createStyledButton("🚪 Logout", new Color(173, 216, 230));
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.addActionListener(e -> logout());
        header.add(logoutButton, BorderLayout.EAST);

        return header;
    }

    // ==================== HABIT PANEL ====================
    private JPanel createHabitPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        topPanel.setBackground(new Color(240, 248, 255));

        // Add Habit Button
        JButton addButton = createStyledButton("➕ Add New Habit", new Color(100, 149, 237));
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.addActionListener(e -> showAddHabitDialog());
        topPanel.add(addButton);

        // Progress Label
        progressLabel = new JLabel();
        updateProgressLabel();
        progressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        progressLabel.setForeground(new Color(70, 130, 180));
        topPanel.add(progressLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        // Habits List Panel
        habitsListPanel = new JPanel();
        habitsListPanel.setLayout(new BoxLayout(habitsListPanel, BoxLayout.Y_AXIS));
        habitsListPanel.setBackground(Color.WHITE);

        refreshHabitsList();

        JScrollPane scrollPane = new JScrollPane(habitsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshHabitsList() {
        if (habitsListPanel == null) return;

        habitsListPanel.removeAll();

        List<Habit> habits = habitController.getAllHabits();

        if (habits.isEmpty()) {
            // Empty State
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

            JLabel emptyLabel = new JLabel(
                    "<html><div style='text-align: center;'>"
                            + "<h2 style='color: #4682B4;'>🌟 No Habits Yet!</h2>"
                            + "<p style='color: #888; margin-top: 10px;'>"
                            + "Start building your daily routine by creating your first habit."
                            + "</p></div></html>"
            );
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JButton createButton = createStyledButton("🎯 Create Your First Habit", new Color(100, 149, 237));
            createButton.setPreferredSize(new Dimension(200, 45));
            createButton.addActionListener(e -> showAddHabitDialog());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(createButton);

            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            emptyPanel.add(buttonPanel, BorderLayout.SOUTH);

            habitsListPanel.add(emptyPanel);
        } else {
            // Display All Habits
            for (Habit habit : habits) {
                habitsListPanel.add(createHabitCard(habit));
                habitsListPanel.add(Box.createVerticalStrut(15));
            }
        }

        habitsListPanel.revalidate();
        habitsListPanel.repaint();
        updateProgressLabel();
    }

    private JPanel createHabitCard(Habit habit) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(700, 110));

        // Left Side: Checkbox and Info
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);

        // Checkbox
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(habit.isCompletedToday());
        checkBox.setBackground(Color.WHITE);
        checkBox.addActionListener(e -> {
            habitController.toggleHabit(habit.getId());
            refreshHabitsList();
        });

        // Habit Info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("📌 " + habit.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(new Color(70, 130, 180));

        JLabel descLabel = new JLabel(habit.getDescription());
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(descLabel);

        leftPanel.add(checkBox, BorderLayout.WEST);
        leftPanel.add(Box.createHorizontalStrut(15), BorderLayout.CENTER);
        leftPanel.add(infoPanel, BorderLayout.EAST);

        // Right Side: Streaks and Delete Button
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // Streaks Panel
        JPanel streakPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        streakPanel.setBackground(Color.WHITE);

        JLabel currentLabel = new JLabel("Current Streak:");
        currentLabel.setFont(new Font("Arial", Font.BOLD, 11));
        currentLabel.setForeground(Color.DARK_GRAY);

        JLabel currentValue = new JLabel(String.valueOf(habit.getCurrentStreak()));
        currentValue.setFont(new Font("Arial", Font.BOLD, 14));
        currentValue.setForeground(new Color(100, 149, 237));
        currentValue.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel bestLabel = new JLabel("Best Streak:");
        bestLabel.setFont(new Font("Arial", Font.BOLD, 11));
        bestLabel.setForeground(Color.DARK_GRAY);

        JLabel bestValue = new JLabel(String.valueOf(habit.getBestStreak()));
        bestValue.setFont(new Font("Arial", Font.BOLD, 14));
        bestValue.setForeground(new Color(30, 144, 255));
        bestValue.setHorizontalAlignment(SwingConstants.CENTER);

        streakPanel.add(currentLabel);
        streakPanel.add(currentValue);
        streakPanel.add(bestLabel);
        streakPanel.add(bestValue);

        // Delete Button
        JButton deleteButton = createStyledButton("🗑 Delete", new Color(255, 200, 200));
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 11));
        deleteButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete '" + habit.getName() + "'?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                habitController.deleteHabit(habit.getId());
                refreshHabitsList();
            }
        });

        rightPanel.add(streakPanel, BorderLayout.CENTER);
        rightPanel.add(deleteButton, BorderLayout.EAST);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private void showAddHabitDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("➕ Create New Habit");
        dialog.setSize(450, 400);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Create New Habit");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialog.add(titleLabel, gbc);

        // Habit Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Habit Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        dialog.add(nameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(descLabel, gbc);

        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setPreferredSize(new Dimension(200, 60));
        dialog.add(descScroll, gbc);

        // Category
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dialog.add(categoryLabel, gbc);

        gbc.gridx = 1;
        String[] categories = {"Wellness", "Fitness", "Productivity", "Learning",
                "Health", "Creativity", "Social", "Finance"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        dialog.add(categoryCombo, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton saveButton = createStyledButton("💾 Save Habit", new Color(100, 149, 237));
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Please enter a habit name!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (name.length() < 3) {
                JOptionPane.showMessageDialog(dialog,
                        "Habit name must be at least 3 characters!",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Habit habit = new Habit(
                    name,
                    descArea.getText().trim(),
                    (String) categoryCombo.getSelectedItem(),
                    getIconForCategory((String) categoryCombo.getSelectedItem()),
                    "#4CAF50"
            );

            habitController.addHabit(habit);

            JOptionPane.showMessageDialog(dialog,
                    "✅ Habit added successfully!\nYou can now track it in your habits list.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dialog.dispose();
            refreshHabitsList();
            tabbedPane.setSelectedIndex(0);
        });

        JButton cancelButton = createStyledButton("❌ Cancel", new Color(200, 200, 200));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
        nameField.requestFocus();
    }

    private String getIconForCategory(String category) {
        switch (category) {
            case "Wellness": return "🧘";
            case "Fitness": return "💪";
            case "Productivity": return "📊";
            case "Learning": return "📚";
            case "Health": return "❤️";
            case "Creativity": return "🎨";
            case "Social": return "👥";
            case "Finance": return "💰";
            default: return "🏆";
        }
    }

    private void updateProgressLabel() {
        if (progressLabel != null) {
            double progress = habitController.getDailyProgress();
            progressLabel.setText(String.format("Today's Progress: %.1f%%", progress));

            // Change color based on progress
            if (progress >= 75) {
                progressLabel.setForeground(new Color(0, 150, 0));
            } else if (progress >= 50) {
                progressLabel.setForeground(new Color(255, 165, 0));
            } else {
                progressLabel.setForeground(new Color(220, 20, 60));
            }
        }
    }

    // ==================== MOOD PANEL ====================
    private JPanel createMoodPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        if (moodController.hasEntryToday()) {
            panel.add(createMoodEntryView(), BorderLayout.CENTER);
        } else {
            panel.add(createMoodInputView(), BorderLayout.CENTER);
        }

        return panel;
    }

    private JPanel createMoodEntryView() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));

        MoodEntry todayEntry = moodController.getTodayEntry();

        JLabel moodLabel = new JLabel("😊 Today's Mood: " + todayEntry.getMood().getDisplay());
        moodLabel.setFont(new Font("Arial", Font.BOLD, 24));
        moodLabel.setForeground(Color.decode(todayEntry.getMood().getColor()));
        moodLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Activities
        JLabel activitiesTitle = new JLabel("🎯 Activities:");
        activitiesTitle.setFont(new Font("Arial", Font.BOLD, 16));
        activitiesTitle.setHorizontalAlignment(SwingConstants.CENTER);

        String activitiesText = todayEntry.getActivities().isEmpty()
                ? "No activities recorded"
                : String.join(" • ", todayEntry.getActivities());
        JLabel activitiesLabel = new JLabel(activitiesText);
        activitiesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        activitiesLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Time
        String time = todayEntry.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        JLabel timeLabel = new JLabel("🕒 Recorded at: " + time);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(moodLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(activitiesTitle);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(activitiesLabel);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(timeLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMoodInputView() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel("😊 How are you feeling today?");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));

        // Mood Selection
        JLabel moodQuestion = new JLabel("Select your mood:");
        moodQuestion.setFont(new Font("Arial", Font.BOLD, 14));
        moodQuestion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(moodQuestion);
        panel.add(Box.createVerticalStrut(15));

        MoodEntry.MoodLevel[] moods = MoodEntry.MoodLevel.values();
        ButtonGroup moodGroup = new ButtonGroup();
        JPanel moodPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        moodPanel.setBackground(Color.WHITE);

        for (MoodEntry.MoodLevel mood : moods) {
            JRadioButton moodButton = new JRadioButton(mood.getDisplay());
            moodButton.setFont(new Font("Arial", Font.PLAIN, 12));
            moodButton.setBackground(Color.WHITE);
            moodButton.setForeground(Color.decode(mood.getColor()));
            moodGroup.add(moodButton);
            moodPanel.add(moodButton);
        }

        // Default selection
        ((JRadioButton)moodPanel.getComponent(2)).setSelected(true); // Select "Meh"

        panel.add(moodPanel);
        panel.add(Box.createVerticalStrut(30));

        // Activities Selection
        JLabel activitiesLabel = new JLabel("🎯 What did you do today? (Select all that apply)");
        activitiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        activitiesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(activitiesLabel);
        panel.add(Box.createVerticalStrut(15));

        String[] activities = {
                "🏃 Exercise", "📚 Read", "🧘 Meditate", "💼 Work",
                "👥 Socialize", "🎮 Gaming", "🍳 Cooking", "🛒 Shopping",
                "📖 Study", "🎵 Music", "🎬 Movies", "🚶 Walk"
        };

        JPanel activitiesPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        activitiesPanel.setBackground(Color.WHITE);
        List<JCheckBox> activityCheckboxes = new ArrayList<>();

        for (String activity : activities) {
            JCheckBox checkBox = new JCheckBox(activity);
            checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
            checkBox.setBackground(Color.WHITE);
            activitiesPanel.add(checkBox);
            activityCheckboxes.add(checkBox);
        }

        JScrollPane activitiesScroll = new JScrollPane(activitiesPanel);
        activitiesScroll.setPreferredSize(new Dimension(500, 150));
        activitiesScroll.setBorder(BorderFactory.createEmptyBorder());
        activitiesScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(activitiesScroll);
        panel.add(Box.createVerticalStrut(30));

        // Save Button
        JButton saveButton = createStyledButton("💾 Save Mood Entry", new Color(100, 149, 237));
        saveButton.setFont(new Font("Arial", Font.BOLD, 16));
        saveButton.setPreferredSize(new Dimension(200, 45));
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action Listener for Save Button
        saveButton.addActionListener(e -> {
            // Get selected mood
            MoodEntry.MoodLevel selectedMood = null;
            for (int i = 0; i < moodPanel.getComponentCount(); i++) {
                JRadioButton button = (JRadioButton) moodPanel.getComponent(i);
                if (button.isSelected()) {
                    selectedMood = moods[i];
                    break;
                }
            }

            if (selectedMood == null) {
                JOptionPane.showMessageDialog(panel,
                        "Please select a mood!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create mood entry
            MoodEntry moodEntry = new MoodEntry(selectedMood);

            // Add selected activities
            for (JCheckBox checkBox : activityCheckboxes) {
                if (checkBox.isSelected()) {
                    String activityText = checkBox.getText();
                    String cleanActivity = activityText.substring(2).trim(); // Remove emoji
                    moodEntry.addActivity(cleanActivity);
                }
            }

            // Save mood entry
            boolean success = moodController.addMoodEntry(moodEntry);

            if (success) {
                JOptionPane.showMessageDialog(panel,
                        "✅ Mood entry saved successfully!\n" +
                                "Your mood: " + selectedMood.getDisplay() + "\n" +
                                "Activities: " + moodEntry.getActivities().size() + " selected",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh mood panel
                refreshMoodPanel();
            } else {
                JOptionPane.showMessageDialog(panel,
                        "❌ You have already made a mood entry today!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saveButton);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private void refreshMoodPanel() {
        tabbedPane.remove(1); // Remove current mood tab
        tabbedPane.insertTab("😊 Mood", null, createMoodPanel(), "Mood Tracker", 1);
        tabbedPane.setSelectedIndex(1); // Select mood tab
    }

    // ==================== STATISTICS PANEL ====================
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("📊 Statistics & Insights");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Main content with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 248, 255));

        // Habit Statistics Section
        contentPanel.add(createHabitStatsSection());
        contentPanel.add(Box.createVerticalStrut(20));

        // Mood Statistics Section
        contentPanel.add(createMoodStatsSection());
        contentPanel.add(Box.createVerticalStrut(20));

        // Insights Section
        contentPanel.add(createInsightsSection());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHabitStatsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("📈 Habit Statistics");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(70, 130, 180));
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sectionTitle);
        panel.add(Box.createVerticalStrut(15));

        // Grid for habit stats
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 15, 15));
        statsGrid.setBackground(Color.WHITE);

        // Daily Progress
        double dailyProgress = habitController.getDailyProgress();
        statsGrid.add(createStatCard("Daily Progress",
                String.format("%.1f%%", dailyProgress),
                "Today's completion rate",
                getProgressColor(dailyProgress)));

        // Total Streak
        int totalStreak = habitController.getTotalStreak();
        statsGrid.add(createStatCard("Total Streak",
                String.valueOf(totalStreak),
                "Active days streak",
                new Color(255, 140, 0)));

        // Total Habits
        int habitCount = habitController.getHabitCount();
        statsGrid.add(createStatCard("Total Habits",
                String.valueOf(habitCount),
                "Active habits",
                new Color(100, 149, 237)));

        // Today's Completions
        int todayCompletions = habitController.getTodayCompletions();
        statsGrid.add(createStatCard("Today's Completions",
                String.valueOf(todayCompletions),
                "Habits completed today",
                new Color(60, 179, 113)));

        panel.add(statsGrid);
        panel.add(Box.createVerticalStrut(15));

        // Additional Stats
        JPanel additionalStats = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        additionalStats.setBackground(Color.WHITE);

        // Weekly Completion Rate
        double weeklyRate = habitController.getWeeklyCompletionRate();
        additionalStats.add(createSmallStatCard("Weekly Rate",
                String.format("%.1f%%", weeklyRate),
                getProgressColor(weeklyRate)));

        // Best Overall Streak
        int bestStreak = habitController.getBestOverallStreak();
        additionalStats.add(createSmallStatCard("Best Streak",
                String.valueOf(bestStreak),
                new Color(255, 69, 0)));

        // Most Productive Category
        String productiveCategory = habitController.getMostProductiveCategory();
        additionalStats.add(createSmallStatCard("Top Category",
                productiveCategory.length() > 10 ? productiveCategory.substring(0, 10) + "..." : productiveCategory,
                new Color(147, 112, 219)));

        panel.add(additionalStats);

        return panel;
    }

    private JPanel createMoodStatsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("😊 Mood Statistics");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(70, 130, 180));
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sectionTitle);
        panel.add(Box.createVerticalStrut(15));

        // Mood Stats Grid
        JPanel statsGrid = new JPanel(new GridLayout(1, 3, 15, 15));
        statsGrid.setBackground(Color.WHITE);

        // Average Mood
        double avgMood = moodController.getAverageMood();
        statsGrid.add(createStatCard("Average Mood",
                String.format("%.1f/5", avgMood),
                "7-day average",
                getMoodColor(avgMood)));

        // Total Mood Entries
        int totalEntries = moodController.getTotalMoodEntries();
        statsGrid.add(createStatCard("Total Entries",
                String.valueOf(totalEntries),
                "Mood records",
                new Color(138, 43, 226)));

        // Good Mood Days
        int goodDays = moodController.getGoodMoodDays();
        statsGrid.add(createStatCard("Good Mood Days",
                String.valueOf(goodDays),
                "Rad/Good days",
                new Color(50, 205, 50)));

        panel.add(statsGrid);
        panel.add(Box.createVerticalStrut(15));

        // Mood Distribution
        JPanel distributionPanel = createMoodDistributionPanel();
        panel.add(distributionPanel);

        return panel;
    }

    private JPanel createMoodDistributionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                "Mood Distribution"
        ));

        Map<MoodEntry.MoodLevel, Integer> distribution = moodController.getMoodDistribution();
        int total = distribution.values().stream().mapToInt(Integer::intValue).sum();

        if (total == 0) {
            JLabel noDataLabel = new JLabel("No mood data available");
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            noDataLabel.setForeground(Color.GRAY);
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noDataLabel);
            return panel;
        }

        for (MoodEntry.MoodLevel mood : MoodEntry.MoodLevel.values()) {
            int count = distribution.getOrDefault(mood, 0);
            double percentage = total > 0 ? (double) count / total * 100 : 0.0;

            JPanel moodRow = new JPanel(new BorderLayout());
            moodRow.setBackground(Color.WHITE);
            moodRow.setMaximumSize(new Dimension(400, 30));

            JLabel moodLabel = new JLabel(mood.getDisplay());
            moodLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            moodLabel.setForeground(Color.decode(mood.getColor()));

            JLabel countLabel = new JLabel(String.format("%d (%.1f%%)", count, percentage));
            countLabel.setFont(new Font("Arial", Font.BOLD, 12));

            moodRow.add(moodLabel, BorderLayout.WEST);
            moodRow.add(countLabel, BorderLayout.EAST);

            panel.add(moodRow);
            panel.add(Box.createVerticalStrut(5));
        }

        return panel;
    }

    private JPanel createInsightsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel sectionTitle = new JLabel("💡 Insights & Recommendations");
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(70, 130, 180));
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sectionTitle);
        panel.add(Box.createVerticalStrut(15));

        // Generate insights
        List<String> insights = generateInsights();

        for (String insight : insights) {
            JPanel insightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            insightPanel.setBackground(Color.WHITE);

            JLabel bullet = new JLabel("•");
            bullet.setFont(new Font("Arial", Font.BOLD, 14));
            bullet.setForeground(new Color(70, 130, 180));

            JLabel insightLabel = new JLabel("<html><div style='width: 350px;'>" + insight + "</div></html>");
            insightLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            insightPanel.add(bullet);
            insightPanel.add(insightLabel);

            panel.add(insightPanel);
            panel.add(Box.createVerticalStrut(8));
        }

        return panel;
    }

    private List<String> generateInsights() {
        List<String> insights = new ArrayList<>();

        // Habit insights
        double progress = habitController.getDailyProgress();
        if (progress >= 80) {
            insights.add("🎯 Excellent! You've completed most of your habits today. Keep up the great work!");
        } else if (progress >= 50) {
            insights.add("👍 Good progress! You're halfway there. Try to complete a few more habits today.");
        } else if (progress > 0) {
            insights.add("💪 You've started! Every small step counts. Focus on completing at least one more habit.");
        } else {
            insights.add("🌟 Start your day by completing at least one habit. Small wins lead to big results!");
        }

        // Streak insights
        int bestStreak = habitController.getBestOverallStreak();
        if (bestStreak >= 7) {
            insights.add("🔥 Amazing consistency! You've maintained habits for " + bestStreak + " days straight.");
        } else if (bestStreak >= 3) {
            insights.add("📈 Building momentum! Your best streak is " + bestStreak + " days. Try to beat it!");
        }

        // Mood insights
        double avgMood = moodController.getAverageMood();
        if (avgMood >= 4) {
            insights.add("😊 Your mood has been positive recently. Great job maintaining emotional well-being!");
        } else if (avgMood >= 3) {
            insights.add("🤔 Your mood has been average. Consider adding more enjoyable activities to your day.");
        } else if (avgMood > 0) {
            insights.add("💭 Your mood could use a boost. Try practicing gratitude or doing something you love.");
        }

        // Completion insights
        int habitCount = habitController.getHabitCount();
        if (habitCount > 5) {
            insights.add("📋 You have " + habitCount + " active habits. Consider focusing on your top 3-5 priorities.");
        } else if (habitCount == 0) {
            insights.add("🎯 Start by adding 1-2 simple habits. Consistency is more important than quantity.");
        }

        // Ensure at least 3 insights
        while (insights.size() < 3) {
            insights.add("💡 Track your habits daily to build consistency and achieve your goals!");
        }

        return insights;
    }

    private JPanel createStatCard(String title, String value, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(valueLabel, BorderLayout.CENTER);
        centerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSmallStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 11));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);

        return card;
    }

    private Color getProgressColor(double progress) {
        if (progress >= 75) return new Color(0, 150, 0);
        if (progress >= 50) return new Color(255, 165, 0);
        return new Color(220, 20, 60);
    }

    private Color getMoodColor(double mood) {
        if (mood >= 4) return new Color(76, 175, 80);
        if (mood >= 3) return new Color(255, 193, 7);
        if (mood > 0) return new Color(244, 67, 54);
        return Color.GRAY;
    }

    // ==================== UTILITY METHODS ====================
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void logout() {
        // Save data and cleanup
        habitController.shutdown();

        // Return to login screen
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame != null) {
            topFrame.getContentPane().removeAll();
            topFrame.getContentPane().add(new LoginView());
            topFrame.revalidate();
            topFrame.repaint();
        }
    }
}