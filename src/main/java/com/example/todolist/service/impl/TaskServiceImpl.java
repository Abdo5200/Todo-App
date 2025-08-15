package com.example.todolist.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todolist.DTO.CreateTaskRequest;
import com.example.todolist.DTO.CreateTaskResponse;
import com.example.todolist.DTO.DeleteTaskRequest;
import com.example.todolist.DTO.PatchTaskRequest;
import com.example.todolist.DTO.TaskResponse;
import com.example.todolist.Repository.TaskRepo;
import com.example.todolist.Repository.UserRepo;
import com.example.todolist.entity.Task;
import com.example.todolist.entity.User;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.exception.UserNotFoundException;
import com.example.todolist.service.TaskService;

import jakarta.transaction.Transactional;

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
        try {
            User user = userRepo.findById(createTaskRequest.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            Task task = new Task();
            task.setTitle(createTaskRequest.getTitle());
            task.setDescription(createTaskRequest.getDescription());
            task.setLabel(createTaskRequest.getLabel());
            task.setUser(user);
            Task savedTask = taskRepo.save(task);
            return convertToCreateTaskResponse(savedTask);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create task", e);
        }
    }

    @Override
    @Transactional
    public List<CreateTaskResponse> findAll(Integer userId) {
        try {
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User is not found"));
            List<Task> tasks = taskRepo.findByUserId(userId);
            return tasks.stream().map(this::convertToCreateTaskResponse).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tasks for user: " + userId, e);
        }
    }

    @Override
    @Transactional
    public TaskResponse deleteTask(DeleteTaskRequest deleteTaskRequest) {
        try {
            Optional<User> user = userRepo.findById(deleteTaskRequest.getUserId());
            if (user.isEmpty())
                throw new UserNotFoundException("User not found");
            Optional<Task> optionalTask = taskRepo.findById(deleteTaskRequest.getTaskId());
            if (optionalTask.isEmpty())
                throw new TaskNotFoundException("Task not found");
            taskRepo.delete(optionalTask.get());
            return new TaskResponse("Deleted Task successfully", true);
        } catch (Exception e) {
            return new TaskResponse("Some Error occurred while deleting task", false);
        }
    }

    @Override
    @Transactional
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
            return new TaskResponse("Some Error occurred while updating task", false);
        }
    }

    private CreateTaskResponse convertToCreateTaskResponse(Task task) {
        return new CreateTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getLabel(),
                task.getUser().getId());
    }
}
