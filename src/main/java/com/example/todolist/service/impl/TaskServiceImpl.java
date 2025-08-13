package com.example.todolist.service.impl;

import com.example.todolist.DTO.*;
import com.example.todolist.Repository.TaskRepo;
import com.example.todolist.Repository.UserRepo;
import com.example.todolist.entity.Task;
import com.example.todolist.entity.User;
import com.example.todolist.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public CreateTaskResponse save(CreateTaskRequest createTaskRequest) {
        User user = userRepo.findById(createTaskRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Task savedTask = taskRepo
                .save(new Task(createTaskRequest.getTitle(), createTaskRequest.getDescription(), createTaskRequest.getLabel(), user));
        return convertToResponse(savedTask);
    }

    @Override
    @Transactional
    public List<CreateTaskResponse> findAll(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User is not found"));
        List<Task> tasks = taskRepo.findByUserId(userId);
        return tasks.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public TaskResponse deleteTask(DeleteTaskRequest deleteTaskRequest) {
        try {

            Optional<User> user = userRepo.findById(deleteTaskRequest.getUserId());
            if (user.isEmpty())
                return new TaskResponse("User does not exist", false);
            Optional<Task> optionalTask = taskRepo.findById(deleteTaskRequest.getTaskId());
            if (optionalTask.isEmpty())
                return new TaskResponse("Task does not exist", false);
            taskRepo.delete(optionalTask.get());
            return new TaskResponse("Deleted Task successfully", true);

        } catch (Exception e) {
            return new TaskResponse("Some Error occurred while deleting task", false);
        }
    }

    @Override
    public TaskResponse updateTask(PatchTaskRequest patchTaskRequest) {
        try {
            Optional<User> optionalUser = userRepo.findById(patchTaskRequest.getUserId());
            if (optionalUser.isEmpty())
                return new TaskResponse("User does not exist", false);
            Optional<Task> optionalTask = taskRepo.findById(patchTaskRequest.getTaskId());
            if (optionalTask.isEmpty())
                return new TaskResponse("Task does not exist", false);

            Task task = optionalTask.get();

            patchTaskRequest.getTitle().ifPresent(task::setTitle);
            patchTaskRequest.getDescription().ifPresent(task::setDescription);
            patchTaskRequest.getLabel().ifPresent(task::setLabel);

            taskRepo.save(task);

            return new TaskResponse("Updated Task successfully", true);

        } catch (Exception e) {
            return new TaskResponse("Some Error occurred while deleting task", false);
        }
    }

    private CreateTaskResponse convertToResponse(Task task) {
        return new CreateTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getLabel(),
                task.getUser().getId()
        );
    }
}
