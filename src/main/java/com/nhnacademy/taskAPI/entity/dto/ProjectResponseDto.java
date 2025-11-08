package com.nhnacademy.taskAPI.entity.dto;

import com.nhnacademy.taskAPI.entity.Project;
import com.nhnacademy.taskAPI.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectResponseDto {

    private final Long id;
    private final String name;
    private final ProjectStatus status;
    private final Long adminId;

    public static ProjectResponseDto fromEntity(Project project) {
        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getAdminId()
        );
    }
}