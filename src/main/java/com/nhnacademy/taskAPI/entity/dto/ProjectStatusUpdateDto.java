package com.nhnacademy.taskAPI.entity.dto;

import com.nhnacademy.taskAPI.entity.ProjectStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectStatusUpdateDto {

    @NotNull(message = "프로젝트 상태는 필수입니다.")
    private ProjectStatus status;
}