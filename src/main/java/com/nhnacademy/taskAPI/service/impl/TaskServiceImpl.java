package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.request.TaskAddTagRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.taskAPI.dto.response.*;
import com.nhnacademy.taskAPI.entity.*;
import com.nhnacademy.taskAPI.repository.*;
import com.nhnacademy.taskAPI.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public TaskResponseDto createTask(Long userId, Long projectId, TaskCreateRequestDto requestDto) {
        projectAuthService.existUserId(userId, projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다: " + projectId));

        Task task = new Task(project, userId, requestDto.getTitle(), requestDto.getContent(), null);
        Task savedTask = taskRepository.save(task);

        return new TaskResponseDto(savedTask.getId(), savedTask.getTitle(), savedTask.getContent(), savedTask.getCreatorId(), null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> findTasksByProjectId(Long userId, Long projectId) {
        projectAuthService.existUserId(userId, projectId);

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<TaskResponseDto> responseDto = new ArrayList<>();

        for (Task t : tasks) {
            Long milestoneId = (t.getMilestone() != null) ? t.getMilestone().getId() : null;

            responseDto.add(new TaskResponseDto(
                    t.getId(),
                    t.getTitle(),
                    t.getContent(),
                    t.getCreatorId(),
                    milestoneId
            ));
        }
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDetailsResponseDto findTaskDetails(Long userId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("태스크를 찾을 수 없습니다: " + taskId));

        long projectId = task.getProject().getId();
        projectAuthService.existUserId(userId, projectId);

        Long taskMilestoneId = (task.getMilestone() != null) ? task.getMilestone().getId() : null;
        TaskResponseDto taskDto = new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCreatorId(),
                taskMilestoneId
        );

        MileStoneResponseDto milestoneDto = (task.getMilestone() != null)
                ? MileStoneResponseDto.fromEntity(task.getMilestone())
                : null;

        List<TagResponseDto> tagDtos = task.getTaskTags().stream()
                .map(taskTag -> taskTag.getTag())
                .map(tag -> new TagResponseDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());

        List<CommentResponseDto> commentDtos = task.getComments().stream()
                .map(comment -> CommentResponseDto.fromEntity(comment))
                .collect(Collectors.toList());

        return new TaskDetailsResponseDto(taskDto, milestoneDto, tagDtos, commentDtos);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTask(Long userId, Long taskId, TaskUpdateRequestDto requestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("태스크를 찾을 수 없습니다: " + taskId));

        long projectId = task.getProject().getId();
        projectAuthService.existUserId(userId, projectId);

        task.setTitle(requestDto.getTitle());
        task.setContent(requestDto.getContent());

        Long milestoneId = (task.getMilestone() != null) ? task.getMilestone().getId() : null;

        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCreatorId(),
                milestoneId
        );
    }

    @Override
    @Transactional
    public void deleteTask(Long userId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("태스크를 찾을 수 없습니다: " + taskId));

        long projectId = task.getProject().getId();
        projectAuthService.existUserId(userId, projectId);

        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public void addTagToTask(Long userId, Long taskId, TaskAddTagRequestDto requestDto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("태스크를 찾을 수 없습니다: " + taskId));

        Tag tag = tagRepository.findById(requestDto.getTagId())
                .orElseThrow(() -> new RuntimeException("태그를 찾을 수 없습니다: " + requestDto.getTagId()));

        long taskProjectId = task.getProject().getId();
        long tagProjectId = tag.getProject().getId();

        projectAuthService.existUserId(userId, taskProjectId);

        if (taskProjectId != tagProjectId) {
            throw new RuntimeException("태스크와 태그가 동일한 프로젝트에 속해있지 않습니다.");
        }

        if (taskTagRepository.existsByTask_IdAndTag_Id(taskId, requestDto.getTagId())) {
            throw new RuntimeException("이미 할당된 태그입니다.");
        }

        TaskTag taskTag = new TaskTag(task, tag);
        taskTagRepository.save(taskTag);
    }

    @Override
    @Transactional
    public void removeTagFromTask(Long userId, Long taskId, Long tagId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("태스크를 찾을 수 없습니다: " + taskId));

        long projectId = task.getProject().getId();
        projectAuthService.existUserId(userId, projectId);

        TaskTag taskTag = taskTagRepository.findByTask_IdAndTag_Id(taskId, tagId)
                .orElseThrow(() -> new RuntimeException("해당 태그가 태스크에 할당되어 있지 않습니다."));

        taskTagRepository.delete(taskTag);
    }
}