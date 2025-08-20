package com.example.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todolist.DTO.CreateTaskRequest;
import com.example.todolist.DTO.CreateTaskResponse;
import com.example.todolist.DTO.DeleteTaskRequest;
import com.example.todolist.DTO.PatchTaskRequest;
import com.example.todolist.DTO.TaskResponse;
import com.example.todolist.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class TodoController {
    private final TaskService taskService;

    @Autowired
    public TodoController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<CreateTaskResponse> addTask(@RequestBody CreateTaskRequest createTaskRequest) {
        CreateTaskResponse savedTask = taskService.save(createTaskRequest);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping("/tasks/{userId}")
    public ResponseEntity<List<CreateTaskResponse>> getTasks(@PathVariable Integer userId) {
        List<CreateTaskResponse> tasks = taskService.findAll(userId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/tasks")
    public ResponseEntity<TaskResponse> postDeleteTask(@Valid @RequestBody DeleteTaskRequest deleteTaskRequest) {
        TaskResponse response = taskService.deleteTask(deleteTaskRequest);
        if (!response.isSuccess())
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/tasks")
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody PatchTaskRequest request) {
        TaskResponse response = taskService.updateTask(request);

        if (!response.isSuccess())
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

}
