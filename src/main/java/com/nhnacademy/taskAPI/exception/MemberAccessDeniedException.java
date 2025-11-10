package com.nhnacademy.taskAPI.exception;

public class MemberAccessDeniedException extends RuntimeException {
    public MemberAccessDeniedException(String message) {
        super(message);
    }
}