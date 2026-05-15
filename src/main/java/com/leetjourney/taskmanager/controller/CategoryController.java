package com.leetjourney.taskmanager.controller;

import com.leetjourney.taskmanager.entity.Category;
import com.leetjourney.taskmanager.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Category findById(@RequestParam Long id) {
        return categoryService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Category> create(
            @RequestBody Category category) {
        Category create =  categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }
}
