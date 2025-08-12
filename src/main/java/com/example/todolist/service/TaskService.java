package com.example.todolist.service;

import com.example.todolist.DTO.TaskRequest;
import com.example.todolist.DTO.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse save(TaskRequest taskRequest);

    List<TaskResponse> findAll(Integer userId);
}
