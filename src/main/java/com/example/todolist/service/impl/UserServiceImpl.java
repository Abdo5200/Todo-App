package com.example.todolist.service.impl;

import com.example.todolist.DTO.LoginRequest;
import com.example.todolist.DTO.LoginResponse;
import com.example.todolist.DTO.SignupRequest;
import com.example.todolist.DTO.SignupResponse;
import com.example.todolist.Repository.UserRepo;
import com.example.todolist.entity.User;
import com.example.todolist.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User findById(Integer id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty())
            throw new RuntimeException("User with id " + id + " does not exist");
        return user.get();
    }

    @Override
    @Transactional
    public SignupResponse registerUser(SignupRequest signupRequest) {
        try {

            Optional<User> existedUser = userRepo.findByEmail(signupRequest.getEmail());
            if (existedUser.isPresent()) {
                return new SignupResponse("Email is already registered", false, null);
            }
            User user = new User();
            user.setFirstName(signupRequest.getFirstName());
            user.setLastName(signupRequest.getLastName());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            User savedUser = userRepo.save(user);
            return new SignupResponse("User registered Successfully", true, savedUser.getId());

        } catch (Exception e) {
            return new SignupResponse("Registeration failed", false, null);
        }
    }

    @Override
    @Transactional
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        try {

            Optional<User> optionalUser = userRepo.findByEmail(loginRequest.getEmail());
            if (optionalUser.isEmpty()) {
                return new LoginResponse("User not found", false, null);
            }
            User user = optionalUser.get();
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return new LoginResponse("Invalid email or password", false, null);
            }
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail()
            );
            return new LoginResponse("Login successful", true, userInfo);

        } catch (Exception e) {
            return new LoginResponse("Login failed", false, null);
        }
    }
}
