package com.nhnacademy.taskAPI.service;

import com.nhnacademy.taskAPI.dto.request.TaskAddTagRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TaskDetailsResponseDto;
import com.nhnacademy.taskAPI.dto.response.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(Long userId, Long projectId, TaskCreateRequestDto requestDto);

    List<TaskResponseDto> findTasksByProjectId(Long userId, Long projectId);

    TaskDetailsResponseDto findTaskDetails(Long userId, Long taskId);

    TaskResponseDto updateTask(Long userId, Long taskId, TaskUpdateRequestDto requestDto);

    void deleteTask(Long userId, Long taskId);

    void addTagToTask(Long userId, Long taskId, TaskAddTagRequestDto requestDto);

    void removeTagFromTask(Long userId, Long taskId, Long tagId);
}