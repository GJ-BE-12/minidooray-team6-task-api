package com.nhnacademy.taskAPI.entity.dto;

import com.nhnacademy.taskAPI.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ProjectResponseDto {

    private final Long id;
    private final String name;
    private final ProjectStatus status;
    private final Long adminAccountId;
    private final LocalDateTime createdAt;

}
