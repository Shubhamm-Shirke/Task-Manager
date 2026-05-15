package com.leetjourney.taskmanager.exception;

public class TaskAlreadyExistException extends RuntimeException{
    public TaskAlreadyExistException(String title) {
        super("Task Already Exist with title : "+ title);
    }
}
