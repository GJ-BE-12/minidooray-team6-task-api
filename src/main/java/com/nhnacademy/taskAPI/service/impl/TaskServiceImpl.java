package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.entity.Task;
import com.nhnacademy.taskAPI.dto.request.TaskCreateDto;
import com.nhnacademy.taskAPI.dto.request.TaskUpdateDto;
import com.nhnacademy.taskAPI.repository.MilestoneRepository;
import com.nhnacademy.taskAPI.repository.TaskRepository;
import com.nhnacademy.taskAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final MilestoneRepository milestoneRepository;

    @Override
    public Task createTask(TaskCreateDto dto, Long projectId, Long creatorId) {
        return null;
    }

    @Override
    public Task updateTask(Long taskId, TaskUpdateDto dto, Long requesterId) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId, Long requesterId) {

    }

    @Override
    public List<Task> findTasksByProjectId(Long projectId) {
        return List.of();
    }
}
