package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.dto.request.ProjectMemberAddRequestDto;
import com.nhnacademy.taskAPI.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<Void> addMembers(
            @RequestHeader("X-USER-ID") Long adminId,
            @PathVariable Long projectId,
            @Valid @RequestBody List<ProjectMemberAddRequestDto> requestDtoList) {

        projectMemberService.addMembersToProject(adminId, projectId, requestDtoList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Long>> getMembers(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long projectId) {
        
        List<Long> memberAccountIds = projectMemberService.getMemberAccountIdsForProject(accountId, projectId);
        return ResponseEntity.ok(memberAccountIds);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @RequestHeader("X-USER-ID") Long adminId,
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        
        projectMemberService.removeMemberFromProject(adminId, projectId, userId);
        return ResponseEntity.noContent().build();
    }
}