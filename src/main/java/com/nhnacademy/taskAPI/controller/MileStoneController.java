package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.dto.request.MileStoneCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.MileStoneResponseDto;
import com.nhnacademy.taskAPI.entity.Milestone;
import com.nhnacademy.taskAPI.service.MileStoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MileStoneController {
    private final MileStoneService milestoneService;

    @PostMapping("/projects/{projectId}/milestones")
    public ResponseEntity<MileStoneResponseDto> createMilestone(
            @RequestHeader("X-USER-ID") Long adminId,
            @PathVariable Long projectId,
            @Valid @RequestBody MileStoneCreateRequestDto requestDto){

        MileStoneResponseDto createMilestone = milestoneService.createMilestone(adminId, projectId, requestDto);
        return ResponseEntity.ok(createMilestone);
    }

    @GetMapping("/projects/{projectId}/milestones")
    public ResponseEntity<List<MileStoneResponseDto>> getMilestonesForProject(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long projectId) {

        List<MileStoneResponseDto> milestones = milestoneService.findMilestonesByProjectId(accountId, projectId);
        return ResponseEntity.ok(milestones);
    }

    @PutMapping("/projects/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<MileStoneResponseDto> updateMilestone(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long milestoneId,
            @Valid @RequestBody MileStoneCreateRequestDto requestDto){

        MileStoneResponseDto updateMilestone = milestoneService.updateMilestone(accountId, milestoneId, requestDto);
        return ResponseEntity.ok(updateMilestone);
    }

    @DeleteMapping("/projects/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long milestoneId) {

        milestoneService.deleteMilestone(accountId, milestoneId);
        return ResponseEntity.noContent().build();
    }
}

