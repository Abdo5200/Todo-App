package com.example.todolist.Repository;

import com.example.todolist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(int userId);
}
