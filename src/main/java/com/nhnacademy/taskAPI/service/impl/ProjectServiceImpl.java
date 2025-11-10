package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.response.*;
import com.nhnacademy.taskAPI.entity.*;
import com.nhnacademy.taskAPI.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.dto.request.ProjectStatusUpdateDto;
import com.nhnacademy.taskAPI.dto.request.ProjectUpdateDto;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.exception.ProjectNotFoundException;
import com.nhnacademy.taskAPI.repository.*;
import com.nhnacademy.taskAPI.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MilestoneRepository milestoneRepository;
    private final TaskRepository taskRepository;
    private final TagRepository tagRepository;


    @Override
    @Transactional
    public ProjectResponseDto createProject(Long adminId, ProjectCreateRequestDto requestDto) {

        Project project = new Project(requestDto.getName(), adminId);
        Project savedProject = projectRepository.save(project);

        ProjectMember adminAsMember = new ProjectMember(savedProject, adminId);
        projectMemberRepository.save(adminAsMember);

        return ProjectResponseDto.fromEntity(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDetailsDto getProject(Long accountId, Long projectId) {

        if (!projectMemberRepository.existsByAccountIdAndProjectId(accountId, projectId)) {
            throw new MemberAccessDeniedException("이 프로젝트에 접근할 권한이 없습니다.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        ProjectResponseDto projectDto = ProjectResponseDto.fromEntity(project);

        List<Milestone> milestones = milestoneRepository.findByProjectId(projectId);
        List<MileStoneResponseDto> milestonesDtos = milestones.stream()
                .map(MileStoneResponseDto::fromEntity)
                .collect(Collectors.toList());


        List<Task> tasks = taskRepository.findByProjectId(projectId);

        List<TaskResponseDto> taskDtos = tasks.stream()
                .map(t -> {
                    Long milestoneId = (t.getMilestone() != null) ? t.getMilestone().getId() : null;

                    List<TagResponseDto> tagDtosForTask = t.getTaskTags().stream()
                            .map(taskTag -> taskTag.getTag())
                            .map(tag -> new TagResponseDto(tag.getId(), tag.getName()))
                            .collect(Collectors.toList());

                    return new TaskResponseDto(
                            t.getId(),
                            t.getTitle(),
                            t.getContent(),
                            t.getCreatorId(),
                            milestoneId
                    );
                })
                .collect(Collectors.toList());

        List<Tag> tags = tagRepository.findByProjectId(projectId);
        List<TagResponseDto> tagDtos = tags.stream()
                .map(TagResponseDto::fromEntity)
                .collect(Collectors.toList());

        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);
        List<Long> memberDtos = members.stream()
                .map(ProjectMember::getAccountId)
                .collect(Collectors.toList());

        return new ProjectDetailsDto(
                projectDto,
                milestonesDtos,
                taskDtos,
                tagDtos,
                memberDtos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getMyProjects(Long accountId) {

        List<ProjectMember> memberships = projectMemberRepository.findByAccountId(accountId);

        return memberships.stream()
                .map(ProjectMember::getProject)
                .map(ProjectResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProject(Long adminId, Long projectId, ProjectUpdateDto requestDto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getAdminId().equals(adminId)) {
            throw new MemberAccessDeniedException("프로젝트 관리자만 수정할 수 있습니다.");
        }

        project.updateProject(requestDto.getName(), requestDto.getStatus());

        return ProjectResponseDto.fromEntity(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long adminId, Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getAdminId().equals(adminId)) {
            throw new MemberAccessDeniedException("프로젝트 관리자만 삭제할 수 있습니다.");
        }

        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto updateProjectStatus(Long adminId, Long projectId, ProjectStatusUpdateDto requestDto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        if (!project.getAdminId().equals(adminId)) {
            throw new MemberAccessDeniedException("프로젝트 관리자만 상태를 수정할 수 있습니다.");
        }

        project.updateStatus(requestDto.getStatus());

        return ProjectResponseDto.fromEntity(project);
    }
}