package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.client.AccountApiClient;
import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.dto.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.entity.dto.ProjectResponseDto;
import com.nhnacademy.taskAPI.repository.ProjectRepository;
import com.nhnacademy.taskAPI.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountApiClient accountApiClient;

    @Override
    @Transactional
    public ProjectResponseDto createProject(Long accountId, ProjectCreateRequestDto requestDto) {

        Project project = new Project(requestDto.getName(), accountId);
        Project savedProject = projectRepository.save(project);

        accountApiClient.addMemberToProject(savedProject.getId(), accountId);

        return new ProjectResponseDto(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getStatus(),
                savedProject.getAdminAccountId(),
                savedProject.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> findProjectsByAccountId(Long accountId) {
        List<Long> projectIds = accountApiClient.getProjectIdsByAccountId(accountId);

        if (projectIds.isEmpty()) {
            return Collections.emptyList();
        }

        return projectRepository.findAllById(projectIds).stream()
                .map(project -> new ProjectResponseDto(
                        project.getId(),
                        project.getName(),
                        project.getStatus(),
                        project.getAdminAccountId(),
                        project.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto findProjectDetails(Long accountId, Long projectId) {

        if (!accountApiClient.isProjectMember(projectId, accountId)) {
            throw new RuntimeException("이 프로젝트에 접근할 권한이 없습니다.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다. ID: " + projectId));

        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getAdminAccountId(),
                project.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public void addMemberToProject(Long adminAccountId, Long projectId, Long accountIdToAdd) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다. ID: " + projectId));

        // 관리자 여부 확인은 TaskApi의 책임
        if (!project.getAdminAccountId().equals(adminAccountId)) {
            throw new RuntimeException("프로젝트 관리자만 멤버를 초대할 수 있습니다.");
        }

        // 멤버 추가/중복 확인을 AccountApi에게 위임
        accountApiClient.addMemberToProject(projectId, accountIdToAdd);
    }
}