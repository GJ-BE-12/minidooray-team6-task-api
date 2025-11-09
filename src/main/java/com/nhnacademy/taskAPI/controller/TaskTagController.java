package com.nhnacademy.taskAPI.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TaskTagController {

    private final TaskService taskService;

    @PostMapping("/tasks/{taskId}/tags")
    public ResponseEntity<Void> assignTagToTask(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long taskId,
            @Valid @RequestBody TagAssignRequestDto requestDto) {

        taskService.assignTagToTask(accountId, taskId, requestDto.getTagId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/tasks/{taskId}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromTask(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long taskId,
            @PathVariable Long tagId) {

        taskService.removeTagFromTask(accountId, taskId, tagId);
        return ResponseEntity.noContent().build();
    }
}