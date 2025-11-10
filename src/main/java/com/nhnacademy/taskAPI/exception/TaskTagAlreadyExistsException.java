package com.nhnacademy.taskAPI.exception;

public class TaskTagAlreadyExistsException extends RuntimeException {
    public TaskTagAlreadyExistsException(String message) {
        super(message);
    }
}