package com.habittracker.service;

import com.habittracker.model.*;
import java.security.MessageDigest;

public class AuthService {
    private DataService dataService;

    public AuthService() { this.dataService = new DataService(); }

    public boolean register(String username, String password, String email) {
        if (username == null || username.trim().isEmpty() ||
                password == null || password.length() < 6 ||
                email == null || !email.contains("@")) {
            return false;
        }

        String passwordHash = hashPassword(password);
        User newUser = new User(username, passwordHash, email);
        AppData data = dataService.loadData();

        if (data.getCurrentUser() != null) return false;

        data.setCurrentUser(newUser);

        try {
            dataService.saveData(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean login(String username, String password) {
        AppData data = dataService.loadData();
        User user = data.getCurrentUser();
        if (user == null) return false;

        String inputHash = hashPassword(password);
        return user.getUsername().equals(username) &&
                user.getPasswordHash().equals(inputHash);
    }

    public boolean isLoggedIn() {
        AppData data = dataService.loadData();
        return data.getCurrentUser() != null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}