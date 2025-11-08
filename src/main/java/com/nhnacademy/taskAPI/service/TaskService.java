package com.nhnacademy.taskAPI.service;


import com.nhnacademy.taskAPI.entity.Task;
import com.nhnacademy.taskAPI.entity.dto.TaskCreateDto;
import com.nhnacademy.taskAPI.entity.dto.TaskUpdateDto;

import java.util.List;

public interface TaskService {
    Task createTask(TaskCreateDto dto, Long projectId, Long creatorId);
    Task updateTask(Long taskId, TaskUpdateDto dto, Long requesterId);
    void deleteTask(Long taskId, Long requesterId);
    List<Task> findTasksByProjectId(Long projectId);
}
