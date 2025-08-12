package com.example.todolist.service.impl;

import com.example.todolist.Repository.UserRepo;
import com.example.todolist.entity.User;
import com.example.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User save(User user) {
        Optional<User> existUser = userRepo.findUserByEmail(user.getEmail());
        if (existUser.isPresent())
            throw new RuntimeException("User with the same email already exist");
        else
            return userRepo.save(user);
    }

    @Override
    public User findById(Integer id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty())
            throw new RuntimeException("User with id " + id + " does not exist");
        return user.get();
    }
}
