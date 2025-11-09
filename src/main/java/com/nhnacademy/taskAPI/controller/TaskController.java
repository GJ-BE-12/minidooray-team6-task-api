package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.dto.request.TaskAddTagRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskUpdateRequestDto;
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
    public ResponseEntity<List<TaskResponseDto>> findTasksByProjectId(@PathVariable Long projectId,
                                                                      @RequestHeader("X-USER-ID") Long userId) {
        List<TaskResponseDto> responseDto = taskService.findTasksByProjectId(userId, projectId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskDetailsResponseDto> findTaskDetails(@PathVariable Long taskId,
                                                                  @RequestHeader("X-USER-ID") Long userId) {
        TaskDetailsResponseDto responseDto = taskService.findTaskDetails(userId, taskId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long taskId,
                                                      @RequestHeader("X-USER-ID") Long userId,
                                                      @RequestBody TaskUpdateRequestDto requestDto) {

        TaskResponseDto responseDto = taskService.updateTask(userId, taskId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId,
                                           @RequestHeader("X-USER-ID") Long userId) {

        taskService.deleteTask(userId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tasks/{taskId}/tags")
    public ResponseEntity<Void> addTagToTask(@PathVariable Long taskId,
                                             @RequestHeader("X-USER-ID") Long userId,
                                             @RequestBody TaskAddTagRequestDto requestDto) {

        taskService.addTagToTask(userId, taskId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/tasks/{taskId}/tags/{tagId}")
    public ResponseEntity<Void> removeTagFromTask(@PathVariable Long taskId,
                                                  @PathVariable Long tagId,
                                                  @RequestHeader("X-USER-ID") Long userId) {

        taskService.removeTagFromTask(userId, taskId, tagId);
        return ResponseEntity.noContent().build();
    }
}