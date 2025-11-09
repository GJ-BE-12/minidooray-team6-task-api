package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.*;
import com.nhnacademy.taskAPI.entity.*;
import com.nhnacademy.taskAPI.repository.CommentRepository;
import com.nhnacademy.taskAPI.repository.ProjectRepository;
import com.nhnacademy.taskAPI.repository.TaskRepository;
import com.nhnacademy.taskAPI.repository.TaskTagRepository;
import com.nhnacademy.taskAPI.service.CommentService;
import com.nhnacademy.taskAPI.service.MileStoneService;
import com.nhnacademy.taskAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;
    private final CommentRepository commentRepository;
    private final ProjectAuthService projectAuthService;


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
            Long milestoneId = (t.getMilestone() != null) ? t.getMilestone().getId() : null;
            responseDto.add(new TaskResponseDto(t.getId(), t.getTitle(), t.getContent(), t.getCreatorId(), milestoneId));
        }

        return responseDto;
    }

    @Override
    public TaskDetailsResponseDto findTaskDetails(Long userId, Long taskId) {
        Task task = taskRepository.getTaskById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found with id: " + taskId);
        }

        long projectId = task.getProject().getId();
        projectAuthService.existUserId(userId, projectId);

        Milestone milestone = task.getMilestone();
        List<TaskTag> taskTags = taskTagRepository.getTaskTagsByTask(task);
        List<Comment> comments = commentRepository.findByTaskId(taskId);

        Long milestoneId = (milestone != null) ? milestone.getId() : null;
        TaskResponseDto taskDto = new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCreatorId(),
                milestoneId
        );

        MileStoneResponseDto milestoneDto = (milestone != null)
                ? MileStoneResponseDto.fromEntity(milestone)
                : null;

        List<TagResponseDto> tagDtos = taskTags.stream()
                .map(TaskTag::getTag)
                .map(tag -> new TagResponseDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());

        List<CommentResponseDto> commentDtos = comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new TaskDetailsResponseDto(
                taskDto,
                milestoneDto,
                tagDtos,
                commentDtos
        );
    }
}
