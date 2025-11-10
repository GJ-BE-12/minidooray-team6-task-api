package com.nhnacademy.taskAPI.exception;

public class ProjectMemberNotFoundException extends RuntimeException {
    public ProjectMemberNotFoundException(String message) {
        super(message);
    }
}