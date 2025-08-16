package com.example.todolist.service;

import com.example.todolist.DTO.LoginRequest;
import com.example.todolist.DTO.LoginResponse;
import com.example.todolist.DTO.SignupRequest;
import com.example.todolist.DTO.SignupResponse;
import com.example.todolist.entity.User;

public interface UserService {
    
    User findById(Integer id);

    SignupResponse registerUser(SignupRequest signupRequest);

    LoginResponse authenticateUser(LoginRequest loginRequest);
}
