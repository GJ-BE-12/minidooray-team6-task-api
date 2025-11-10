package com.nhnacademy.taskAPI.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Task에 tag 추가
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAddTagRequestDto {
    private Long tagId;
}
