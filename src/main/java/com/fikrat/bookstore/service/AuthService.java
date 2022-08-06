package com.fikrat.bookstore.service;

import com.fikrat.bookstore.dto.auth.JwtResponse;
import com.fikrat.bookstore.dto.auth.LoginRequest;
import com.fikrat.bookstore.dto.auth.RegisterRequest;
import com.fikrat.bookstore.model.User;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    User registerAsPublisher(RegisterRequest request);
    User registerAsUser(RegisterRequest request);
}
