package com.nhnacademy.taskAPI.service;

import com.nhnacademy.taskAPI.entity.dto.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.entity.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(Long accountId, ProjectCreateRequestDto requestDto);

    List<ProjectResponseDto> findProjectsByAccountId(Long accountId);

    ProjectResponseDto findProjectDetails(Long accountId, Long projectId);

    void addMemberToProject(Long adminAccountId, Long projectId, Long accountIdToAdd);
}
