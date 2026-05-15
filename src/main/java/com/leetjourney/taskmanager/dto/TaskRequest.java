package com.leetjourney.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequest{
        @NotBlank(message = "Title is mandatory")
        @Size(min= 3, max=100, message = "Title must be between 3 and 100 characters")
        String title;

        @Size(min = 3, max = 500, message = "Description can be up to 500 characters")
        String description;
        Boolean taskStatus;
        Long categoryId;
}
