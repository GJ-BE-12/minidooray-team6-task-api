package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.dto.request.ProjectCreateRequestDto;
import com.nhnacademy.taskAPI.dto.response.ProjectDetailsDto;
import com.nhnacademy.taskAPI.dto.response.ProjectResponseDto;
import com.nhnacademy.taskAPI.dto.request.ProjectStatusUpdateDto;
import com.nhnacademy.taskAPI.dto.request.ProjectUpdateDto;
import com.nhnacademy.taskAPI.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(
            @RequestHeader("X-USER-ID") Long adminId,
            @Valid @RequestBody ProjectCreateRequestDto requestDto) {

        ProjectResponseDto createdProject = projectService.createProject(adminId, requestDto);

        URI location = URI.create("/projects/" + createdProject.getId());
        return ResponseEntity.created(location).body(createdProject);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getMyProjects(
            @RequestHeader("X-USER-ID") Long accountId) {

        List<ProjectResponseDto> myProjects = projectService.getMyProjects(accountId);
        return ResponseEntity.ok(myProjects);
    }


    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> updateProject(
            @RequestHeader("X-USER-ID") Long adminId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateDto requestDto) {

        ProjectResponseDto updatedProject = projectService.updateProject(adminId, projectId, requestDto);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailsDto> getProjectDetails(
            @RequestHeader("X-USER-ID") Long accountId,
            @PathVariable Long projectId) {

        ProjectDetailsDto project = projectService.getProject(accountId, projectId);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @RequestHeader("X-USER-ID") Long adminId,
            @PathVariable Long projectId) {

        projectService.deleteProject(adminId, projectId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{projectId}/status")
    public ResponseEntity<ProjectResponseDto> updateProjectStatus(
            @RequestHeader("X-USER-ID") Long adminId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectStatusUpdateDto requestDto) {

        ProjectResponseDto updatedProject = projectService.updateProjectStatus(adminId, projectId, requestDto);
        return ResponseEntity.ok(updatedProject);
    }
}