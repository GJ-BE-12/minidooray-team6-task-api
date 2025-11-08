package com.nhnacademy.taskAPI.service;


import com.nhnacademy.taskAPI.dto.response.AccountResponseDto;
import com.nhnacademy.taskAPI.dto.request.ProjectMemberAddRequestDto;

import java.util.List;

public interface ProjectMemberService {

    void addMemberToProject(Long adminId, Long projectId, ProjectMemberAddRequestDto requestDto);
    void removeMemberFromProject(Long adminId, Long projectId, Long accountIdToRemove);

    List<AccountResponseDto> getMembersForProject(Long accountId, Long projectId);
}