package com.example.todolist.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {

    @NotNull(message = "title is required")
    private String title;

    private String description;

    @NotNull(message = "label is required")
    private String label;

    @NotNull(message = "User id is required")
    private int userId;
}
