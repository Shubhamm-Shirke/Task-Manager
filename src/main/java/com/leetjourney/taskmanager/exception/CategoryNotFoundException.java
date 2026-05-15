package com.leetjourney.taskmanager.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long id) {
        super("Category bot found with id : " + id);
    }
}
