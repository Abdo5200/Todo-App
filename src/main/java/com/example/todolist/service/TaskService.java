package com.example.todolist.service;

import com.example.todolist.DTO.*;

import java.util.List;

public interface TaskService {
    CreateTaskResponse save(CreateTaskRequest createTaskRequest);

    List<CreateTaskResponse> findAll(Integer userId);

    TaskResponse deleteTask(DeleteTaskRequest deleteTaskRequest);

    TaskResponse updateTask(PatchTaskRequest patchTaskRequest);
}
