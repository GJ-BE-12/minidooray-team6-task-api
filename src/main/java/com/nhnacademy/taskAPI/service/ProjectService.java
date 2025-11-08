package com.nhnacademy.taskAPI.service;



import com.nhnacademy.taskAPI.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.ProjectResponseDto;
import com.nhnacademy.taskAPI.dto.request.ProjectStatusUpdateDto;
import com.nhnacademy.taskAPI.dto.request.ProjectUpdateDto;

import java.util.List;

public interface ProjectService {


    ProjectResponseDto createProject(Long adminId, ProjectCreateRequestDto requestDto);
    ProjectResponseDto getProject(Long accountId, Long projectId);
    List<ProjectResponseDto> getMyProjects(Long accountId);
    ProjectResponseDto updateProject(Long adminId, Long projectId, ProjectUpdateDto requestDto);
    void deleteProject(Long adminId, Long projectId);
\
    ProjectResponseDto updateProjectStatus(Long adminId, Long projectId, ProjectStatusUpdateDto requestDto);
}