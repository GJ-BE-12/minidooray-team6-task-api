package com.nhnacademy.taskAPI.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagAssignRequestDto {

    @NotNull(message = "할당할 태그 ID는 필수입니다.")
    private Long tagId;
}