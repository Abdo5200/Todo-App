package com.example.todolist.controller;

import com.example.todolist.DTO.TaskRequest;
import com.example.todolist.DTO.TaskResponse;
import com.example.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class TodoController {
    private final TaskService taskService;

    @Autowired
    public TodoController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> addTask(@RequestBody TaskRequest taskRequest) {
        TaskResponse savedTask = taskService.save(taskRequest);
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping("/tasks/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable Integer userId) {
        List<TaskResponse> tasks = taskService.findAll(userId);
        return ResponseEntity.ok(tasks);
    }
}
