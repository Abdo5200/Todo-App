package com.example.todolist.controller;

import com.example.todolist.DTO.*;
import com.example.todolist.service.UserService;
import com.example.todolist.service.impl.PasswordResetService;
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
    private final PasswordResetService passwordResetService;

    @Autowired
    public AuthController(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
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

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> postForgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordResponse forgotPasswordResponse = passwordResetService.forgotPassword(forgotPasswordRequest);
        if (!forgotPasswordResponse.isSuccess())
            return ResponseEntity.badRequest().body(forgotPasswordResponse);
        return ResponseEntity.ok(forgotPasswordResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> postResetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse resetPasswordResponse = passwordResetService.resetPassword(resetPasswordRequest);
        if (!resetPasswordResponse.isSuccess())
            return ResponseEntity.badRequest().body(resetPasswordResponse);
        return ResponseEntity.ok(resetPasswordResponse);
    }

}
