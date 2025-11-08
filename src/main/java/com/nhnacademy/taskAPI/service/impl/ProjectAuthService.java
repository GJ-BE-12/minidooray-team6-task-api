package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectAuthService {
    private final ProjectMemberRepository projectMemberRepository;

    public void existUserId(Long userId, Long projectId) {
        if (!projectMemberRepository.existsByProjectIdAndAccountId(userId, projectId)) {
            throw new RuntimeException("이 프로젝트에 대한 권한이 없습니다.");
        }
    }

}
