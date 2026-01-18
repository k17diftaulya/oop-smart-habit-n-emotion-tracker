package com.habittracker.controller;

import com.habittracker.service.AuthService;

public class AuthController {
    private AuthService authService;
    public AuthController() { this.authService = new AuthService(); }
    public boolean login(String username, String password) { return authService.login(username, password); }
    public boolean register(String username, String password, String email) { return authService.register(username, password, email); }
    public boolean isLoggedIn() { return authService.isLoggedIn(); }
}