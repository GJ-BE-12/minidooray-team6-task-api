package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.exception.MemberAccessDeniedException;
import com.nhnacademy.taskAPI.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectAuthService {
    private final ProjectMemberRepository projectMemberRepository;

    public void existUserId(Long userId, Long projectId) {
        if (!projectMemberRepository.existsByProjectIdAndAccountId(userId, projectId)) {
            throw new MemberAccessDeniedException("이 프로젝트에 대한 권한이 없습니다.");
        }
    }

    //관리자 권한 확인 추가
    public void checkProjectAdmin(Long userId, Project project) {
        if (!project.getAdminId().equals(userId)) {
            throw new MemberAccessDeniedException("프로젝트 관리자 권한이 필요합니다.");
        }
    }

}
