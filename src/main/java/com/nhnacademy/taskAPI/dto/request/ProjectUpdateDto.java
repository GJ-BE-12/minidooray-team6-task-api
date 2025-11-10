package com.nhnacademy.taskAPI.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.taskAPI.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProjectUpdateDto {

    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    @Size(max = 100, message = "프로젝트 이름은 100자를 초과할 수 없습니다.")
    private String name;

    @NotNull(message = "프로젝트 상태는 필수입니다.")
    private ProjectStatus status;
}