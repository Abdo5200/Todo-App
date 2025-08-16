package com.example.todolist.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchTaskRequest {

    @NotNull(message = "Task id must be provided")
    private Integer taskId;

    @NotNull(message = "User id must be provided")

    private Integer userId;

    private Optional<String> title = Optional.empty();

    private Optional<String> description = Optional.empty();

    private Optional<String> label = Optional.empty();
}
