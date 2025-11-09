package com.nhnacademy.taskAPI.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Task 내 다른 필드를 건드리지 않는 update request
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequestDto {
    private String title;
    private String content;
}

