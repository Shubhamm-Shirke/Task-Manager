package com.leetjourney.taskmanager.exception;

public class CategoryAlreadyExistException extends RuntimeException{
    public CategoryAlreadyExistException(String name) {
        super("Category Already Exist with name : "+ name);
    }
}
