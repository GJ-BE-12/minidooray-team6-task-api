package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TaskDetailsResponseDto;
import com.nhnacademy.taskAPI.dto.response.TaskResponseDto;
import com.nhnacademy.taskAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponseDto> createTask(@PathVariable Long projectId,
                                                      @RequestHeader("X-USER-ID") Long userId,
                                                      @RequestBody TaskCreateRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.createTask(userId, projectId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> getTasks(@PathVariable Long projectId,
                                                          @RequestHeader("X-USER-ID") Long userId) {
        List<TaskResponseDto> responseDto = taskService.findTasksByProjectId(userId, projectId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDetailsResponseDto> getTaskDetail(@PathVariable Long taskId,
                                                                @RequestHeader("X-USER-ID") Long userId) {
        TaskDetailsResponseDto responseDto = taskService.findTaskDetails(userId, taskId);

        return ResponseEntity.ok(responseDto);
    }
}
