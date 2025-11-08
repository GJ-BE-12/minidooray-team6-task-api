package com.nhnacademy.taskAPI.controller;

import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.dto.ProjectResponseDto;
import com.nhnacademy.taskAPI.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> createProject(
            @RequestHeader("X-USER-ID") Long adminId,

    )
}
