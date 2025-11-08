package com.nhnacademy.taskAPI.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 역직렬화를 위한 기본 생성자
public class TaskUpdateDto {

    @NotBlank(message = "태스크 제목은 필수입니다.")
    private String title;
    private String content;
    private Long milestoneId;
}
