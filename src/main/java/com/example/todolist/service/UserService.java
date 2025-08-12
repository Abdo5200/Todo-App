package com.example.todolist.service;

import com.example.todolist.entity.User;

public interface UserService {

    User save(User user);

    User findById(Integer id);
}
