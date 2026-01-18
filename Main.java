package com.habittracker;

import com.habittracker.view.LoginView;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Custom UI dengan text button hitam
            UIManager.put("Button.background", new Color(173, 216, 230));
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));
            UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(100, 149, 237)));

            UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));
            UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 12));

        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Smart Habit & Emotion Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setMinimumSize(new Dimension(800, 600));
            frame.setLocationRelativeTo(null);
            frame.getContentPane().add(new LoginView());
            frame.setVisible(true);
        });
    }
}