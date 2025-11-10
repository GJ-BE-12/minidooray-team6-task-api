package com.nhnacademy.taskAPI.dto.response;

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
    private final Long adminUserId;

    public static ProjectResponseDto fromEntity(Project project) {
        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getAdminId()
        );
    }
}