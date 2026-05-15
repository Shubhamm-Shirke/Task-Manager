package com.leetjourney.taskmanager.mapper;

import com.leetjourney.taskmanager.dto.CategoryResponse;
import com.leetjourney.taskmanager.dto.TaskRequest;
import com.leetjourney.taskmanager.dto.TaskResponse;
import com.leetjourney.taskmanager.entity.Category;
import com.leetjourney.taskmanager.entity.Task;
import com.leetjourney.taskmanager.service.CategoryService;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final CategoryService  categoryService;

    public TaskMapper(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Task toEntity(TaskRequest request) {
        Category category = null;
        if (request != null && request.getCategoryId() != null) {
            category = categoryService.findById(request.getCategoryId());
        }

        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .taskStatus(request.getTaskStatus() != null ? request.getTaskStatus() : false)
                .category(category)
                .build();
    }

    public TaskResponse toResponse(Task task) {
        CategoryResponse categoryResponse = null;

        if (task != null && task.getCategory() != null) {
            categoryResponse = CategoryResponse.builder()
                    .categoryId(task.getCategory().getId())
                    .name(task.getCategory().getName())
                    .description(task.getCategory().getDescription())
                    .build();
        }

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskStatus(task.getTaskStatus())
                .createdAt(task.getCreatedAt())
                .category(categoryResponse)
                .build();
    }

    public void updateEntityFromRequest(Task task, TaskRequest request) {
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setTaskStatus(request.getTaskStatus());

        if (request.getCategoryId() != null) {
            task.setCategory(categoryService.findById(request.getCategoryId()));
        }
    }
}
