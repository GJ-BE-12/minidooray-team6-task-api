package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.entity.dto.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.entity.dto.ProjectResponseDto;
import com.nhnacademy.taskAPI.service.ProjectService;

import java.util.List;

public class ProjectServiceImpl implements ProjectService{

    @Override
    public ProjectResponseDto createProject(Long accountId, ProjectCreateRequestDto requestDto) {
        return null;
    }

    @Override
    public List<ProjectResponseDto> findProjectsByAccountId(Long accountId) {
        return List.of();
    }

    @Override
    public ProjectResponseDto findProjectDetails(Long accountId, Long projectId) {
        return null;
    }

    @Override
    public void addMemberToProject(Long adminAccountId, Long projectId, Long accountIdToAdd) {

    }
}
