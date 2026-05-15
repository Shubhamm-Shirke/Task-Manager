package com.leetjourney.taskmanager.service;

import com.leetjourney.taskmanager.entity.Category;
import com.leetjourney.taskmanager.exception.CategoryAlreadyExistException;
import com.leetjourney.taskmanager.exception.CategoryNotFoundException;
import com.leetjourney.taskmanager.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Category create(Category category) {
        categoryRepository.findByName(category.getName())
                .ifPresent(existing -> {
                    throw new CategoryAlreadyExistException(category.getName());
                });
        return categoryRepository.save(category);
    }
}
