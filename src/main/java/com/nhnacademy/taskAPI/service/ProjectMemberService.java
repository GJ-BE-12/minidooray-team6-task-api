package com.nhnacademy.taskAPI.service;



import com.nhnacademy.taskAPI.dto.request.ProjectMemberAddRequestDto;

import java.util.List;

public interface ProjectMemberService {

    void addMembersToProject(Long adminId, Long projectId, List<ProjectMemberAddRequestDto> requestDtoList);
    void removeMemberFromProject(Long adminId, Long projectId, Long userId);
    List<Long> getMemberAccountIdsForProject(Long accountId, Long projectId);
}