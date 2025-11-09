package com.nhnacademy.taskAPI.service;

import com.nhnacademy.taskAPI.dto.request.MileStoneCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.MileStoneResponseDto;

import java.util.List;

public interface MileStoneService {
    MileStoneResponseDto createMilestone(Long accountId, Long projectId, MileStoneCreateRequestDto requestDto);
    List<MileStoneResponseDto> findMilestonesByProjectId(Long accountId, Long projectId);
    MileStoneResponseDto updateMilestone(Long accountId, Long milestoneId, MileStoneCreateRequestDto requestDto);
    void deleteMilestone(Long accountId, Long milestoneId);

}
