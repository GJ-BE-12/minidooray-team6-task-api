package com.nhnacademy.taskAPI.service;



import com.nhnacademy.taskAPI.entity.dto.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.entity.dto.ProjectResponseDto;
import com.nhnacademy.taskAPI.entity.dto.ProjectStatusUpdateDto;
import com.nhnacademy.taskAPI.entity.dto.ProjectUpdateDto;

import java.util.List;

public interface ProjectService {


    ProjectResponseDto createProject(Long adminId, ProjectCreateRequestDto requestDto);
    ProjectResponseDto getProject(Long accountId, Long projectId);
    List<ProjectResponseDto> getMyProjects(Long accountId);
    ProjectResponseDto updateProject(Long adminId, Long projectId, ProjectUpdateDto requestDto);
    void deleteProject(Long adminId, Long projectId);

    /**
     * 프로젝트의 '상태'수정
     */
    ProjectResponseDto updateProjectStatus(Long adminId, Long projectId, ProjectStatusUpdateDto requestDto);
}