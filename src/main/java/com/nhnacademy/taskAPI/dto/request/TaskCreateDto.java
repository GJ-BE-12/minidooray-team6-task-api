package com.nhnacademy.taskAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TaskCreateDto {

    @NotBlank(message = "태스크 제목은 필수입니다.")
    private String title;

    private String content;

    private Long milestoneId;

    private List<Long> tagIds;
}


