package com.habittracker.view;

import com.habittracker.controller.AuthController;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel messageLabel;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController();
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(500, 100));
        JLabel titleLabel = new JLabel("Smart Habit & Emotion Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        contentPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        contentPanel.add(passwordField, gbc);

        // Show Password
        gbc.gridx = 2; gbc.gridy = 1;
        JCheckBox showPass = new JCheckBox("Show");
        showPass.setBackground(new Color(240, 248, 255));
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) passwordField.setEchoChar((char) 0);
            else passwordField.setEchoChar('•');
        });
        contentPanel.add(showPass, gbc);

        // Remember Me
        gbc.gridx = 1; gbc.gridy = 2;
        JCheckBox remember = new JCheckBox("Remember Me");
        remember.setBackground(new Color(240, 248, 255));
        contentPanel.add(remember, gbc);

        // Buttons - TEXT HITAM
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255));

        loginButton = new JButton("Login");
        styleButton(loginButton, new Color(173, 216, 230)); // Light blue background
        loginButton.addActionListener(e -> handleLogin());

        registerButton = new JButton("Register");
        styleButton(registerButton, new Color(173, 216, 230)); // Light blue background
        registerButton.addActionListener(e -> showRegisterDialog());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        contentPanel.add(buttonPanel, gbc);

        // Message
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        messageLabel.setForeground(Color.RED);
        contentPanel.add(messageLabel, gbc);

        // Forgot Password
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        JButton forgotBtn = new JButton("Forgot Password?");
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setForeground(new Color(70, 130, 180));
        contentPanel.add(forgotBtn, gbc);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    // STYLE BUTTON DENGAN TEXT HITAM
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK); // TEXT HITAM
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setForeground(Color.BLACK); // Tetap hitam saat hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setForeground(Color.BLACK); // Tetap hitam
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields!");
            return;
        }

        if (authController.login(username, password)) {
            messageLabel.setText("Login successful!");
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.getContentPane().removeAll();
            topFrame.getContentPane().add(new MainView());
            topFrame.revalidate();
            topFrame.repaint();
        } else {
            messageLabel.setText("Invalid username or password!");
        }
    }

    private void showRegisterDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Register");
        dialog.setSize(400, 350);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        JTextField userField = new JTextField(15);
        dialog.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        dialog.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        JTextField emailField = new JTextField(15);
        dialog.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel();

        JButton regBtn = new JButton("Register");
        styleButton(regBtn, new Color(173, 216, 230)); // TEXT HITAM
        regBtn.addActionListener(e -> {
            if (authController.register(
                    userField.getText().trim(),
                    new String(passField.getPassword()),
                    emailField.getText().trim()
            )) {
                JOptionPane.showMessageDialog(dialog, "Registration successful!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Registration failed!");
            }
        });

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(200, 200, 200)); // Gray background, TEXT HITAM
        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(regBtn);
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, gbc);

        dialog.setVisible(true);
    }
}