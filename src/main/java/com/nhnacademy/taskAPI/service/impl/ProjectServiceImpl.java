package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.ProjectMember;
import com.nhnacademy.taskAPI.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.ProjectResponseDto;
import com.nhnacademy.taskAPI.dto.request.ProjectStatusUpdateDto;
import com.nhnacademy.taskAPI.dto.request.ProjectUpdateDto;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.exception.ProjectNotFoundException;
import com.nhnacademy.taskAPI.repository.ProjectMemberRepository;
import com.nhnacademy.taskAPI.repository.ProjectRepository;
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
    public ProjectResponseDto getProject(Long accountId, Long projectId) {

        if (!projectMemberRepository.existsByProjectIdAndAccountId(projectId, accountId)) {
            throw new MemberAccessDeniedException("이 프로젝트에 접근할 권한이 없습니다.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다."));

        return ProjectResponseDto.fromEntity(project);
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