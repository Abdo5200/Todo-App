package com.example.todolist.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskResponse {
    private int id;

    private String title;

    private String description;

    private String label;

    private int userId;
}
