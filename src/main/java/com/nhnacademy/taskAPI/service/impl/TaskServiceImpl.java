package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.TaskDetailsResponseDto;
import com.nhnacademy.taskAPI.dto.response.TaskResponseDto;
import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.Tag;
import com.nhnacademy.taskAPI.entity.Task;
import com.nhnacademy.taskAPI.repository.ProjectRepository;
import com.nhnacademy.taskAPI.repository.TaskRepository;
import com.nhnacademy.taskAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ProjectAuthService projectAuthService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public TaskResponseDto createTask(Long userId, Long projectId, TaskCreateRequestDto requestDto) {
        projectAuthService.existUserId(userId, projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(RuntimeException::new);

        Task task = new Task(project, userId, requestDto.getTitle(), requestDto.getContent(), null);
        taskRepository.save(task);

        TaskResponseDto responseDto = new TaskResponseDto(task.getId(), task.getTitle(), task.getContent(), task.getCreatorId(), null);

        return responseDto;
    }

    @Override
    public List<TaskResponseDto> findTasksByProjectId(Long userId, Long projectId) {
        projectAuthService.existUserId(userId, projectId);

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<TaskResponseDto> responseDto = new ArrayList<>();
        for (Task t : tasks) {
            responseDto.add(new TaskResponseDto(t.getId(), t.getTitle(), t.getContent(), t.getCreatorId(), t.getMilestone().getId()));
        }

        return responseDto;
    }

    @Override
    public TaskDetailsResponseDto findTaskDetails(Long userId, Long taskId) {
        Task task = taskRepository.getTaskById(taskId);
//        TaskDetailsResponseDto responseDto = new TaskDetailsResponseDto(task, , )
        return null;
    }
}
