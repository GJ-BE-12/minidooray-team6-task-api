package com.nhnacademy.taskAPI.service.impl;

import com.nhnacademy.taskAPI.dto.request.MileStoneCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.MileStoneResponseDto;
import com.nhnacademy.taskAPI.entity.Milestone;
import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.exception.MilestoneNotFoundException;
import com.nhnacademy.taskAPI.exception.ProjectNotFoundException;
import com.nhnacademy.taskAPI.repository.MilestoneRepository;
import com.nhnacademy.taskAPI.repository.ProjectRepository;
import com.nhnacademy.taskAPI.service.MileStoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MileStoneServiceImpl implements MileStoneService {
    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final ProjectAuthService projectAuthService;

    @Override
    @Transactional
    public MileStoneResponseDto createMilestone(Long accountId, Long projectId, MileStoneCreateRequestDto requestDto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다"));

        projectAuthService.checkProjectAdmin(accountId, project);

        Milestone milestone = new Milestone(project, requestDto.getName());
        Milestone saveMilestone = milestoneRepository.save(milestone);
        return MileStoneResponseDto.fromEntity(saveMilestone);
    }

    @Override
    public List<MileStoneResponseDto> findMilestonesByProjectId(Long accountId, Long projectId) {
        projectAuthService.existUserId(accountId, projectId);

        List<Milestone> milestones = milestoneRepository.findByProjectId(projectId);

        return milestones.stream()
                .map(MileStoneResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MileStoneResponseDto updateMilestone(Long accountId, Long milestoneId, MileStoneCreateRequestDto requestDto) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new MilestoneNotFoundException("마일스톤을 찾을 수 없습니다"));

        projectAuthService.checkProjectAdmin(accountId, milestone.getProject());

        milestone.updateMilestone(requestDto.getName());
        return MileStoneResponseDto.fromEntity(milestone);
    }

    @Override
    public void deleteMilestone(Long accountId, Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new MilestoneNotFoundException("마일스톤을 찾을 수 없습니다"));
        projectAuthService.checkProjectAdmin(accountId, milestone.getProject());

        milestoneRepository.delete(milestone);
    }
}
