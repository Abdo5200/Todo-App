package com.example.todolist.service;

import com.example.todolist.DTO.DeleteTaskRequest;
import com.example.todolist.DTO.DeleteTaskResponse;
import com.example.todolist.DTO.TaskRequest;
import com.example.todolist.DTO.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse save(TaskRequest taskRequest);

    List<TaskResponse> findAll(Integer userId);

    DeleteTaskResponse deleteTask(DeleteTaskRequest deleteTaskRequest);
}
