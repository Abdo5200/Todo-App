package com.example.todolist.controller;

import com.example.todolist.DTO.LoginRequest;
import com.example.todolist.DTO.LoginResponse;
import com.example.todolist.DTO.SignupRequest;
import com.example.todolist.DTO.SignupResponse;
import com.example.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> postSignUp(@Valid @RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = userService.registerUser(signupRequest);
        if (signupResponse.isSuccess())
            return ResponseEntity.ok(signupResponse);
        else
            return ResponseEntity.badRequest().body(signupResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> postLogin(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.authenticateUser(loginRequest);
        if (loginResponse.isSuccess())
            return ResponseEntity.ok(loginResponse);
        else
            return ResponseEntity.badRequest().body(loginResponse);
    }


}
