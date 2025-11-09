package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.request.ProjectMemberAddRequestDto;
import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.ProjectMember;
import com.nhnacademy.taskAPI.repository.ProjectMemberRepository;
import com.nhnacademy.taskAPI.repository.ProjectRepository;
import com.nhnacademy.taskAPI.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectAuthService projectAuthService;


    @Override
    @Transactional
    public void addMembersToProject(Long adminId, Long projectId, List<ProjectMemberAddRequestDto> requestDtoList) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

        projectAuthService.checkProjectAdmin(adminId, project);

        for (ProjectMemberAddRequestDto requestDto : requestDtoList) {
            Long accountIdToAdd = requestDto.getAccountId();

            if (projectMemberRepository.existsByProjectIdAndAccountId(projectId, accountIdToAdd)) {
                continue;
            }

            ProjectMember newMember = new ProjectMember(project, accountIdToAdd);
            projectMemberRepository.save(newMember);
        }
    }

    @Override
    @Transactional
    public void removeMemberFromProject(Long adminId, Long projectId, Long userId) {
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

        projectAuthService.checkProjectAdmin(adminId, project);

        if (project.getAdminId().equals(userId)) {
            throw new RuntimeException("프로젝트 관리자(본인)는 삭제할 수 없습니다.");
        }

        ProjectMember member = projectMemberRepository.findByProjectIdAndAccountId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("해당 프로젝트의 멤버를 찾을 수 없습니다."));

        projectMemberRepository.delete(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getMemberAccountIdsForProject(Long accountId, Long projectId) {

        projectAuthService.existUserId(accountId, projectId);

        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(ProjectMember::getAccountId)
                .collect(Collectors.toList());
    }
}