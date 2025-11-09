package com.nhnacademy.taskAPI.service;

import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TaskDetailsResponseDto;
import com.nhnacademy.taskAPI.dto.response.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(Long userId, Long projectId, TaskCreateRequestDto requestDto);

    List<TaskResponseDto> findTasksByProjectId(Long userId, Long projectId);

    TaskDetailsResponseDto findTaskDetails(Long userId, Long taskId);
}
