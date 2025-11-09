package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Task> addTaskToProject(@PathVariable Long projectId,
                                                                   @RequestHeader("X-USER-ID") Long userId,
                                                                   @RequestBody TaskCreateDto requestDto) {

        return null;
    }
}
