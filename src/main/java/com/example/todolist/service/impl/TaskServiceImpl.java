package com.example.todolist.service.impl;

import com.example.todolist.DTO.TaskRequest;
import com.example.todolist.DTO.TaskResponse;
import com.example.todolist.Repository.TaskRepo;
import com.example.todolist.Repository.UserRepo;
import com.example.todolist.entity.Task;
import com.example.todolist.entity.User;
import com.example.todolist.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepo taskRepo;
    private final UserRepo userRepo;

    @Autowired
    public TaskServiceImpl(TaskRepo taskRepo, UserRepo userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public TaskResponse save(TaskRequest taskRequest) {
        User user = userRepo.findById(taskRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Task savedTask = taskRepo
                .save(new Task(taskRequest.getTitle(), taskRequest.getDescription(), taskRequest.getLabel(), user));
        return convertToResponse(savedTask);
    }

    @Override
    @Transactional
    public List<TaskResponse> findAll(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User is not found"));
        List<Task> tasks = taskRepo.findByUserId(userId);
        return tasks.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private TaskResponse convertToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getLabel(),
                task.getUser().getId()
        );
    }
}
