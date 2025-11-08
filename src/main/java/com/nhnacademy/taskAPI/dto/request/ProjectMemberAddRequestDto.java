package com.nhnacademy.taskAPI.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectMemberAddRequestDto {

    @NotNull(message = "추가할 멤버의 ID는 필수입니다.")
    private Long accountId;
}